package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;

public final class EdibleModifier implements Modifier {
    public static final EdibleModifier INSTANCE = new EdibleModifier();

    private EdibleModifier() {
    }

    @Override
    public void register(ModifierMap modifierMap) {
        modifierMap.put(EdibleModifier.class, this);
    }
}
