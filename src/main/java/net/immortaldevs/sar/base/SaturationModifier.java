package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface SaturationModifier extends Modifier {
    float apply(ItemStack stack, float saturation);

    static SaturationModifier multiply(float value) {
        return (stack, saturation) -> saturation * value;
    }

    static SaturationModifier add(float value) {
        return (stack, saturation) -> saturation + value;
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(SaturationModifier.class, this, (a, b) -> (stack, saturation) ->
                b.apply(stack, a.apply(stack, saturation)));
    }
}
