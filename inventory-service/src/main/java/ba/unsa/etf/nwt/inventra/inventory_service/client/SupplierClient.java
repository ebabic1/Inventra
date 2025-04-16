package ba.unsa.etf.nwt.inventra.inventory_service.client;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.SupplierDTO;
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
        try {
            restTemplate.getForEntity(url, SupplierDTO.class);
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
