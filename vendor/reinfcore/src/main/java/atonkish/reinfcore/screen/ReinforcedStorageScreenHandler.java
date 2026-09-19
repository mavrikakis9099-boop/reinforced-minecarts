package atonkish.reinfcore.screen;

/**
 * Marker used by inventory-sorting clients that identify modded storage screens by class name.
 *
 * <p>The original Reinforced Storage menu used this exact binary name.  Keeping the marker
 * name lets current clients recognise the 26.2 menu without changing menu ids, slot ordering,
 * click handling, or serialized storage data.</p>
 */
public interface ReinforcedStorageScreenHandler {
}
