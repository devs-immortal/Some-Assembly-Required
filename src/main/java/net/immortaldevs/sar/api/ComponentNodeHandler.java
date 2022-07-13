package net.immortaldevs.sar.api;

public interface ComponentNodeHandler {
    void load(ComponentNode node);

    void load(ComponentNode node, ModifierTransformer transformer);
}
