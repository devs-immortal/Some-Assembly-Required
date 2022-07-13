package net.immortaldevs.sar.mixin;

import com.mojang.datafixers.util.Pair;
import net.immortaldevs.divineintervention.injection.ModifyOperand;
import net.immortaldevs.sar.base.FoodEffectModifier;
import net.immortaldevs.sar.base.FoodStatusEffectModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyOperand(method = "applyFoodEffects",
            at = @At(value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/item/FoodComponent;getStatusEffects()Ljava/util/List;"))
    private static List<Pair<StatusEffectInstance, Float>> applyFoodEffects(
            List<Pair<StatusEffectInstance, Float>> list,
            ItemStack stack,
            World world,
            LivingEntity targetEntity
    ) {
        var foodEffectModifier = stack.getModifier(FoodEffectModifier.class);
        if (foodEffectModifier != null) {
            foodEffectModifier.apply(stack, world, targetEntity);
        }

        var foodStatusEffectModifier = stack.getModifier(FoodStatusEffectModifier.class);
        if (foodStatusEffectModifier != null) {
            List<Pair<StatusEffectInstance, Float>> effects = new ArrayList<>(list);
            foodStatusEffectModifier.apply(stack, world, targetEntity, effects);
            return effects;
        }

        return list;
    }
}
