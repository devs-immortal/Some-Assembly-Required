package net.immortaldevs.sar.base;

import com.google.common.base.MoreObjects;
import net.immortaldevs.sar.api.*;
import net.immortaldevs.sar.impl.NbtComponentNodeCollection;
import net.immortaldevs.sar.impl.Util;
import net.immortaldevs.sar.mixin.NbtCompoundAccessor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class NbtComponentNode extends ComponentNode {
    private final NbtCompound nbt;
    private final ComponentNodeHost host;
    private final Component component;

    public NbtComponentNode(NbtCompound nbt,
                            ComponentNodeHost host) {
        this.nbt = nbt;
        this.host = host;
        this.component = SarRegistries.COMPONENT.get(Identifier.tryParse(nbt.getString("id")));
    }

    @Override
    public Component getComponent() {
        return this.component;
    }

    @Override
    public void setComponent(Component component) {
        this.nbt.putString("id", component.getId().toString());
    }

    @Override
    public @Nullable NbtCompound getNbt() {
        return this.hasNbt() ? this.nbt.getCompound("tag") : null;
    }

    @Override
    public void setNbt(NbtCompound nbt) {
        this.nbt.put("tag", nbt);
    }

    @Override
    public boolean hasNbt() {
        return this.nbt.contains("tag", NbtElement.COMPOUND_TYPE);
    }

    @Override
    public void removeSubNbt(String key) {
        NbtCompound nbt = this.getNbt();
        if (nbt == null) return;
        nbt.remove(key);
        if (nbt.isEmpty()) this.nbt.remove("tag");
    }

    @Override
    public void setSubNbt(String key, NbtElement element) {
        this.getOrCreateNbt().put(key, element);
    }

    @Override
    public @Nullable NbtCompound getSubNbt(String key) {
        NbtCompound nbt = this.getNbt();
        return nbt != null && nbt.contains(key, NbtElement.COMPOUND_TYPE)
                ? nbt.getCompound(key)
                : null;
    }

    @Override
    public NbtCompound getOrCreateNbt() {
        NbtCompound nbt = this.getNbt();
        if (nbt == null) this.setNbt(nbt = new NbtCompound());
        return nbt;
    }

    @Override
    public NbtCompound getOrCreateSubNbt(String key) {
        NbtCompound nbt = this.getOrCreateNbt();
        if (!nbt.contains(key, NbtElement.COMPOUND_TYPE)) {
            nbt.put(key, new NbtCompound());
        }

        return nbt.getCompound(key);
    }

    @Override
    public void updateComponents() {
        if (this.nbt.contains("components", NbtElement.COMPOUND_TYPE)) {
            NbtCompound components = this.nbt.getCompound("components");
            Util.updateComponents(((NbtCompoundAccessor) components).getEntries().values(), true);
            if (components.isEmpty()) this.nbt.remove("components");
        }

        this.host.updateComponents();
    }

    @Override
    public void loadComponentNodes(ComponentNodeHandler handler) {
        this.component.loadComponentNodes(this, handler);

        if (!this.nbt.contains("components", NbtElement.COMPOUND_TYPE)) return;
        NbtCompound components = this.nbt.getCompound("components");
        for (NbtElement element : ((NbtCompoundAccessor) components).getEntries().values()) {
            if (element instanceof NbtCompound nbt) {
                handler.load(new NbtComponentNode(nbt, this));
            } else if (element instanceof NbtList list
                    && list.getHeldType() == NbtElement.COMPOUND_TYPE) {
                for (int i = 0, size = list.size(); i < size; i++) {
                    handler.load(new NbtComponentNode(list.getCompound(i), this));
                }
            }
        }
    }

    @Override
    public boolean containsComponentNode(Identifier id) {
        return this.nbt.getCompound("components")
                .getCompound(id.toString())
                .contains("id", NbtElement.STRING_TYPE);
    }

    @Override
    public boolean containsComponentNodes(Identifier id) {
        return !this.nbt.getCompound("components")
                .getList(id.toString(), NbtElement.COMPOUND_TYPE)
                .isEmpty();
    }

    @Override
    public @Nullable ComponentNode getComponentNode(Identifier id) {
        NbtCompound childNbt = this.nbt.getCompound("components").getCompound(id.toString());
        return childNbt.contains("id", NbtElement.STRING_TYPE)
                ? new NbtComponentNode(childNbt, this)
                : null;
    }

    @Override
    public ComponentNode createComponentNode(Identifier id, Component component) {
        NbtCompound components;
        if (!this.nbt.contains("components", NbtElement.COMPOUND_TYPE)) {
            this.nbt.put("components", components = new NbtCompound());
        } else components = this.nbt.getCompound("components");

        NbtCompound childNbt = new NbtCompound();
        childNbt.putString("id", component.getId().toString());

        components.put(id.toString(), childNbt);
        return new NbtComponentNode(childNbt, this);
    }

    @Override
    public void removeComponentNode(Identifier id) {
        if (!this.nbt.contains("components")) return;
        NbtCompound components = this.nbt.getCompound("components");
        components.remove(id.toString());
        if (components.isEmpty()) this.nbt.remove("components");
    }

    @Override
    public ComponentNodeCollection getComponentNodes(Identifier id) {
        String key = id.toString();
        return new NbtComponentNodeCollection() {
            @Override
            protected NbtList read() {
                return NbtComponentNode.this.nbt
                        .getCompound("components")
                        .getList(key, NbtElement.COMPOUND_TYPE);
            }

            @Override
            protected void write(NbtList list) {
                if (list.isEmpty()) this.clear();
                else {
                    NbtCompound components = NbtComponentNode.this.nbt.getCompound("components");
                    components.put(key, list);
                    NbtComponentNode.this.nbt.put("components", components);
                }
            }

            @Override
            public void clear() {
                NbtComponentNode.this.removeComponentNode(id);
            }

            @Override
            public ComponentNode get(int i) {
                return new NbtComponentNode(this.read().getCompound(i), NbtComponentNode.this);
            }
        };
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("component", this.component)
                .toString();
    }
}
