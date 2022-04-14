package net.immortaldevs.sar.mixin;

import net.immortaldevs.divineintervention.injection.ModifyOperand;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @ModifyOperand(method = "useOnBlock",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/BlockItem;isFood()Z"))
    private boolean useOnBlock(boolean result, ItemUsageContext context) {
        return context.getStack().isFood();
    }
}
