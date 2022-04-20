package net.immortaldevs.sar.api;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * A component host is responsible for holding components, and exposing them and their modifiers
 */
public interface ComponentHost {
    /**
     * Gets the modifiers for all attached components, initialising them if needed
     * @return a map of all the modifiers
     */
    FixedModifierMap getModifiers();

    /**
     * An iterator over all top-level attached components. Does not include the components' children
     * @return an iterator over all attached components
     */
    Iterator<ComponentData> componentIterator();

    /**
     * @param name the name of the component to check for
     * @return true if a component with this name is present at the top level
     */
    boolean hasComponent(String name);

    /**
     * @param name the name of the components to check for
     * @return true if components with this name are present at the top level
     */
    boolean hasComponents(String name);

    /**
     * @param name the name of the component to retrieve
     * @return the component's data, or null if no component with this name is present
     */
    @Nullable SkeletalComponentData getComponent(String name);

    /**
     * @param name the name of the component to retrieve
     * @param component the component to create if no existing component is found
     * @return the component's data, created if necessary
     */
    SkeletalComponentData getOrCreateComponent(String name, Component component);

    /**
     * Removes a component from the top level
     * @param name the name of the component to remove
     */
    void removeComponent(String name);

    /**
     * @param name the name of the components to retrieve
     * @return a (possibly empty) collection of components
     */
    ComponentCollection getComponents(String name);
}
