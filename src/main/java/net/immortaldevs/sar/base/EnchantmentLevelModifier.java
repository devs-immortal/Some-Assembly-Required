package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface EnchantmentLevelModifier extends Modifier {
    int applyEnchantmentLevelModifier(Enchantment enchantment, ItemStack stack, int level);

    static EnchantmentLevelModifier of(Enchantment enchantment, int level) {
        return (enchantment1, stack, level1) -> enchantment == enchantment1 ? level1 + level : level1;
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(EnchantmentLevelModifier.class, this, (a, b) -> (enchantment, stack, level) ->
                b.applyEnchantmentLevelModifier(enchantment, stack,
                        a.applyEnchantmentLevelModifier(enchantment, stack, level)));
    }
}
