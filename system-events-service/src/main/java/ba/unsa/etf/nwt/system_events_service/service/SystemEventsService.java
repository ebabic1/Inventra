package ba.unsa.etf.nwt.system_events_service.service;

import ba.unsa.etf.nwt.system_events_service.SystemEventRequest;
import ba.unsa.etf.nwt.system_events_service.SystemEventResponse;
import ba.unsa.etf.nwt.system_events_service.SystemEventsServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SystemEventsService extends SystemEventsServiceGrpc.SystemEventsServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(SystemEventsService.class);

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

        SystemEventResponse response = SystemEventResponse.newBuilder()
                .setStatus("OK")
                .setMessage("Event successfully logged")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
