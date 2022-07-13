package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public interface FoodEffectModifier extends Modifier<FoodEffectModifier> {
    void apply(ItemStack stack, World world, LivingEntity targetEntity);

    @Override
    default Class<FoodEffectModifier> getType() {
        return FoodEffectModifier.class;
    }

    @Override
    default FoodEffectModifier merge(FoodEffectModifier that) {
        return (stack, world, targetEntity) -> {
            this.apply(stack, world, targetEntity);
            that.apply(stack, world, targetEntity);
        };
    }
}
