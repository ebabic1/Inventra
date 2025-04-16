package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.WarehouseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Statistics statistics;

    @BeforeEach
    void setUp() {
        statistics = entityManager.unwrap(org.hibernate.Session.class).getSessionFactory().getStatistics();
        statistics.setStatisticsEnabled(true);
    }

    @Test
    void testNPlusOneProblem_articleRepository() {
        statistics.clear();

        List<Article> articles = articleRepository.findAll();
        assertFalse(articles.isEmpty());

        long queryCount = statistics.getQueryExecutionCount();
        System.out.println("Number of queries executed: " + queryCount);

        assertTrue(queryCount <= 2, "Possible N+1 issue detected!");
    }

    @Test
    void testNPlusOneProblem_locationRepository() {
        statistics.clear();

        List<Location> articles = locationRepository.findAll();
        assertFalse(articles.isEmpty());

        long queryCount = statistics.getQueryExecutionCount();
        System.out.println("Number of queries executed: " + queryCount);

        assertTrue(queryCount <= 2, "Possible N+1 issue detected!");
    }

    @Test
    void testNPlusOneProblem_warehouseRepository() {
        statistics.clear();

        List<Warehouse> warehouses = warehouseRepository.findAll();
        assertFalse(warehouses.isEmpty());

        long queryCount = statistics.getQueryExecutionCount();
        System.out.println("Number of queries executed: " + queryCount);

        assertTrue(queryCount <= 2, "Possible N+1 issue detected!");
    }
}