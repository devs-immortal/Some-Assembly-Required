package net.immortaldevs.sar.api;

import net.immortaldevs.sar.base.SarRegistries;

public abstract class Component {
    public static final Component UNKNOWN = new Component() {
        @Override
        public void init(ComponentData data) {
        }
    };

    public abstract void init(ComponentData data);

    @Override
    public String toString() {
        return SarRegistries.COMPONENT.getId(this).toString();
    }
}
