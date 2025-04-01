package ba.unsa.etf.nwt.inventra.reporting_service.service;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderSummaryDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.ReportRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public byte[] generateOrderSummaryReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderSummaryDTO> summaries = reportRepository.getOrderSummaries(startDate, endDate);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Order Summary Report")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Report Period: " + startDate.toLocalDate() + " to " + endDate.toLocalDate())
                    .setItalic().setFontSize(12));

            document.add(new Paragraph("\n"));

            Table table = new Table(new float[]{2, 3, 4, 2, 2});
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell("Order ID");
            table.addHeaderCell("Order Date");
            table.addHeaderCell("Article Name");
            table.addHeaderCell("Quantity");
            table.addHeaderCell("Total Price");

            summaries.forEach(summary -> {
                table.addCell(summary.getId().toString());
                table.addCell(summary.getOrderDate().toString());
                table.addCell(summary.getArticleName());
                table.addCell(summary.getQuantity().toString());
                table.addCell(String.format("%.2f", summary.getTotalPrice()));
            });

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }

    public byte[] generateArticleOrderedReport() {
        List<ArticleDTO> articles = reportRepository.findArticlesOrderedByQuantity();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Most Ordered Articles Report")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            Table table = new Table(new float[]{3, 3, 3, 2});
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell("Article Name");
            table.addHeaderCell("Price");
            table.addHeaderCell("Category");
            table.addHeaderCell("Total Quantity Ordered");

            articles.forEach(article -> {
                table.addCell(article.getName() != null ? article.getName() : "Unknown");
                table.addCell(article.getPrice() != 0 ? String.format("%.2f", article.getPrice()) : "0.00");
                table.addCell(article.getCategory() != null ? article.getCategory() : "Unknown");
                table.addCell(article.getTotalQuantityOrdered() != null ? String.valueOf(article.getTotalQuantityOrdered()) : "0");
            });

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Article Ordered PDF report", e);
        }
    }
}
