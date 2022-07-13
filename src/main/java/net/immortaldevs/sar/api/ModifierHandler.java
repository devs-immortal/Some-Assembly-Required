package net.immortaldevs.sar.api;

public interface ModifierHandler {
    <T extends Modifier<T>> T add(T modifier);
}
