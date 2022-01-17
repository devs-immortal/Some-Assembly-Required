package net.immortaldevs.sar.base;

import net.minecraft.nbt.NbtCompound;

public class VersionedRootComponentData extends RootComponentData {
    public final int version;

    public VersionedRootComponentData(NbtCompound nbt, boolean onClient, int version) {
        super(nbt, onClient);
        this.version = version;
    }
}
