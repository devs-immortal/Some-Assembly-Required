package net.immortaldevs.sar.api;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

/**
 * A component is attached to a {@link ComponentNodeHost}, and is responsible for adding modifiers and loading child
 * components.
 */
@SuppressWarnings("unused")
public class Component {
    /**
     * A default component that handles unknown ids
     */
    public static final Component AIR = new Component();

    private final RegistryEntry.Reference<Component> registryEntry = SarRegistries.COMPONENT.createEntry(this);

    public void loadComponentNodes(ComponentNode node, ComponentNodeHandler handler) {
    }

    public void addModifiers(ComponentNode node, ModifierHandler handler) {
    }

    public final RegistryEntry.Reference<Component> getRegistryEntry() {
        return this.registryEntry;
    }

    public final Identifier getId() {
        return this.registryEntry.registryKey().getValue();
    }

    @Override
    public String toString() {
        return this.getId().toString();
    }
}
