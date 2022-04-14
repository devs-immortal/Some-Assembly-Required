package net.immortaldevs.sar.impl;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.base.SarItemStack;
import org.jetbrains.annotations.Nullable;

public final class Util {
    public static <T extends Modifier> @Nullable T getModifier(SarItemStack stack, Class<T> type) {
        return stack.getModifiers().get(type);
    }
}
