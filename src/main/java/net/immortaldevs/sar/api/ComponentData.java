package net.immortaldevs.sar.api;

import java.util.Iterator;

public interface ComponentData extends SkeletalComponentData {
    Iterator<ComponentData> loadedChildIterator();
}
