package net.immortaldevs.sar.base;

import com.mojang.datafixers.util.Pair;
import net.immortaldevs.sar.api.Modifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

@FunctionalInterface
public interface FoodStatusEffectModifier extends Modifier<FoodStatusEffectModifier> {
    void apply(ItemStack stack,
               World world,
               LivingEntity targetEntity,
               List<Pair<StatusEffectInstance, Float>> effects);

    @Override
    default Class<FoodStatusEffectModifier> getType() {
        return FoodStatusEffectModifier.class;
    }

    @Override
    default FoodStatusEffectModifier merge(FoodStatusEffectModifier that) {
        return (stack, world, targetEntity, effects) -> {
            this.apply(stack, world, targetEntity, effects);
            that.apply(stack, world, targetEntity, effects);
        };
    }

    static FoodStatusEffectModifier of(StatusEffect effect, int duration, int amplifier, float chance) {
        return of(new StatusEffectInstance(effect, duration, amplifier), chance);
    }

    static FoodStatusEffectModifier of(StatusEffectInstance effect, float chance) {
        return of(Pair.of(effect, chance));
    }

    static FoodStatusEffectModifier of(Pair<StatusEffectInstance, Float> effect) {
        return (stack, world, targetEntity, effects) -> effects.add(effect);
    }
}
