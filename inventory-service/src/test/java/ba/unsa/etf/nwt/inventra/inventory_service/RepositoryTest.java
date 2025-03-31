package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Supplier;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.SupplierRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.WarehouseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testNPlusOneProblem_articleRepository() {
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
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        List<Warehouse> warehouses = warehouseRepository.findAll();
        assertFalse(warehouses.isEmpty());

        long queryCount = stats.getQueryExecutionCount();
        System.out.println("Number of queries executed: " + queryCount);

        assertTrue(queryCount <= 2, "Possible N+1 issue detected!");
    }

    @Test
    void testNPlusOneProblem_supplierRepository() {
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        List<Supplier> suppliers = supplierRepository.findAll();
        assertFalse(suppliers.isEmpty());

        long queryCount = stats.getQueryExecutionCount();
        System.out.println("Number of queries executed: " + queryCount);

        assertTrue(queryCount <= 2, "Possible N+1 issue detected!");
    }
}