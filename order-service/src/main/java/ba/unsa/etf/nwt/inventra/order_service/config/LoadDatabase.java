package ba.unsa.etf.nwt.inventra.order_service.config;

import ba.unsa.etf.nwt.inventra.order_service.model.*;
import ba.unsa.etf.nwt.inventra.order_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.order_service.repository.OrderArticleRepository;
import ba.unsa.etf.nwt.inventra.order_service.repository.OrderRepository;
import ba.unsa.etf.nwt.inventra.order_service.repository.SupplierRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class LoadDatabase {

    private final ArticleRepository articleRepository;
    private final OrderRepository orderRepository;
    private final OrderArticleRepository orderArticleRepository;
    private final SupplierRepository supplierRepository;

    public LoadDatabase(ArticleRepository articleRepository,
                        OrderArticleRepository orderArticleRepository,
                        OrderRepository orderRepository,
                        SupplierRepository supplierRepository) {
        this.articleRepository = articleRepository;
        this.orderRepository = orderRepository;
        this.orderArticleRepository = orderArticleRepository;
        this.supplierRepository = supplierRepository;
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            Supplier supplier1 = new Supplier();
            supplier1.setName("Tech Supplies Ltd.");
            supplier1.setEmail("tech@gmail.com");
            supplier1.setCategory(SupplierCategory.LOCAL);
            supplier1.setPhone("12345678999");
            supplier1 = supplierRepository.save(supplier1);

            Supplier supplier2 = new Supplier();
            supplier2.setName("Office Essentials Co.");
            supplier2.setEmail("tech@gmail.com");
            supplier2.setCategory(SupplierCategory.LOCAL);
            supplier2.setPhone("12345678999");
            supplier2 = supplierRepository.save(supplier2);

            Article article1 = new Article();
            article1.setName("Laptop");
            article1.setPrice(1200.00);
            article1 = articleRepository.save(article1);

            Article article2 = new Article();
            article2.setName("Desk Chair");
            article2.setPrice(250.50);
            article2 = articleRepository.save(article2);

            Order order1 = new Order();
            order1.setName("Office Equipment Order");
            order1.setUserId(1L);
            order1.setSupplier(supplier1);
            order1.setOrderDate(LocalDate.now());
            order1.setExpiryDate(LocalDate.now().plusDays(30));
            order1.setInvoice("INV-2024001");
            order1.setNote("Urgent delivery required.");
            order1.setSupplier(supplier1);
            order1 = orderRepository.save(order1);

            Order order2 = new Order();
            order2.setName("Workstation Setup Order");
            order2.setUserId(2L);
            order2.setSupplier(supplier2);
            order2.setOrderDate(LocalDate.now());
            order2.setExpiryDate(LocalDate.now().plusDays(20));
            order2.setInvoice("INV-2024002");
            order2.setNote("Ensure quality checks.");
            order2.setSupplier(supplier2);
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
        };
    }
}