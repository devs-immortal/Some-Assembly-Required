package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@FunctionalInterface
public interface EnchantmentLevelModifier extends Modifier<EnchantmentLevelModifier> {
    int apply(Enchantment enchantment, ItemStack stack, int level);

    @Override
    default Class<EnchantmentLevelModifier> getType() {
        return EnchantmentLevelModifier.class;
    }

    @Override
    default EnchantmentLevelModifier merge(EnchantmentLevelModifier that) {
        return (enchantment, stack, level) ->
                that.apply(enchantment, stack, this.apply(enchantment, stack, level));
    }

    static EnchantmentLevelModifier of(Enchantment enchantment, int level) {
        return (enchantment0, stack, level0) -> enchantment == enchantment0 ? level0 + level : level0;
    }
}
