package net.immortaldevs.sar.api;

import java.util.function.Consumer;

public interface RootComponentData extends ComponentData {
    FixedModifierMap fork(Consumer<LarvalComponentData> initialiser);
}
