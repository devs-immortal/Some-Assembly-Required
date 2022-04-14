package net.immortaldevs.sar.api;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class Component {
    public static final Component AIR = new Component();
    public static final Component ROOT = new Component();

    public void init(LarvalComponentData data) {
    }

    public final Identifier getId() {
        return SarRegistries.COMPONENT.getId(this);
    }

    @Override
    public String toString() {
        return this.getId().toString();
    }

    public static Component fromId(@Nullable Identifier id) {
        return SarRegistries.COMPONENT.get(id);
    }

    public static Component fromId(@Nullable String id) {
        return fromId(Identifier.tryParse(id));
    }
}
