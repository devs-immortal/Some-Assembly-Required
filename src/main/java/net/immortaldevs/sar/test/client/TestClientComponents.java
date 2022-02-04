package net.immortaldevs.sar.test.client;

import net.immortaldevs.sar.base.client.ClientComponents;
import net.immortaldevs.sar.base.client.CrackParticleSpriteModifier;
import net.immortaldevs.sar.base.client.ItemRenderers;
import net.immortaldevs.sar.base.client.VertexConsumerModifier;
import net.immortaldevs.sar.test.PotatoComponent;
import net.immortaldevs.sar.test.TestComponents;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

public final class TestClientComponents {
    public static void init() {
        ClientComponents.register(TestComponents.POTATO, data -> {
            Function<ItemStack, BakedModel> potato = data.containsChild("filling")
                    ? TestComponentModels.FILLED_POTATO
                    : TestComponentModels.POTATO;

            data.addModifier(ItemRenderers.ENTITY_TRANSLUCENT_CULL_LAYERED.get(potato));
            data.addModifier(CrackParticleSpriteModifier.of(potato));
            data.loadChild("filling");
        });

        ClientComponents.register(TestComponents.CHEESE, data -> {
            Function<ItemStack, BakedModel> cheese =
                    data.parent().isPresent() && data.parent().get().type() instanceof PotatoComponent
                            ? TestComponentModels.POTATO_CHEESE
                            : TestComponentModels.CHEESE;

            data.addModifier(ItemRenderers.ENTITY_TRANSLUCENT_CULL_LAYERED.get(cheese));
            data.addModifier(CrackParticleSpriteModifier.of(cheese));
        });

        ClientComponents.register(TestComponents.NITROGLYCERIN, data -> {
            Function<ItemStack, BakedModel> nitroglycerin =
                    data.parent().isPresent() && data.parent().get().type() instanceof PotatoComponent
                            ? TestComponentModels.POTATO_NITROGLYCERIN
                            : TestComponentModels.NITROGLYCERIN;

            data.addModifier(ItemRenderers.ENTITY_TRANSLUCENT_CULL_LAYERED.get(nitroglycerin));
            data.addModifier(CrackParticleSpriteModifier.of(nitroglycerin));
        });

        ClientComponents.register(TestComponents.CUM, data ->
                data.addModifier((VertexConsumerModifier) (model, stack, matrices, provider, vertices) ->
                        VertexConsumers.union(provider.getBuffer(TestRenderLayers.CUM), vertices)));
    }
}
