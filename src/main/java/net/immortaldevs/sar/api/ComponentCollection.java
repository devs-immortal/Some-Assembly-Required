package net.immortaldevs.sar.api;

public interface ComponentCollection {
    int size();

    boolean isEmpty();

    void add(Component component);

    void add(int i, Component component);

    void remove(int i);

    void clear();

    SkeletalComponentData get(int i);
}
