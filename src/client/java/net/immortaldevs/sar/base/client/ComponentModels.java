package net.immortaldevs.sar.base.client;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Component;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class ComponentModels {
    private static final Reference2ReferenceMap<Component, ComponentModel> REGISTRY
            = new Reference2ReferenceOpenHashMap<>();

    public static void register(Component component, ComponentModel componentModel) {
        if (REGISTRY.put(component, componentModel) == null) return;
        throw new IllegalStateException("Model for component " + component + " has already been registered");
    }

    public static @Nullable ComponentModel get(Component component) {
        return REGISTRY.get(component);
    }
}
