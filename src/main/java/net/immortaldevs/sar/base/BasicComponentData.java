package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class BasicComponentData implements ComponentData {
    private final HashModifierMap modifierMap;
    private final NbtCompound nbt;
    private final Component type;
    private final boolean onClient;

    public BasicComponentData(NbtCompound nbt, boolean client, Consumer<Modifier> modifierHandler) {
        this.modifierMap = new HashModifierMap();
        this.nbt = nbt;
        this.type = SarRegistries.COMPONENT.get(Identifier.tryParse(nbt.getString("type")));
        this.onClient = client;

        this.type.init(this);

        for (Modifier modifier : modifierMap.entries()) {
            modifierHandler.accept(modifier);
        }
    }

    @Override
    public NbtCompound nbt() {
        return this.nbt;
    }

    @Override
    public Component type() {
        return this.type;
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
    public void addChild(NbtCompound data, Consumer<Modifier> modifierHandler) {
        new BasicComponentData(data, this.onClient, modifierHandler);
    }

    @Override
    public boolean onClient() {
        return this.onClient;
    }
}
