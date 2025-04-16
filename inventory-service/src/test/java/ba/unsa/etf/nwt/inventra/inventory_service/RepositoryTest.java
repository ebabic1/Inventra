package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.WarehouseRepository;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void testNPlusOneProblem_articleRepository() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        List<Article> articles = articleRepository.findAll();
        assertFalse(articles.isEmpty());

        long queryCount = stats.getQueryExecutionCount();
        System.out.println("Number of queries executed: " + queryCount);

        assertTrue(queryCount <= 2, "Possible N+1 issue detected!");
    }

    @Test
    void testNPlusOneProblem_locationRepository() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        List<Location> articles = locationRepository.findAll();
        assertFalse(articles.isEmpty());

        long queryCount = stats.getQueryExecutionCount();
        System.out.println("Number of queries executed: " + queryCount);

        assertTrue(queryCount <= 2, "Possible N+1 issue detected!");
    }

    @Test
    void testNPlusOneProblem_warehouseRepository() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        List<Warehouse> warehouses = warehouseRepository.findAll();
        assertFalse(warehouses.isEmpty());

        long queryCount = stats.getQueryExecutionCount();
        System.out.println("Number of queries executed: " + queryCount);

        assertTrue(queryCount <= 2, "Possible N+1 issue detected!");
    }
}