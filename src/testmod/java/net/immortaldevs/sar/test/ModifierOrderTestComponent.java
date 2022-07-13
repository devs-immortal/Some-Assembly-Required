package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentNode;
import net.immortaldevs.sar.api.ComponentNodeHandler;
import net.immortaldevs.sar.api.ModifierHandler;
import net.immortaldevs.sar.base.NbtComponentNode;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class ModifierOrderTestComponent extends Component {
    @Override
    public void loadComponentNodes(ComponentNode node, ComponentNodeHandler handler) {
        NbtCompound nbt = node.getNbt();
        if (nbt == null || !nbt.contains("child", NbtElement.COMPOUND_TYPE)) return;
        handler.load(new NbtComponentNode(nbt.getCompound("child"), node));
    }

    @Override
    public void addModifiers(ComponentNode node, ModifierHandler handler) {
        NbtCompound nbt = node.getNbt();
        String name = nbt == null ? "missingNbt" : nbt.getString("name");
        handler.add((OrderTestModifier) () -> name);
    }
}
