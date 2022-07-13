package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.SarRegistries;
import net.minecraft.util.Identifier;

public class SarModelIds {
    public static Identifier getComponentModelId(Component component) {
        Identifier identifier = SarRegistries.COMPONENT.getId(component);
        return new Identifier(identifier.getNamespace(), "component/" + identifier.getPath());
    }

    public static Identifier getComponentSubModelId(Component component, String suffix) {
        Identifier identifier = SarRegistries.COMPONENT.getId(component);
        return new Identifier(identifier.getNamespace(), "component/" + identifier.getPath() + suffix);
    }
}
