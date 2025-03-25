package ba.unsa.etf.nwt.inventra.reporting_service;

import ba.unsa.etf.nwt.inventra.reporting_service.model.*;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class ReportingServiceApplication implements CommandLineRunner {

	private final ArticleRepository articleRepository;
	private final OrderArticleRepository orderArticleRepository;
	private final OrderReportRepository orderReportRepository;
	private final OrderRepository orderRepository;
	private final ReportRepository reportRepository;

	public ReportingServiceApplication(ArticleRepository articleRepository,
									   OrderArticleRepository orderArticleRepository,
									   OrderReportRepository orderReportRepository,
									   OrderRepository orderRepository,
									   ReportRepository reportRepository) {
		this.articleRepository = articleRepository;
		this.orderArticleRepository = orderArticleRepository;
		this.orderReportRepository = orderReportRepository;
		this.orderRepository = orderRepository;
		this.reportRepository = reportRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(ReportingServiceApplication.class, args);
	}

	@Override
	public void run(String ... args) {

		Article article1 = new Article();
		article1.setName("Laptop");
		article1.setPrice(1200.00);
		article1 = articleRepository.save(article1);

		Article article2 = new Article();
		article2.setName("Desk Chair");
		article2.setPrice(250.50);
		article2 = articleRepository.save(article2);

		Order order1 = new Order();
		order1.setOrderDate(LocalDate.now());
		order1 = orderRepository.save(order1);

		Order order2 = new Order();
		order2.setOrderDate(LocalDate.now());
		order2 = orderRepository.save(order2);

		OrderArticle orderArticle1 = new OrderArticle();
		orderArticle1.setOrder(order1);
		orderArticle1.setArticle(article1);
		orderArticle1.setQuantity(5);
		orderArticleRepository.save(orderArticle1);

		OrderArticle orderArticle2 = new OrderArticle();
		orderArticle2.setOrder(order2);
		orderArticle2.setArticle(article2);
		orderArticle2.setQuantity(10);
		orderArticleRepository.save(orderArticle2);

		Report report1 = new Report();
		report1.setGeneratedAt(LocalDate.now());
		reportRepository.save(report1);

		Report report2 = new Report();
		report2.setGeneratedAt(LocalDate.now());
		reportRepository.save(report2);

		OrderReport orderReport1 = new OrderReport();
		orderReport1.setReport(report1);
		orderReport1.setOrderArticle(orderArticle1);
		orderReportRepository.save(orderReport1);

		OrderReport orderReport2 = new OrderReport();
		orderReport2.setReport(report2);
		orderReport2.setOrderArticle(orderArticle2);
		orderReportRepository.save(orderReport2);
	}
}
