package net.immortaldevs.sar.mixin;

import net.immortaldevs.sar.api.ComponentNodeHandler;
import net.immortaldevs.sar.impl.SarItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin implements SarItem {
    @Override
    public void loadComponentNodes(@NotNull ItemStack stack, @NotNull ComponentNodeHandler handler) {
    }
}
