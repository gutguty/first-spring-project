package ru.gazprom.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gazprom.server.dto.ReportCategory;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/stock")
    public Response<List<ReportCategory>> stockSummary() {
        return reportService.report();
    }

    @GetMapping("/stock/jdbc")
    public Response<List<ReportCategory>> reportJdbc() {
        return reportService.reportJdbc();
    }
}