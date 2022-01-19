package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static net.immortaldevs.sar.base.Sar.sanitiseChild;

public class NbtComponentData extends NbtSkeletalComponentData implements ComponentData {
    private final HashModifierMap modifierMap;
    private final boolean onClient;

    public NbtComponentData(@Nullable SkeletalComponentData parent, Runnable changedCallback, NbtCompound nbt, boolean onClient) {
        super(parent, changedCallback, nbt);
        this.modifierMap = new HashModifierMap();
        this.onClient = onClient;
        this.type().init(this);
    }

    public NbtComponentData(@Nullable SkeletalComponentData parent, Runnable changedCallback, NbtCompound nbt, boolean onClient, Consumer<Modifier> modifierHandler) {
        this(parent, changedCallback, nbt, onClient);

        for (Modifier modifier : this.modifierMap.entries()) {
            modifierHandler.accept(modifier);
        }
    }

    @Override
    public ModifierMap modifierMap() {
        return this.modifierMap;
    }

    @Override
    public void addModifier(Modifier modifier) {
        modifier.register(this.modifierMap);
    }

    @Override
    public void loadChild(String name, Consumer<Modifier> modifierHandler) {
        String sanitisedName = sanitiseChild(name);

        if (this.nbt.contains(sanitisedName, NbtElement.COMPOUND_TYPE)) {
            new NbtComponentData(this,
                    this.changedCallback,
                    this.nbt.getCompound(sanitisedName),
                    this.onClient,
                    modifierHandler);
        }
    }

    @Override
    public void loadChildren(String name, Consumer<Modifier> modifierHandler) {
        NbtList nbtChildren = this.nbt.getList(sanitiseChild(name), NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < nbtChildren.size(); i++) {
            new NbtComponentData(this,
                    this.changedCallback,
                    nbtChildren.getCompound(i),
                    this.onClient,
                    modifierHandler);
        }
    }

    @Override
    public boolean onClient() {
        return this.onClient;
    }
}
