package ba.unsa.etf.nwt.inventra.order_service;

import static org.assertj.core.api.Assertions.assertThat;

import ba.unsa.etf.nwt.inventra.order_service.model.*;
import ba.unsa.etf.nwt.inventra.order_service.repository.*;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private OrderArticleRepository orderArticleRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Statistics statistics;

    @BeforeEach
    void setUp() {
        statistics = entityManager.unwrap(org.hibernate.Session.class).getSessionFactory().getStatistics();
        statistics.setStatisticsEnabled(true);
    }

    @Test
    void testFindAllArticles() {
        statistics.clear();
        List<Article> articles = articleRepository.findAll();
        assertThat(articles).isNotEmpty();
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void testFindAllOrders() {
        statistics.clear();
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).isNotEmpty();
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void testFindAllOrderArticles() {
        statistics.clear();
        List<OrderArticle> orderArticles = orderArticleRepository.findAll();
        assertThat(orderArticles).isNotEmpty();
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void testFindAllSuppliers() {
        statistics.clear();
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).isNotEmpty();
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }
}