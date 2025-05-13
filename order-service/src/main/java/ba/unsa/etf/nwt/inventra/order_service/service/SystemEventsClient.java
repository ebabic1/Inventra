package ba.unsa.etf.nwt.inventra.order_service.service;

import ba.unsa.etf.nwt.system_events_service.*;
import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.time.Instant;

public class SystemEventsClient {
    private final SystemEventsServiceGrpc.SystemEventsServiceBlockingStub blockingStub;

    public SystemEventsClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = SystemEventsServiceGrpc.newBlockingStub(channel);
    }

    public String logEvent(String timestampStr, String microserviceName, String username,
                           ActionType actionType, String resourceName, ResponseType responseType) {

        Instant instant = Instant.parse(timestampStr);
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();

        SystemEventRequest request = SystemEventRequest.newBuilder()
                .setTimestamp(timestamp)
                .setMicroserviceName(microserviceName)
                .setUsername(username)
                .setActionType(actionType)
                .setResourceName(resourceName)
                .setResponseType(responseType)
                .build();

        SystemEventResponse response = blockingStub.logEvent(request);
        return response.getStatus() + ": " + response.getMessage();
    }
}