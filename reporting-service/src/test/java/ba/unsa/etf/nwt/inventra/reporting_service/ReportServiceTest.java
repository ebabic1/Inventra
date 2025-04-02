package ba.unsa.etf.nwt.inventra.reporting_service;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderSummaryDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.ReportRepository;
import ba.unsa.etf.nwt.inventra.reporting_service.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        endDate = LocalDateTime.of(2024, 1, 31, 23, 59);
    }

    @Test
    void generateOrderSummaryReport_ShouldReturnNonEmptyPDF() {
        // Arrange
        OrderSummaryDTO orderSummary = new OrderSummaryDTO(1L, LocalDateTime.now(), "Article A", 10, 100.0);
        when(reportRepository.getOrderSummaries(startDate, endDate)).thenReturn(List.of(orderSummary));

        // Act
        byte[] pdfBytes = reportService.generateOrderSummaryReport(startDate, endDate);

        // Assert
        assertThat(pdfBytes).isNotEmpty();
        verify(reportRepository, times(1)).getOrderSummaries(startDate, endDate);
    }

    @Test
    void generateOrderSummaryReport_WithNoData_ShouldReturnEmptyPDF() {
        // Arrange
        when(reportRepository.getOrderSummaries(startDate, endDate)).thenReturn(Collections.emptyList());

        // Act
        byte[] pdfBytes = reportService.generateOrderSummaryReport(startDate, endDate);

        // Assert
        assertThat(pdfBytes).isNotEmpty(); // Even an empty PDF should not be null
        verify(reportRepository, times(1)).getOrderSummaries(startDate, endDate);
    }

    @Test
    void generateOrderSummaryReport_ShouldThrowException_WhenErrorOccurs() {
        // Arrange
        when(reportRepository.getOrderSummaries(startDate, endDate)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> reportService.generateOrderSummaryReport(startDate, endDate));
        verify(reportRepository, times(1)).getOrderSummaries(startDate, endDate);
    }

    @Test
    void generateArticleOrderedReport_ShouldReturnNonEmptyPDF() {
        // Arrange
        ArticleDTO article = new ArticleDTO("Article A", 50.0, "Category 1", 100L);
        when(reportRepository.findArticlesOrderedByQuantity()).thenReturn(List.of(article));

        // Act
        byte[] pdfBytes = reportService.generateArticleOrderedReport();

        // Assert
        assertThat(pdfBytes).isNotEmpty();
        verify(reportRepository, times(1)).findArticlesOrderedByQuantity();
    }

    @Test
    void generateArticleOrderedReport_WithNoData_ShouldReturnEmptyPDF() {
        // Arrange
        when(reportRepository.findArticlesOrderedByQuantity()).thenReturn(Collections.emptyList());

        // Act
        byte[] pdfBytes = reportService.generateArticleOrderedReport();

        // Assert
        assertThat(pdfBytes).isNotEmpty();
        verify(reportRepository, times(1)).findArticlesOrderedByQuantity();
    }
}
