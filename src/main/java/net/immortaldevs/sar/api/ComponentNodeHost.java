package net.immortaldevs.sar.api;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface ComponentNodeHost {
    void updateComponents();

    void loadComponentNodes(ComponentNodeHandler handler);

    boolean containsComponentNode(Identifier id);

    boolean containsComponentNodes(Identifier id);

    @Nullable ComponentNode getComponentNode(Identifier id);

    ComponentNode createComponentNode(Identifier id, Component component);

    default ComponentNode getOrCreateComponentNode(Identifier id, Component component) {
        ComponentNode node = this.getComponentNode(id);
        return node == null ? this.createComponentNode(id, component) : node;
    }

    void removeComponentNode(Identifier id);

    ComponentNodeCollection getComponentNodes(Identifier id);
}
