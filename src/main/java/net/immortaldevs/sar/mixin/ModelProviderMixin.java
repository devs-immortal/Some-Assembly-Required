package net.immortaldevs.sar.mixin;

import com.google.gson.JsonElement;
import net.immortaldevs.sar.base.ComponentModelGenerator;
import net.immortaldevs.sar.base.SarModelProvider;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(ModelProvider.class)
public abstract class ModelProviderMixin {
    @SuppressWarnings("rawtypes")
    @Inject(method = "run",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/data/client/ItemModelGenerator;register()V",
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void run(CallbackInfo ci,
                     Map map,
                     Consumer<BlockStateSupplier> consumer,
                     Map map2,
                     Set set,
                     BiConsumer<Identifier, Supplier<JsonElement>> biConsumer,
                     Consumer<Item> consumer2) {
        if (this instanceof SarModelProvider sarModelProvider) {
            sarModelProvider.generateComponentModels(new ComponentModelGenerator(biConsumer));
        }
    }
}
