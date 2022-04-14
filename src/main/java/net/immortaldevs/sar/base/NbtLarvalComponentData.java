package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class NbtLarvalComponentData extends NbtSkeletalComponentData implements LarvalComponentData {
    private final Consumer<LarvalComponentData> initialiser;
    private HashModifierMap modifierMap = new HashModifierMap();
    private NbtLarvalComponentData firstChild = null;
    private NbtLarvalComponentData nextSibling = null;

    public NbtLarvalComponentData(NbtCompound nbt,
                                  @Nullable SkeletalComponentData parent,
                                  Runnable changedCallback,
                                  Consumer<LarvalComponentData> initialiser,
                                  Consumer<LarvalComponentData> transformer,
                                  Consumer<Modifier> modifierConsumer) {
        super(nbt, parent, changedCallback);
        this.initialiser = initialiser;

        initialiser.accept(this);
        transformer.accept(this);
        this.modifierMap.entries().forEach(modifierConsumer);
        this.modifierMap = null;
    }

    @Override
    public ModifierMap modifiers() {
        if (this.modifierMap != null) return this.modifierMap;
        throw new IllegalStateException("Cannot get modifiers; " +
                "component data has already been initialised");
    }

    @Override
    public void addModifier(Modifier modifier) {
        modifier.register(this.modifiers());
    }

    @Override
    public void loadChild(String name, Consumer<LarvalComponentData> transformer) {
        NbtCompound components = this.nbt.getCompound("components");

        if (components.contains(name, NbtElement.COMPOUND_TYPE)) {
            NbtLarvalComponentData child = new NbtLarvalComponentData(
                    components.getCompound(name),
                    this,
                    this.changedCallback,
                    this.initialiser,
                    transformer,
                    this::addModifier);

            child.nextSibling = this.firstChild;
            this.firstChild = child;
        }
    }

    @Override
    public void loadChildren(String name, Consumer<LarvalComponentData> transformer) {
        NbtList nbtChildren = this.nbt.getCompound("components")
                .getList(name, NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < nbtChildren.size(); i++) {
            new NbtLarvalComponentData(
                    nbtChildren.getCompound(i),
                    this,
                    this.changedCallback,
                    this.initialiser,
                    transformer,
                    this::addModifier);
        }
    }

    @Override
    public Iterator<ComponentData> loadedChildIterator() {
        return new ChildIterator(this.firstChild);
    }

    private static final class ChildIterator implements Iterator<ComponentData> {
        private NbtLarvalComponentData next;

        public ChildIterator(NbtLarvalComponentData firstChild) {
            this.next = firstChild;
        }

        @Override
        public boolean hasNext() {
            return this.next != null;
        }

        @Override
        public ComponentData next() {
            NbtLarvalComponentData out = this.next;
            if (out == null) throw new NoSuchElementException();
            this.next = out.nextSibling;
            return out;
        }
    }
}
