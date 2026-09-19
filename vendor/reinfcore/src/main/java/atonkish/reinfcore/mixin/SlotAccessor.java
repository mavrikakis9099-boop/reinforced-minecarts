package atonkish.reinfcore.mixin;

import net.minecraft.world.inventory.Slot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Allows the Scroll presentation to reposition existing slots without changing slot order. */
@Mixin(Slot.class)
public interface SlotAccessor {
    @Mutable
    @Accessor("x")
    void reinfcore$setX(int x);

    @Mutable
    @Accessor("y")
    void reinfcore$setY(int y);
}
