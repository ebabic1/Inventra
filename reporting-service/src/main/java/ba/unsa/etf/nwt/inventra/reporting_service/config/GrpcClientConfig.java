package ba.unsa.etf.nwt.inventra.reporting_service.config;

import ba.unsa.etf.nwt.inventra.reporting_service.service.SystemEventsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${system.events.service.host:localhost}")
    private String host;

    @Value("${system.events.service.port:8081}")
    private int port;

    @Bean
    public SystemEventsClient systemEventsClient() {
        return new SystemEventsClient(host, port);
    }
}