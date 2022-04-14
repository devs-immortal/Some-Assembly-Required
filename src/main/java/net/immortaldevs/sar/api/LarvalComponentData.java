package net.immortaldevs.sar.api;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface LarvalComponentData extends ComponentData {
    ModifierMap modifiers();

    void addModifier(Modifier modifier);

    void loadChild(String name, Consumer<LarvalComponentData> transformer);

    void loadChildren(String name, Consumer<LarvalComponentData> transformer);

    default void loadChild(String name) {
        this.loadChild(name, data -> {});
    }

    default void loadChildren(String name) {
        this.loadChildren(name, data -> {});
    }
}
