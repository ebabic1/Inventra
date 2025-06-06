package ba.unsa.etf.nwt.system_events_service.config;

import io.grpc.Server;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GrpcServerRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(GrpcServerRunner.class);
    private final Server grpcServer;

    public GrpcServerRunner(Server grpcServer) {
        this.grpcServer = grpcServer;
    }

    @Override
    public void run(String... args) throws Exception {
        grpcServer.start();
        logger.info("gRPC Server started, listening on port {}", grpcServer.getPort());

        new Thread(() -> {
            try {
                grpcServer.awaitTermination();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("gRPC server thread interrupted", e);
            }
        }, "grpc-await-thread").start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down gRPC server");
            grpcServer.shutdown();
        }));
    }

    @PreDestroy
    public void stop() {
        if (grpcServer != null && !grpcServer.isShutdown()) {
            grpcServer.shutdown();
        }
    }
}