package ba.unsa.etf.nwt.system_events_service.config;

import ba.unsa.etf.nwt.system_events_service.service.SystemEventsService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Value("${grpc.server.port:9090}")
    private int grpcPort;

    @Bean
    public Server grpcServer(SystemEventsService systemEventsService) {
        return ServerBuilder.forPort(grpcPort)
                .addService(systemEventsService)
                .build();
    }
}