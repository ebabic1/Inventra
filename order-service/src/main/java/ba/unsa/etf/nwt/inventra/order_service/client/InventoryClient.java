package ba.unsa.etf.nwt.inventra.order_service.client;

import ba.unsa.etf.nwt.inventra.order_service.dto.ArticleResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.LocationResponseDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {

    private final RestTemplate restTemplate;

    public InventoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ArticleResponseDTO fetchArticle(Long articleId) {
        String url = "http://inventory-service/api/articles/" + articleId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Auth", "true");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ArticleResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    ArticleResponseDTO.class
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Inventory service unavailable while fetching article with ID: " + articleId, e);
        } catch (IllegalStateException e) {
            if (e.getMessage() != null && e.getMessage().startsWith("No instances available for")) {
                throw new RuntimeException("No instances of inventory-service available while fetching article with ID: " + articleId, e);
            }
            throw e;
        }
    }

    public LocationResponseDTO fetchLocation(Long locationId) {
        String url = "http://inventory-service/api/locations/" + locationId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Auth", "true");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<LocationResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    LocationResponseDTO.class
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Inventory service unavailable while fetching location with ID: " + locationId, e);
        } catch (IllegalStateException e) {
            if (e.getMessage() != null && e.getMessage().startsWith("No instances available for")) {
                throw new RuntimeException("No instances of inventory-service available while fetching location with ID: " + locationId, e);
            }
            throw e;
        }
    }
}
