package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@FunctionalInterface
public interface HungerModifier extends Modifier {
    int applyHungerModifier(ItemStack stack, int hunger);

    static HungerModifier add(int value) {
        return (stack, hunger) -> hunger + value;
    }

    static HungerModifier multiply(int numerator, int denominator) {
        return (stack, food) -> (numerator * food) / denominator;
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(HungerModifier.class, this, (a, b) -> (stack, hunger) ->
                b.applyHungerModifier(stack, a.applyHungerModifier(stack, hunger)));
    }
}
