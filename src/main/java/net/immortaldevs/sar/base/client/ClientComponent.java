package net.immortaldevs.sar.base.client;

import net.immortaldevs.sar.api.LarvalComponentData;

@FunctionalInterface
public interface ClientComponent {
    void init(LarvalComponentData data);
}
