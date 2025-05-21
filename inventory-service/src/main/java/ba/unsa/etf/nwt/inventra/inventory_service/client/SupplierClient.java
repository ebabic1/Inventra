package ba.unsa.etf.nwt.inventra.inventory_service.client;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.SupplierDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SupplierClient {

    private final RestTemplate restTemplate;

    public SupplierClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean checkSupplierExists(Long supplierId) {
        String url = "http://order-service/api/suppliers/" + supplierId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Auth", "true");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<SupplierDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    SupplierDTO.class
            );

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
