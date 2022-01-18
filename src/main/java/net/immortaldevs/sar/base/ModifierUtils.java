package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.api.Modifier;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ModifierUtils {
    public static <T extends Modifier> @Nullable T getModifier(ItemStack stack, Class<T> type) {
        RootComponentData rootComponentData = ((ItemStackExt) (Object) stack).sar$getComponentRoot();
        if (rootComponentData == null) return null;
        return rootComponentData.modifierMap.get(type);
    }

    public static <T extends Modifier> void acceptModifier(ItemStack stack, Class<T> type, Consumer<T> action) {
        T modifier = getModifier(stack, type);
        if (modifier == null) return;
        action.accept(modifier);
    }

    public static <T extends Modifier, U> @Nullable U applyModifier(ItemStack stack, Class<T> type, Function<T, U> action) {
        T modifier = getModifier(stack, type);
        if (modifier == null) return null;
        return action.apply(modifier);
    }
}
