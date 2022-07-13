package net.immortaldevs.sar.api;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("unused")
public interface ModifierHolder {
    <T extends Modifier<T>> T add(T modifier);

    <T extends Modifier<T>, U extends Modifier<U>> void map(Class<T> type, Function<T, @Nullable U> function);

    <T extends Modifier<T>, U extends Modifier<U>> void compute(Class<T> type,
                                                                Function<@Nullable T, @Nullable U> function);

    <T extends Modifier<T>> T fill(T value);

    <T extends Modifier<T>> @Nullable T get(Class<T> type);

    <T extends Modifier<T>> @Nullable T remove(Class<T> type);
}
