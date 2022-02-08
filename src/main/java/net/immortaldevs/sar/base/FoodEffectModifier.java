package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface FoodEffectModifier extends Modifier {
    void applyFoodEffectModifier(ItemStack stack, World world, LivingEntity targetEntity);

    static FoodEffectModifier of(StatusEffect effect, int duration, int amplifier) {
        return (stack, world, targetEntity) ->
                targetEntity.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier));
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(FoodEffectModifier.class, this, (a, b) -> (stack, world, targetEntity) -> {
            a.applyFoodEffectModifier(stack, world, targetEntity);
            b.applyFoodEffectModifier(stack, world, targetEntity);
        });
    }
}
