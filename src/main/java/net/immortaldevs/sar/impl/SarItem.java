package net.immortaldevs.sar.impl;

import net.immortaldevs.sar.api.ComponentNodeHandler;
import net.minecraft.item.ItemStack;

public interface SarItem {
    default void loadComponentNodes(ItemStack stack, ComponentNodeHandler handler) {
        throw new NoSuchMethodError();
    }
}
