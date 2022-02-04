package net.immortaldevs.sar.test;

import net.immortaldevs.sar.mixin.ItemAccessor;
import net.minecraft.item.Item;

public class CumSprayItem extends Item {
    public CumSprayItem(Settings settings) {
        super(settings);
        ((ItemAccessor) this).setRecipeRemainder(this);
    }
}
