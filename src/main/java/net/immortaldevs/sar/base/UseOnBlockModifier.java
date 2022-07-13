package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

@FunctionalInterface
public interface UseOnBlockModifier extends Modifier<UseOnBlockModifier> {
    ActionResult apply(ItemUsageContext context);

    @Override
    default Class<UseOnBlockModifier> getType() {
        return UseOnBlockModifier.class;
    }

    @Override
    default UseOnBlockModifier merge(UseOnBlockModifier that) {
        return context -> {
            ActionResult result = this.apply(context);
            if (result != ActionResult.PASS) return result;
            return that.apply(context);
        };
    }
}
