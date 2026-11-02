package ru.gazprom.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.*;
import ru.gazprom.server.exception.CategoryNotFoundError;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ThreadError;
import ru.gazprom.server.repository.ReportJdbcRepository;
import ru.gazprom.server.service.quantity.QuantityProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;

@Service
@Slf4j
public class ReportService {

    private final CategoryService categoryService;
    private final ExecutorService executorService;
    private final CardService cardService;
    private final QuantityProvider quantityProvider;
    private final ReportJdbcRepository reportJdbcRepository;
    private final ReentrantLock reportLock = new ReentrantLock();

    public ReportService(CategoryService categoryService, ExecutorService executorService, CardService cardService, QuantityProvider quantityProvider, ReportJdbcRepository reportJdbcRepository) {
        this.categoryService = categoryService;
        this.executorService = executorService;
        this.cardService = cardService;
        this.quantityProvider = quantityProvider;
        this.reportJdbcRepository = reportJdbcRepository;
    }

    private ReportCategory buildReportForCategory(CategoryDTO categoryDTO) {
        List<ReportRow> result = new ArrayList<>();
        int total = 0;
        for (CardDTO card : cardService.findByCategoryId(categoryDTO.getId()).getData()) {
            Integer quantity = quantityProvider.getQuantity(card.getId());
            result.add(new ReportRow(card.getId(), card.getTitle(), quantity));
            if (quantity != null) {
                total += quantity;
            }
        }

        return new ReportCategory(categoryDTO.getId(), categoryDTO.getName(), result, total);
    }

    public Response<List<ReportCategory>> report() {
        if (!reportLock.tryLock()) {
            return responseError("report", new ThreadError("Report is running"));
        }
        try {
            List<Callable<ReportCategory>> tasks = new ArrayList<>();
            List<CategoryDTO> categories = categoryService.getAllCategories().getData();

            if (categories == null) {
                return responseError("report", new CategoryNotFoundError("Categories not found in report"));
            }

            for (CategoryDTO categoryDTO : categories) {
                tasks.add(() -> buildReportForCategory(categoryDTO));
            }

            List<ReportCategory> result = new ArrayList<>();
            try {
                List<Future<ReportCategory>> futures = executorService.invokeAll(tasks);
                for (Future<ReportCategory> future : futures) {
                    result.add(future.get());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return responseError("report", new ThreadError("Thread interrupt " + e.getMessage()));
            } catch (ExecutionException e) {
                return responseError("report", new ThreadError("Error to extract data from future " + e.getMessage()));
            }
            return responseSuccess("report", result);
        } finally {
            reportLock.unlock();
        }
    }

    public Response<List<ReportCategory>> reportJdbc() {
        log.info("reportJdbc started");
        List<CategoryDTO> categories = reportJdbcRepository.findCategories();
        log.debug("Found {} categories", categories.size());

        List<Long> ids = new ArrayList<>();
        for (CategoryDTO categoryDTO: categories) {
            ids.add(categoryDTO.getId());
        }

        List<CardRow> cards = reportJdbcRepository.findCardsByCategory(ids);
        log.debug("Found {} cards for categories {}", cards.size(), ids);

        Map<Long, List<ReportRow>> rowsByCategory = new HashMap<>();

        for (CardRow cardRow: cards) {
            if (!rowsByCategory.containsKey(cardRow.getCategoryId())) {
                rowsByCategory.put(cardRow.getCategoryId(), new ArrayList<>());
            }
            rowsByCategory.get(cardRow.getCategoryId()).add(new ReportRow(cardRow.getId(), cardRow.getTitle(), null));
        }

        List<ReportCategory> result = new ArrayList<>();

        for (CategoryDTO categoryDTO: categories) {
            List<ReportRow> rows = rowsByCategory.getOrDefault(categoryDTO.getId(), List.of());
            result.add(new ReportCategory(categoryDTO.getId(), categoryDTO.getName(), rows, 0));
        }

        log.info("reportJdbc ended, {} categories", result.size());
        return responseSuccess("reportJdbc", result);
    }

}