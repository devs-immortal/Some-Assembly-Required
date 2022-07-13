package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@FunctionalInterface
public interface HungerModifier extends Modifier<HungerModifier> {
    int apply(ItemStack stack, int hunger);

    @Override
    default Class<HungerModifier> getType() {
        return HungerModifier.class;
    }

    @Override
    default HungerModifier merge(HungerModifier that) {
        return (stack, hunger) -> that.apply(stack, this.apply(stack, hunger));
    }

    static HungerModifier add(int value) {
        return (stack, hunger) -> hunger + value;
    }

    static HungerModifier multiply(int numerator, int denominator) {
        return (stack, hunger) -> (numerator * hunger) / denominator;
    }
}
