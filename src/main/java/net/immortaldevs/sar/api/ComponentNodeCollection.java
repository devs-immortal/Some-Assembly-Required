package net.immortaldevs.sar.api;

@SuppressWarnings("unused")
public interface ComponentNodeCollection extends Iterable<ComponentNode> {
    int size();

    boolean isEmpty();

    boolean contains(Component component);

    void add(Component component);

    void add(int i, Component component);

    ComponentNode get(int i);

    void remove(int i);

    void clear();
}
