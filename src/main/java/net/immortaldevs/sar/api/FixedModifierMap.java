package net.immortaldevs.sar.api;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface FixedModifierMap {
    FixedModifierMap EMPTY = new FixedModifierMap() {
        @Override
        public <T extends Modifier> @Nullable T get(Class<T> key) {
            return null;
        }

        @Override
        public <T extends Modifier> T getOrDefault(Class<T> key, T or) {
            return or;
        }

        @Override
        public boolean contains(Class<? extends Modifier> key) {
            return false;
        }
    };

    <T extends Modifier> @Nullable T get(Class<T> key);

    <T extends Modifier> T getOrDefault(Class<T> key, T or);

    boolean contains(Class<? extends Modifier> key);
}
