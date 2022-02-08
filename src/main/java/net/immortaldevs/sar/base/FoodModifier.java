package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@FunctionalInterface
public interface FoodModifier extends Modifier {
    int applyFoodModifier(ItemStack stack, int food);

    static FoodModifier add(int value) {
        return (stack, food) -> food + value;
    }

    static FoodModifier multiply(int numerator, int denominator) {
        return (stack, food) -> (numerator * food) / denominator;
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(FoodModifier.class, this, (a, b) -> (stack, food) ->
                b.applyFoodModifier(stack, a.applyFoodModifier(stack, food)));
    }
}
