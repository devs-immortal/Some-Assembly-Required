package net.immortaldevs.sar.base.client;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentData;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class ComponentModel {
    private static final Reference2ReferenceMap<Component, ComponentModel> REGISTRY
            = new Reference2ReferenceOpenHashMap<>();

    public ComponentModel(Component component) {
        if (REGISTRY.put(component, this) == null) return;
        throw new IllegalStateException("Component model for " + component + " has already been registered");
    }

    public abstract @Nullable Sprite getParticleSprite(ComponentData data,
                                             ItemStack stack);

    public abstract void itemRender(ComponentData data,
                                    VertexConsumerProvider vertexConsumers,
                                    ItemStack stack,
                                    MatrixStack matrices,
                                    ModelTransformation.Mode renderMode,
                                    int light,
                                    int overlay);

    public static @Nullable ComponentModel get(Component component) {
        return REGISTRY.get(component);
    }
}
