package ba.unsa.etf.nwt.inventra.reporting_service.config;

import ba.unsa.etf.nwt.inventra.reporting_service.model.ReportType;
import ba.unsa.etf.nwt.inventra.reporting_service.model.*;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ArticleRepository articleRepository,
                                   OrderArticleRepository orderArticleRepository,
                                   OrderReportRepository orderReportRepository,
                                   OrderRepository orderRepository,
                                   ReportRepository reportRepository) {
        return args -> {

            Article article1 = new Article();
            article1.setName("Laptop");
            article1.setPrice(1200.00);
            article1 = articleRepository.save(article1);
            log.info("Preloaded: {}", article1);

            Article article2 = new Article();
            article2.setName("Desk Chair");
            article2.setPrice(250.50);
            article2 = articleRepository.save(article2);
            log.info("Preloaded: {}", article2);

            Order order1 = new Order();
            order1.setOrderDate(LocalDateTime.now());
            order1 = orderRepository.save(order1);
            log.info("Preloaded: {}", order1);

            Order order2 = new Order();
            order2.setOrderDate(LocalDateTime.now());
            order2 = orderRepository.save(order2);
            log.info("Preloaded: {}", order2);

            OrderArticle orderArticle1 = new OrderArticle();
            orderArticle1.setOrder(order1);
            orderArticle1.setArticle(article1);
            orderArticle1.setQuantity(5);
            orderArticleRepository.save(orderArticle1);
            log.info("Preloaded: {}", orderArticle1);

            OrderArticle orderArticle2 = new OrderArticle();
            orderArticle2.setOrder(order2);
            orderArticle2.setArticle(article2);
            orderArticle2.setQuantity(10);
            orderArticleRepository.save(orderArticle2);
            log.info("Preloaded: {}", orderArticle2);

            Report report1 = new Report();
            report1.setGeneratedAt(LocalDateTime.now());
            report1.setUserId(1L);
            report1.setType(ReportType.FINANCIAL);
            reportRepository.save(report1);
            log.info("Preloaded: {}", report1);

            Report report2 = new Report();
            report2.setGeneratedAt(LocalDateTime.now());
            report2.setUserId(1L);
            report2.setType(ReportType.FINANCIAL);
            reportRepository.save(report2);
            log.info("Preloaded: {}", report2);

            OrderReport orderReport1 = new OrderReport();
            orderReport1.setReport(report1);
            orderReport1.setOrderArticle(orderArticle1);
            orderReportRepository.save(orderReport1);
            log.info("Preloaded: {}", orderReport1);

            OrderReport orderReport2 = new OrderReport();
            orderReport2.setReport(report2);
            orderReport2.setOrderArticle(orderArticle2);
            orderReportRepository.save(orderReport2);
            log.info("Preloaded: {}", orderReport2);
        };
    }
}
