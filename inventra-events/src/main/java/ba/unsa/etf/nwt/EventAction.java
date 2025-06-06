package ba.unsa.etf.nwt;

public enum EventAction {
    CREATED("created"),
    UPDATED("updated"),
    DELETED("deleted");

    private final String key;

    EventAction(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
