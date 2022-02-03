package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.api.SkeletalComponentData;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ItemStackExt {
    default Optional<RootComponentData> sar$getComponentRoot() {
        throw new NotImplementedException();
    }

    default boolean sar$hasComponentRoot() {
        throw new NotImplementedException();
    }

    default Optional<SkeletalComponentData> sar$getSkeletalComponentRoot() {
        throw new NotImplementedException();
    }

    default SkeletalComponentData sar$getOrCreateSkeletalComponentRoot() {
        throw new NotImplementedException();
    }
}
