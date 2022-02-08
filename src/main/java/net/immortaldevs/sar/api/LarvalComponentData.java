package net.immortaldevs.sar.api;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

@SuppressWarnings("unused")
public interface LarvalComponentData extends ComponentData {
    ModifierMap modifiers();

    void addModifier(Modifier modifier);

    void loadChild(String name, Consumer<LarvalComponentData> transformer);

    void loadChildren(String name, ObjIntConsumer<LarvalComponentData> transformer);

    default void loadChild(String name) {
        this.loadChild(name, data -> {});
    }

    default void loadChildren(String name) {
        this.loadChildren(name, (data, index) -> {});
    }
}
