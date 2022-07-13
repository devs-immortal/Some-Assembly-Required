package net.immortaldevs.sar.mixin;

import net.immortaldevs.divineintervention.injection.ModifyOperand;
import net.immortaldevs.sar.base.HungerModifier;
import net.immortaldevs.sar.base.SaturationModifierModifier;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @ModifyOperand(method = "eat",
            at = @At(value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/item/FoodComponent;getHunger()I"))
    private int modifyHunger(int hunger, Item item, ItemStack stack) {
        HungerModifier modifier = stack.getModifier(HungerModifier.class);
        if (modifier == null) return hunger;
        return modifier.apply(stack, hunger);
    }

    @ModifyOperand(method = "eat",
            at = @At(value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/item/FoodComponent;getSaturationModifier()F"))
    private float modifySaturation(float saturation, Item item, ItemStack stack) {
        SaturationModifierModifier modifier = stack.getModifier(SaturationModifierModifier.class);
        if (modifier == null) return saturation;
        return modifier.apply(stack, saturation);
    }
}
