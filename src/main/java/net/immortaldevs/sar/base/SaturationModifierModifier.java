package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@FunctionalInterface
public interface SaturationModifierModifier extends Modifier<SaturationModifierModifier> {
    float apply(ItemStack stack, float saturationModifier);

    @Override
    default Class<SaturationModifierModifier> getType() {
        return SaturationModifierModifier.class;
    }

    @Override
    default SaturationModifierModifier merge(SaturationModifierModifier that) {
        return (stack, saturationModifier) -> that.apply(stack, this.apply(stack, saturationModifier));
    }

    static SaturationModifierModifier add(float value) {
        return (stack, saturationModifier) -> saturationModifier + value;
    }

    static SaturationModifierModifier multiply(float value) {
        return (stack, saturationModifier) -> saturationModifier * value;
    }
}
