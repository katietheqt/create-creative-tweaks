package cat.katie.createcreativetweaks.features.contraption_order.state;

public enum DisplayState {
    FRONTIER,
    ACTORS_STORAGES,
    DISABLED;

    private static final DisplayState[] VALUES = DisplayState.values();

    public DisplayState next() {
        if (this == DISABLED) {
            throw new IllegalStateException("cannot advance past DISABLED state");
        }

        return VALUES[ordinal() + 1];
    }

    public boolean showFrontierOrdering() {
        return this == FRONTIER;
    }

    public boolean showStorageOrder() {
        return true;
    }

    public boolean showActorOrder() {
        return true;
    }
}
