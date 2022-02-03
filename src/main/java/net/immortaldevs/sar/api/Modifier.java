package net.immortaldevs.sar.api;

import org.jetbrains.annotations.Contract;

public interface Modifier {
    @Contract(mutates = "param")
    void register(ModifierMap modifierMap);
}
