package ba.unsa.etf.nwt.inventra.order_service.client;

import ba.unsa.etf.nwt.inventra.order_service.dto.ArticleResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.LocationResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Component
public class InventoryClient {

    private final RestTemplate restTemplate;

    public InventoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ArticleResponseDTO fetchArticle(Long articleId) {
        String url = "http://inventory-service/api/articles/" + articleId;
        try {
            return restTemplate.getForObject(url, ArticleResponseDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Inventory service unavailable while fetching article with ID: " + articleId);
        }
    }

    public LocationResponseDTO fetchLocation(Long locationId) {
        String url = "http://inventory-service/api/locations/" + locationId;
        try {
            return restTemplate.getForObject(url, LocationResponseDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Inventory service unavailable while fetching location with ID: " + locationId);
        }
    }
}
