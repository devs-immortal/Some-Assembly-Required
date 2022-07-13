package net.immortaldevs.sar.impl;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.Collection;
import java.util.Iterator;

public final class Util {
    public static void updateComponents(Collection<NbtElement> elements, boolean recurse) {
        Iterator<NbtElement> iterator = elements.iterator();
        while (iterator.hasNext()) {
            NbtElement element = iterator.next();
            if (element instanceof NbtCompound nbt && "sar:air".equals(nbt.getString("id"))) {
                iterator.remove();
            } else if (recurse && element instanceof NbtList list) {
                updateComponents(list, false);
                if (list.isEmpty()) iterator.remove();
            }
        }
    }
}
