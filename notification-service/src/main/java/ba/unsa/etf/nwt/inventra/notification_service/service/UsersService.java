package ba.unsa.etf.nwt.inventra.notification_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class UsersService {

    private final RestTemplate restTemplate;

    public UsersService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getAllUserEmails() {
        String gatewayUrl = "http://localhost:8085/auth/api/users/emails";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Service-Name", "NotificationService");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<String>> response = restTemplate.exchange(
                    gatewayUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<String>>() {}
            );

            return response.getBody() != null ? response.getBody() : List.of();

        } catch (Exception e) {
            log.error("Failed to fetch user emails through Gateway API: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
