package net.immortaldevs.sar.api;

import java.util.Iterator;

public interface ComponentCollection extends Iterable<SkeletalComponentData> {
    int size();

    boolean isEmpty();

    void add(Component component);

    void add(int i, Component component);

    void remove(int i);

    void clear();

    SkeletalComponentData get(int i);

    @Override
    default Iterator<SkeletalComponentData> iterator() {
       return new Iterator<>() {
           int index = 0;

           @Override
           public boolean hasNext() {
               return index < size();
           }

           @Override
           public SkeletalComponentData next() {
               return get(index++);
           }
       };
    }
}
