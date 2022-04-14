package net.immortaldevs.sar.impl.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.immortaldevs.sar.base.client.LoadedModel;
import net.immortaldevs.sar.test.client.TestComponentModels;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class SarClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> LoadedModel.emitIds(out));

        ModelLoadingRegistry.INSTANCE.registerVariantProvider(manager -> (modelId, context) -> {
            if ("component".equals(modelId.getVariant())) {
                return context.loadModel(new Identifier(modelId.getNamespace(),
                        "component/" + modelId.getPath()));
            }

            return null;
        });

        TestComponentModels.init();
    }
}
