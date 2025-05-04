package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.system_events_service.SystemEventRequest;
import ba.unsa.etf.nwt.system_events_service.SystemEventResponse;
import ba.unsa.etf.nwt.system_events_service.SystemEventsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SystemEventsClient {
    private final SystemEventsServiceGrpc.SystemEventsServiceBlockingStub blockingStub;

    public SystemEventsClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = SystemEventsServiceGrpc.newBlockingStub(channel);
    }

    public String logEvent(String timestamp, String microserviceName, String username,
                           String actionType, String resourceName, String responseType) {
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