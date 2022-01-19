package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.SkeletalComponentData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static net.immortaldevs.sar.base.Sar.sanitiseChild;

public class NbtSkeletalComponentData implements SkeletalComponentData {
    protected final Runnable changedCallback;
    protected final NbtCompound nbt;
    private final @Nullable SkeletalComponentData parent;
    private final Component type;

    public NbtSkeletalComponentData(@Nullable SkeletalComponentData parent, Runnable changedCallback, NbtCompound nbt) {
        this.changedCallback = changedCallback;
        this.nbt = nbt;
        this.parent = parent;
        this.type = SarRegistries.COMPONENT.get(Identifier.tryParse(nbt.getString("id")));
    }

    @Override
    public @Nullable SkeletalComponentData parent() {
        return this.parent;
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
        return this.nbt.contains(name);
    }

    @Override
    public boolean containsChild(String name) {
        name = sanitiseChild(name);
        return this.nbt.getCompound(name).contains("id", NbtElement.STRING_TYPE);
    }

    @Override
    public boolean containsChildren(String name) {
        name = sanitiseChild(name);
        return !this.nbt.getList(name, NbtElement.COMPOUND_TYPE).isEmpty();
    }

    @Override
    public @Nullable SkeletalComponentData getChild(String name) {
        name = sanitiseChild(name);
        NbtCompound childNbt = nbt.getCompound(name);
        if (childNbt.contains("id", NbtElement.STRING_TYPE)) {
            return new NbtSkeletalComponentData(this, this.changedCallback, childNbt);
        } else return null;
    }

    @Override
    public SkeletalComponentData createChild(String name, Component type) {
        name = sanitiseChild(name);
        NbtCompound childNbt = new NbtCompound();
        childNbt.putString("id", SarRegistries.COMPONENT.getId(type).toString());
        this.nbt.put(name, childNbt);
        this.changedCallback.run();
        return new NbtSkeletalComponentData(this, this.changedCallback, childNbt);
    }

    @Override
    public void removeChild(String name) {
        name = sanitiseChild(name);
        this.nbt.remove(name);
        this.changedCallback.run();
    }

    @Override
    public Children getChildren(String name0) {
        String name = sanitiseChild(name0);
        NbtList children = this.nbt.getList(name, NbtElement.COMPOUND_TYPE);
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
                NbtSkeletalComponentData.this.nbt.put(name, children);
                children.add(createCompound(type));
                NbtSkeletalComponentData.this.changedCallback.run();
            }

            @Override
            public void add(int i, Component type) {
                NbtSkeletalComponentData.this.nbt.put(name, children);
                children.add(i, createCompound(type));
                NbtSkeletalComponentData.this.changedCallback.run();
            }

            @Override
            public void remove(int i) {
                children.remove(i);
                if (children.isEmpty()) NbtSkeletalComponentData.this.nbt.remove(name);
                NbtSkeletalComponentData.this.changedCallback.run();
            }

            @Override
            public void clear() {
                children.clear();
                NbtSkeletalComponentData.this.nbt.remove(name);
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
}
