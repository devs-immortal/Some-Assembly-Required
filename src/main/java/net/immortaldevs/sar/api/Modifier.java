package net.immortaldevs.sar.api;

public interface Modifier<T extends Modifier<T>> {
    Class<T> getType();

    T merge(T that);

    default T adopt(T that) {
        return this.merge(that);
    }
}
