package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ComponentNode;
import org.jetbrains.annotations.Nullable;

public final class ModelModifier implements Modifier<ModelModifier> {
    public final ComponentNode node;
    public @Nullable ModelModifier sibling = null;
    public @Nullable ModelModifier child = null;

    public ModelModifier(ComponentNode node) {
        this.node = node;
    }

    @Override
    public Class<ModelModifier> getType() {
        return ModelModifier.class;
    }

    @Override
    public ModelModifier merge(ModelModifier that) {
        ModelModifier sibling = this;
        while (sibling.sibling != null) sibling = sibling.sibling;
        sibling.sibling = that;
        return this;
    }

    @Override
    public ModelModifier adopt(ModelModifier that) {
        ModelModifier child = this;
        while (child.child != null) child = child.child;
        child.child = that;
        return this;
    }
}
