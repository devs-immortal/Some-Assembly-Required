package net.immortaldevs.sar.mixin.data;

import com.google.gson.JsonElement;
import net.immortaldevs.sar.base.data.ComponentModelGenerator;
import net.immortaldevs.sar.base.data.SarModelProvider;
import net.minecraft.data.DataCache;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(ModelProvider.class)
public abstract class ModelProviderMixin {
    @Inject(method = "run",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/data/client/ItemModelGenerator;register()V",
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void run(DataCache cache, CallbackInfo ci, Path path, Map<?, ?> map, Consumer<?> consumer, Map<?, ?> map2, Set<?> set, BiConsumer<Identifier, Supplier<JsonElement>> biConsumer) {
        if (this instanceof SarModelProvider sarModelProvider) {
            sarModelProvider.generateComponentModels(new ComponentModelGenerator(biConsumer));
        }
    }
}
