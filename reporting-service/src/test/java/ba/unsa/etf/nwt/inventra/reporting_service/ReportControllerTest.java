package ba.unsa.etf.nwt.inventra.reporting_service;

import ba.unsa.etf.nwt.inventra.reporting_service.controller.ReportController;
import ba.unsa.etf.nwt.inventra.reporting_service.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @Test
    void getOrderSummaryReport_ShouldReturnPDF() throws Exception {
        // Arrange
        byte[] fakePdf = "Fake PDF Content".getBytes(StandardCharsets.UTF_8);
        Mockito.when(reportService.generateOrderSummaryReport(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(fakePdf);

        // Act & Assert
        mockMvc.perform(get("/api/reports/orders")
                        .param("startDate", "2024-01-01T00:00:00")
                        .param("endDate", "2024-01-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=order_report.pdf"))
                .andExpect(content().bytes(fakePdf));
    }

    @Test
    void generateArticleOrderedReport_ShouldReturnPDF() throws Exception {
        // Arrange
        byte[] fakePdf = "Fake PDF Content".getBytes(StandardCharsets.UTF_8);
        Mockito.when(reportService.generateArticleOrderedReport()).thenReturn(fakePdf);

        // Act & Assert
        mockMvc.perform(get("/api/reports/most-ordered-articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=most_ordered_articles_report.pdf"))
                .andExpect(content().bytes(fakePdf));
    }
}