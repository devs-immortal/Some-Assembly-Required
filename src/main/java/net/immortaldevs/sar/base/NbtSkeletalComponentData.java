package net.immortaldevs.sar.base;

import com.google.common.base.MoreObjects;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.SarRegistries;
import net.immortaldevs.sar.api.SkeletalComponentData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NbtSkeletalComponentData implements SkeletalComponentData {
    protected final Runnable changedCallback;
    protected final NbtCompound nbt;
    private final @Nullable SkeletalComponentData parent;
    private final Component type;

    public NbtSkeletalComponentData(
            @Nullable SkeletalComponentData parent,
            Runnable changedCallback,
            NbtCompound nbt
    ) {
        this(parent, changedCallback, nbt,
                SarRegistries.COMPONENT.get(Identifier.tryParse(nbt.getString("id"))));
    }

    public NbtSkeletalComponentData(
            @Nullable SkeletalComponentData parent,
            Runnable changedCallback,
            NbtCompound nbt,
            Component type
    ) {
        this.changedCallback = changedCallback;
        this.nbt = nbt;
        this.parent = parent;
        this.type = type;
    }

    @Override
    public Optional<SkeletalComponentData> parent() {
        return Optional.ofNullable(this.parent);
    }

    @Override
    public Component type() {
        return this.type;
    }

    @Override
    public NbtCompound getNbt() {
        return this.nbt.getCompound("tag");
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
        NbtCompound data = this.getNbt();
        if (data == null) return;
        data.remove(key);
        if (data.isEmpty()) this.nbt.remove("tag");
    }

    @Override
    public void changeType(Component type) {
        this.nbt.putString("id", SarRegistries.COMPONENT.getId(type).toString());
        this.changedCallback.run();
    }

    @Override
    public boolean contains(String name) {
        return this.nbt.getCompound("components")
                .contains(name);
    }

    @Override
    public boolean containsChild(String name) {
        return this.nbt.getCompound("components")
                .getCompound(name)
                .contains("id", NbtElement.STRING_TYPE);
    }

    @Override
    public boolean containsChildren(String name) {
        return !this.nbt.getCompound("components")
                .getList(name, NbtElement.COMPOUND_TYPE)
                .isEmpty();
    }

    @Override
    public Optional<SkeletalComponentData> getChild(String name) {
        NbtCompound childNbt = this.nbt.getCompound("components").getCompound(name);
        return childNbt.contains("id", NbtElement.STRING_TYPE)
                ? Optional.of(new NbtSkeletalComponentData(this, this.changedCallback, childNbt))
                : Optional.empty();
    }

    @Override
    public SkeletalComponentData createChild(String name, Component type) {
        NbtCompound components;
        if (!this.nbt.contains("components", NbtElement.COMPOUND_TYPE)) {
            this.nbt.put("components", components = new NbtCompound());
        } else components = this.nbt.getCompound("components");

        NbtCompound childNbt = new NbtCompound();
        childNbt.putString("id", SarRegistries.COMPONENT.getId(type).toString());

        components.put(name, childNbt);
        this.changedCallback.run();
        return new NbtSkeletalComponentData(this, this.changedCallback, childNbt);
    }

    @Override
    public void removeChild(String name) {
        if (this.nbt.contains("components")) {
            NbtCompound components = this.nbt.getCompound("components");
            components.remove(name);
            if (components.isEmpty()) this.nbt.remove("components");
            this.changedCallback.run();
        }
    }

    @Override
    public Children getChildren(String name) {
        NbtCompound components = this.nbt.getCompound("components");
        NbtList children = this.nbt.getCompound("components").getList(name, NbtElement.COMPOUND_TYPE);
        return new Children() {
            @Override
            public int size() {
                return children.size();
            }

            @Override
            public boolean isEmpty() {
                return children.isEmpty();
            }

            @Override
            public void add(Component type) {
                components.put(name, children);
                children.add(createCompound(type));
                NbtSkeletalComponentData.this.changedCallback.run();
            }

            @Override
            public void add(int i, Component type) {
                components.put(name, children);
                children.add(i, createCompound(type));
                NbtSkeletalComponentData.this.changedCallback.run();
            }

            @Override
            public void remove(int i) {
                children.remove(i);
                if (children.isEmpty()) components.remove(name);
                NbtSkeletalComponentData.this.changedCallback.run();
            }

            @Override
            public void clear() {
                children.clear();
                components.remove(name);
                NbtSkeletalComponentData.this.changedCallback.run();
            }

            @Override
            public SkeletalComponentData get(int i) {
                return new NbtSkeletalComponentData(NbtSkeletalComponentData.this,
                        NbtSkeletalComponentData.this.changedCallback,
                        children.getCompound(i));
            }

            private static NbtCompound createCompound(Component type) {
                NbtCompound ret = new NbtCompound();
                ret.putString("id", SarRegistries.COMPONENT.getId(type).toString());
                return ret;
            }
        };
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .toString();
    }
}
