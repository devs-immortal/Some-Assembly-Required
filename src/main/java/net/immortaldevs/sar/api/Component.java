package net.immortaldevs.sar.api;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

/**
 * A component is attached to a {@link ComponentHost}, and is responsible for adding modifiers and loading child
 * components.
 */
public class Component {
    /**
     * A default component that handles unknown ids
     */
    public static final Component AIR = new Component();

    private final RegistryEntry.Reference<Component> registryEntry = SarRegistries.COMPONENT.createEntry(this);

    /**
     * Called when initialising components for a host. Add modifiers and load children here.
     * @param data the data for this component
     */
    public void init(LarvalComponentData data) {
    }

    public final RegistryEntry.Reference<Component> getRegistryEntry() {
        return this.registryEntry;
    }

    public final Identifier getId() {
        return SarRegistries.COMPONENT.getId(this);
    }

    @Override
    public String toString() {
        return this.getId().toString();
    }

    public static Component fromId(@Nullable Identifier id) {
        return SarRegistries.COMPONENT.get(id);
    }

    public static Component fromId(@Nullable String id) {
        return fromId(Identifier.tryParse(id));
    }
}
