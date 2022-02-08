package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@FunctionalInterface
public interface SaturationModifierModifier extends Modifier {
    float applySaturationModifierModifier(ItemStack stack, float saturationModifier);

    static SaturationModifierModifier add(float value) {
        return (stack, saturationModifier) -> saturationModifier + value;
    }

    static SaturationModifierModifier multiply(float value) {
        return (stack, saturationModifier) -> saturationModifier * value;
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(SaturationModifierModifier.class, this, (a, b) -> (stack, saturationModifier) ->
                b.applySaturationModifierModifier(stack,
                        a.applySaturationModifierModifier(stack, saturationModifier)));
    }
}
