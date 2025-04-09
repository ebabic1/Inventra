package ba.unsa.etf.nwt.inventra.reporting_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class LoadBalancerTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void testGetOrderById() {
        String url = "http://order-service/api/orders/1";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("Response: " + response.getBody());
    }

    @Test
    void testGetInstancePort() {
        String url = "http://order-service/api/orders/instance-port";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println(">>> " + response.getBody());
    }
}
