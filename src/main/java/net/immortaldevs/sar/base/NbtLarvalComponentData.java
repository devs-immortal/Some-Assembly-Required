package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NbtLarvalComponentData extends NbtSkeletalComponentData implements LarvalComponentData {
    private final Consumer<LarvalComponentData> initialiser;
    private final HashModifierMap modifierMap = new HashModifierMap();

    public NbtLarvalComponentData(
            @Nullable SkeletalComponentData parent,
            Runnable changedCallback,
            NbtCompound nbt,
            Consumer<LarvalComponentData> initialiser,
            Consumer<LarvalComponentData> transformer,
            Consumer<Modifier> modifierConsumer
    ) {
        super(parent, changedCallback, nbt);
        this.initialiser = initialiser;

        initialiser.accept(this);
        transformer.accept(this);
        this.modifierMap.entries().forEach(modifierConsumer);
    }

    @Override
    public ModifierMap modifiers() {
        return this.modifierMap;
    }

    @Override
    public void addModifier(Modifier modifier) {
        modifier.register(this.modifierMap);
    }

    @Override
    public void loadChild(String name, Consumer<LarvalComponentData> transformer) {
        String sanitisedName = sanitiseChildName(name);

        if (this.nbt.contains(sanitisedName, NbtElement.COMPOUND_TYPE)) {
            new NbtLarvalComponentData(this,
                    this.changedCallback,
                    this.nbt.getCompound(sanitisedName),
                    this.initialiser,
                    transformer,
                    this::addModifier);
        }
    }

    @Override
    public void loadChildren(String name, Consumer<LarvalComponentData> transformer) {
        NbtList nbtChildren = this.nbt.getList(sanitiseChildName(name), NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < nbtChildren.size(); i++) {
            new NbtLarvalComponentData(this,
                    this.changedCallback,
                    nbtChildren.getCompound(i),
                    this.initialiser,
                    transformer,
                    this::addModifier);
        }
    }
}
