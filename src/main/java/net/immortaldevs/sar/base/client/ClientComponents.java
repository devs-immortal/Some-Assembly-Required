package net.immortaldevs.sar.base.client;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.*;
import net.immortaldevs.sar.api.SarRegistries;

import java.util.WeakHashMap;

@Environment(EnvType.CLIENT)
public final class ClientComponents {
    private static final WeakHashMap<RootComponentData, FixedModifierMap> INSTANCES = new WeakHashMap<>();
    private static final Reference2ReferenceMap<Component, ClientComponent> CLIENT_COMPONENTS
            = new Reference2ReferenceOpenHashMap<>(SarRegistries.COMPONENT.size());

    public static FixedModifierMap getModifiers(RootComponentData data) {
        return INSTANCES.computeIfAbsent(data, root -> root.fork(larval -> {
            ClientComponent clientComponent = CLIENT_COMPONENTS.get(larval.type());
            if (clientComponent != null) clientComponent.init(larval);
        }));
    }

    public static void register(Component component, ClientComponent clientComponent) {
        CLIENT_COMPONENTS.put(component, clientComponent);
    }
}
