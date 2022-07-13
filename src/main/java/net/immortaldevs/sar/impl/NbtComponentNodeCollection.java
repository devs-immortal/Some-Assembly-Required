package net.immortaldevs.sar.impl;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentNode;
import net.immortaldevs.sar.api.ComponentNodeCollection;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.Iterator;

public abstract class NbtComponentNodeCollection implements ComponentNodeCollection {
    @Override
    public Iterator<ComponentNode> iterator() {
        return new Iterator<>() {
            private final int size = NbtComponentNodeCollection.this.size();
            private int nextIndex = 0;

            @Override
            public boolean hasNext() {
                return this.nextIndex < this.size;
            }

            @Override
            public ComponentNode next() {
                return NbtComponentNodeCollection.this.get(this.nextIndex++);
            }

            @Override
            public void remove() {
                NbtComponentNodeCollection.this.remove(--this.nextIndex);
            }
        };
    }

    @Override
    public int size() {
        return this.read().size();
    }

    @Override
    public boolean isEmpty() {
        return this.read().isEmpty();
    }

    @Override
    public void add(Component component) {
        NbtCompound child = new NbtCompound();
        child.putString("id", component.getId().toString());

        NbtList children = this.read();
        children.add(child);
        this.write(children);
    }

    @Override
    public void add(int i, Component component) {
        NbtCompound child = new NbtCompound();
        child.putString("id", component.getId().toString());

        NbtList children = this.read();
        children.add(i, child);
        this.write(children);
    }

    @Override
    public void remove(int i) {
        NbtList children = this.read();
        children.remove(i);
        this.write(children);
    }

    protected abstract NbtList read();

    protected abstract void write(NbtList list);
}