package ba.unsa.etf.nwt.system_events_service.service;

import ba.unsa.etf.nwt.system_events_service.*;
import ba.unsa.etf.nwt.system_events_service.model.SystemEvent;
import ba.unsa.etf.nwt.system_events_service.repository.SystemEventsRepository;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SystemEventsService extends SystemEventsServiceGrpc.SystemEventsServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(SystemEventsService.class);

    private final SystemEventsRepository systemEventsRepository;

    public SystemEventsService(SystemEventsRepository systemEventsRepository) {
        this.systemEventsRepository = systemEventsRepository;
    }

    @Override
    public void logEvent(SystemEventRequest request, StreamObserver<SystemEventResponse> responseObserver) {
        logger.info("[EVENT] {} | {} | {} | {} | {} | {}",
                request.getTimestamp(),
                request.getMicroserviceName(),
                request.getUsername(),
                request.getActionType(),
                request.getResourceName(),
                request.getResponseType()
        );

        SystemEvent event = SystemEvent.builder()
                .timestamp(toInstant(request.getTimestamp()))
                .microserviceName(request.getMicroserviceName())
                .username(request.getUsername())
                .actionType(ActionType.valueOf(request.getActionType().name()))
                .resourceName(request.getResourceName())
                .responseType(ResponseType.valueOf(request.getResponseType().name()))
                .build();

        systemEventsRepository.save(event);

        SystemEventResponse response = SystemEventResponse.newBuilder()
                .setStatus("OK")
                .setMessage("Event successfully logged")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Instant toInstant(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
