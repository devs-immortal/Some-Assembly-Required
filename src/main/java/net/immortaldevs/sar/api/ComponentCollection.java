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

    /**
     * @return Whether this ComponentCollection contains the provided
     *         SkeletalComponentData or Component.
     */
    // The parameter is an Object to reflect Java's Collection interface
    default boolean contains(Object o) {
        if (isEmpty()) return false;

        for (var datum : this) {
            if (datum.equals(o) || datum.getComponent().equals(o)) {
                return true;
            }
        }

        return false;
    }

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
