package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

@FunctionalInterface
public interface UseOnBlockModifier extends Modifier {
    ActionResult apply(ItemUsageContext context);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(UseOnBlockModifier.class, this, (a, b) -> context -> {
            ActionResult result = a.apply(context);
            if (result != ActionResult.PASS) return result;
            return b.apply(context);
        });
    }
}
