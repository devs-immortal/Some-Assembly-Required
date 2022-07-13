package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.ComponentNode;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ComponentModel {
    @Nullable Sprite getParticleSprite(ComponentNode node,
                                       ItemStack stack);

    void render(ComponentNode node,
                Children children,
                VertexConsumerProvider vertexConsumers,
                ItemStack stack,
                MatrixStack matrices,
                ModelTransformation.Mode renderMode,
                int light,
                int overlay);


    interface Children {
        void render();
    }
}
