package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.api.SkeletalComponentData;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface ItemStackExt {
    @Nullable ComponentData sar$getComponentRoot();

    boolean sar$hasComponentRoot();

    @Nullable SkeletalComponentData sar$getSkeletalComponentRoot();

    SkeletalComponentData sar$getOrCreateSkeletalComponentRoot(Component type);
}
