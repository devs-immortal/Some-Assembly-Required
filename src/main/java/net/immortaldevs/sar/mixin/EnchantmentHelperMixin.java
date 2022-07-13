package net.immortaldevs.sar.mixin;

import net.immortaldevs.divineintervention.injection.ModifyOperand;
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
    @ModifyOperand(method = "getLevel",
            at = @At("RETURN"))
    private static int modifyLevel(int level, Enchantment enchantment, ItemStack stack) {
        EnchantmentLevelModifier modifier = stack.getModifier(EnchantmentLevelModifier.class);
        return modifier == null ? level : modifier.apply(enchantment, stack, level);
    }
}
