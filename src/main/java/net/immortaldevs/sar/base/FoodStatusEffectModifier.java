package net.immortaldevs.sar.base;

import com.mojang.datafixers.util.Pair;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

@FunctionalInterface
public interface FoodStatusEffectModifier extends Modifier {
    void applyFoodStatusEffectModifier(ItemStack stack,
                                       World world,
                                       LivingEntity targetEntity,
                                       List<Pair<StatusEffectInstance, Float>> effects);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(FoodStatusEffectModifier.class, this, (a, b) ->
                (stack, world, targetEntity, effects) -> {
                    a.applyFoodStatusEffectModifier(stack, world, targetEntity, effects);
                    b.applyFoodStatusEffectModifier(stack, world, targetEntity, effects);
                });
    }
}
