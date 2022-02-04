package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.RootComponentData;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("unused")
public final class Util {
    public static <T extends Modifier> @Nullable T getModifier(ItemStackExt stack, Class<T> type) {
        Optional<RootComponentData> data = stack.sar$getComponentRoot();
        if (data.isEmpty()) return null;
        return data.get().modifiers().get(type);
    }

    @SafeVarargs
    public static <T> T[] arrayOf(T... values) {
        return values;
    }
}
