package net.immortaldevs.sar.mixin;

import net.immortaldevs.sar.base.EnchantmentLevelModifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(method = "getLevel",
            at = @At("RETURN"),
            cancellable = true)
    private static void getLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        EnchantmentLevelModifier modifier = stack.getModifiers().get(EnchantmentLevelModifier.class);
        if (modifier == null) return;
        cir.setReturnValue(modifier.apply(enchantment, stack, cir.getReturnValueI()));
    }
}
