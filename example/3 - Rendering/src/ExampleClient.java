import net.fabricmc.api.ClientModInitializer;
import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.base.client.LoadedModelComponentModel;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public final class ExampleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // LoadedModelComponentModel does all the model loading and registration for you. Simply creating one is
        // sufficient, but I want the bomb to render behind the item instead of in front, so I'm extending the class
        // to do so.
        new LoadedModelComponentModel(Example.BOMB_COMPONENT) {
            @Override
            public void itemRender(ComponentData data,
                                   VertexConsumerProvider vertexConsumers,
                                   ItemStack stack,
                                   MatrixStack matrices,
                                   ModelTransformation.Mode renderMode,
                                   int light,
                                   int overlay) {
                matrices.translate(0.0, 0.0, -0.0625); // The matrices are automatically pushed and popped
                super.itemRender(data, vertexConsumers, stack, matrices, renderMode, light, overlay);
            }

            @Override
            protected VertexConsumer getItemVertexConsumer(ComponentData data,
                                                           VertexConsumerProvider vertexConsumers,
                                                           ItemStack stack,
                                                           ModelTransformation.Mode renderMode) {
                return vertexConsumers.getBuffer(TexturedRenderLayers.getEntityTranslucentCull());
            }
        };
    }
}
