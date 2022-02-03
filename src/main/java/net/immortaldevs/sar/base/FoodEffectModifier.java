package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface FoodEffectModifier extends Modifier {
    void apply(ItemStack stack, World world, LivingEntity targetEntity);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(FoodEffectModifier.class, this, (a, b) -> (stack, world, targetEntity) -> {
            a.apply(stack, world, targetEntity);
            b.apply(stack, world, targetEntity);
        });
    }
}
