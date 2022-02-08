package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.*;
import net.minecraft.nbt.NbtCompound;

import java.util.function.Consumer;

public class NbtRootComponentData extends NbtSkeletalComponentData implements RootComponentData {
    private final HashModifierMap modifierMap;

    public NbtRootComponentData(Runnable changedCallback, NbtCompound nbt) {
        super(null, changedCallback, nbt, Component.ROOT);
        this.modifierMap = this.collect(data -> data.type().init(data));
    }

    @Override
    public FixedModifierMap modifiers() {
        return this.modifierMap;
    }

    @Override
    public FixedModifierMap fork(Consumer<LarvalComponentData> initialiser) {
        return this.collect(initialiser);
    }

    private HashModifierMap collect(Consumer<LarvalComponentData> initialiser) {
        HashModifierMap modifierMap = new HashModifierMap();
        NbtCompound components = this.nbt.getCompound("components");

        for (String key : components.getKeys()) {
            new NbtLarvalComponentData(this,
                    this.changedCallback,
                    components.getCompound(key),
                    initialiser,
                    data -> {},
                    modifier -> modifier.register(modifierMap));
        }

        return modifierMap;
    }
}
