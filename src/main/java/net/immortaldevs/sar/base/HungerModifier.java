package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface HungerModifier extends Modifier {
    void apply(ItemStack stack, Values values);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(HungerModifier.class, this, (a, b) -> (stack, values) -> {
            a.apply(stack, values);
            b.apply(stack, values);
        });
    }

    final class Values {
        public int food;
        public float saturationModifier;

        public Values(int food, float saturationModifier) {
            this.food = food;
            this.saturationModifier = saturationModifier;
        }
    }
}
