package net.immortaldevs.sar.mixin;

import net.immortaldevs.divineintervention.injection.ModifyOperand;
import net.immortaldevs.sar.base.FoodModifier;
import net.immortaldevs.sar.base.SaturationModifierModifier;
import net.immortaldevs.sar.base.Util;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @ModifyOperand(method = "eat",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/FoodComponent;getHunger()I"))
    private int modifyFood(int food, Item item, ItemStack stack) {
        FoodModifier modifier = Util.getModifier(stack, FoodModifier.class);
        if (modifier == null) return food;
        return modifier.applyFoodModifier(stack, food);
    }

    @ModifyOperand(method = "eat",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/FoodComponent;getSaturationModifier()F"))
    private float modifySaturation(float saturation, Item item, ItemStack stack) {
        SaturationModifierModifier modifier = Util.getModifier(stack, SaturationModifierModifier.class);
        if (modifier == null) return saturation;
        return modifier.applySaturationModifierModifier(stack, saturation);
    }
}
