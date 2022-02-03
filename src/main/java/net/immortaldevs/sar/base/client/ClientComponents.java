package net.immortaldevs.sar.base.client;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.immortaldevs.sar.api.*;
import net.immortaldevs.sar.base.SarRegistries;

import java.util.WeakHashMap;
import java.util.function.Consumer;

public final class ClientComponents {
    private static final WeakHashMap<RootComponentData, FixedModifierMap> INSTANCES = new WeakHashMap<>();
    private static final Reference2ReferenceMap<Component, Consumer<LarvalComponentData>> INITIALISERS
            = new Reference2ReferenceOpenHashMap<>(SarRegistries.COMPONENT.size());

    public static FixedModifierMap getModifiers(RootComponentData data) {
        return INSTANCES.computeIfAbsent(data, root -> root.fork(larval -> {
            var initialiser = INITIALISERS.get(larval.type());
            if (initialiser != null) initialiser.accept(larval);
        }));
    }

    public static void register(Component component, Consumer<LarvalComponentData> initialiser) {
        INITIALISERS.put(component, initialiser);
    }
}
