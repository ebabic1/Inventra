package ba.unsa.etf.nwt.inventra.inventory_service.config;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.WarehouseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private final LocationRepository locationRepository;
    private final ArticleRepository articleRepository;
    private final WarehouseRepository warehouseRepository;

    public LoadDatabase(LocationRepository locationRepository,
                        ArticleRepository articleRepository,
                        WarehouseRepository warehouseRepository) {
        this.locationRepository = locationRepository;
        this.articleRepository = articleRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            Warehouse warehouse1 = new Warehouse();
            warehouse1.setName("Main warehouse");
            warehouseRepository.save(warehouse1);

            Location location1 = new Location();
            location1.setName("Location X");
            location1.setWarehouse(warehouse1);
            location1.setIsCapacityFull(true);
            locationRepository.save(location1);

            Location location2 = new Location();
            location2.setName("Location Y");
            location2.setWarehouse(warehouse1);
            location2.setIsCapacityFull(false);
            locationRepository.save(location2);

            Article article1 = new Article();
            article1.setName("Article 1");
            article1.setSupplierId(1L);
            article1.setLocation(location1);
            article1.setPrice(3.99);
            articleRepository.save(article1);

            Article article2 = new Article();
            article2.setName("Article 2");
            article2.setSupplierId(2L);
            article2.setLocation(location2);
            article2.setPrice(20.5);
            articleRepository.save(article2);
        };
    }
}
