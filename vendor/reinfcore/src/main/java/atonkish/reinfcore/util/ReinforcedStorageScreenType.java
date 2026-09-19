package atonkish.reinfcore.util;

/** Exact behavioral reconstruction of the 1.21.11 core enum. */
public enum ReinforcedStorageScreenType {
    SINGLE("Single"),
    SCROLL("Scroll");

    private final String type;

    ReinforcedStorageScreenType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
