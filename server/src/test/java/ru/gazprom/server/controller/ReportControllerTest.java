package ru.gazprom.server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import ru.gazprom.server.dto.ReportCategory;
import ru.gazprom.server.dto.ReportRow;
import ru.gazprom.server.exception.ThreadError;
import ru.gazprom.server.service.ReportService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;

public class ReportControllerTest {

    private ReportService reportService;
    private WebTestClient client;


    @BeforeEach
    void setUp() {
        reportService = Mockito.mock(ReportService.class);
        client = MockMvcWebTestClient
                .bindToController(new ReportController(reportService))
                .build();
    }

    @Test
    void reportJdbc_findCategories() {
        ReportCategory category = new ReportCategory(1L, "Jeans",
                List.of(new ReportRow(2L, "T-Shirt", null)), 0);
        Mockito.when(reportService.reportJdbc()).thenReturn(responseSuccess("reportJdbc", List.of(category)));

        client.get().uri("/api/reports/stock/jdbc")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody();

        verify(reportService).reportJdbc();

    }

    @Test
    void reportJdbc_whenReportIsRunning_returnsError() {
        Mockito.when(reportService.reportJdbc())
                .thenReturn(responseError("reportJdbc", new ThreadError("Report is running")));

        client.get().uri("/api/reports/stock/jdbc")
                .exchange()
                .expectStatus().isOk()
                .expectBody();

    }
}
