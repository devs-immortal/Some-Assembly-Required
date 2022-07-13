package net.immortaldevs.sar.api;

@SuppressWarnings("unused")
public interface ComponentNodeCollection extends Iterable<ComponentNode> {
    int size();

    boolean isEmpty();

    void add(Component component);

    void add(int i, Component component);

    void remove(int i);

    void clear();

    ComponentNode get(int i);
}
