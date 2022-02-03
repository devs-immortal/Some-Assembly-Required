package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.api.SkeletalComponentData;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ItemStackExt {
    Optional<RootComponentData> sar$getComponentRoot();

    boolean sar$hasComponentRoot();

    Optional<SkeletalComponentData> sar$getSkeletalComponentRoot();

    SkeletalComponentData sar$getOrCreateSkeletalComponentRoot();
}
