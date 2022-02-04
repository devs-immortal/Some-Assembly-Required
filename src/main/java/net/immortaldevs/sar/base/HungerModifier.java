package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface HungerModifier extends Modifier {
    int apply(ItemStack stack, int hunger);

    static HungerModifier multiply(int numerator, int denominator) {
        return (stack, hunger) -> (hunger * numerator) / denominator;
    }

    static HungerModifier add(int value) {
        return (stack, hunger) -> hunger + value;
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(HungerModifier.class, this, (a, b) -> (stack, hunger) ->
                b.apply(stack, a.apply(stack, hunger)));
    }
}
