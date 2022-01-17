package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.api.Modifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class BasicComponentData implements ComponentData {
    private final Consumer<Modifier> modifierHandler;
    private final NbtCompound nbt;
    private final Component type;
    private final boolean onClient;

    public BasicComponentData(NbtCompound nbt, boolean client, Consumer<Modifier> modifierHandler) {
        this.modifierHandler = modifierHandler;
        this.nbt = nbt;
        this.type = SarRegistries.COMPONENT.get(Identifier.tryParse(nbt.getString("type")));
        this.onClient = client;

        this.type.init(this);
    }

    @Override
    public NbtCompound nbt() {
        return nbt;
    }

    @Override
    public Component type() {
        return type;
    }

    @Override
    public void addModifier(Modifier modifier) {
        modifierHandler.accept(modifier);
    }

    @Override
    public void addChild(String name, Consumer<Modifier> modifierHandler) {
        if (nbt.contains(name, NbtElement.COMPOUND_TYPE)) {
            new BasicComponentData(nbt.getCompound(name), onClient, modifierHandler);
        }
    }

    @Override
    public void addChild(String name) {
        this.addChild(name, modifierHandler);
    }

    @Override
    public void addChildren(String name, Consumer<Modifier> modifierHandler) {
        NbtList nbtChildren = nbt.getList(name, NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtChildren.size(); i++) {
            new BasicComponentData(nbtChildren.getCompound(i), onClient, modifierHandler);
        }
    }

    @Override
    public void addChildren(String name) {
        this.addChildren(name, modifierHandler);
    }

    @Override
    public boolean onClient() {
        return onClient;
    }
}
