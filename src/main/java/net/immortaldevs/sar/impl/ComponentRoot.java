package net.immortaldevs.sar.impl;

import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.api.FixedModifierMap;

import java.util.Collections;
import java.util.Iterator;

public interface ComponentRoot {
    ComponentRoot EMPTY = new ComponentRoot() {
        @Override
        public FixedModifierMap modifiers() {
            return FixedModifierMap.EMPTY;
        }

        @Override
        public Iterator<ComponentData> components() {
            return Collections.emptyIterator();
        }
    };

    FixedModifierMap modifiers();

    Iterator<ComponentData> components();
}
