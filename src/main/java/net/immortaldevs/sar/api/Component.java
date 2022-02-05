package net.immortaldevs.sar.api;

public class Component {
    public static final Component UNKNOWN = new Component();
    public static final Component ROOT = new Component();

    public void init(LarvalComponentData data) {
    }

    @Override
    public String toString() {
        return SarRegistries.COMPONENT.getId(this).toString();
    }
}
