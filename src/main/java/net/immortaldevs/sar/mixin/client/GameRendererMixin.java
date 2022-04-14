package net.immortaldevs.sar.mixin.client;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.base.client.SarShaders;
import net.minecraft.client.gl.Program;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "loadShaders",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 0,
                    remap = false),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void loadShaders(ResourceManager manager, CallbackInfo ci, List<Program> list, List<Pair<Shader, Consumer<Shader>>> list2) throws IOException {
        list2.add(Pair.of(new Shader(manager, "sar_rendertype_translucent_ghost", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL),
                (shader) -> SarShaders.translucentGhost = shader));

        list2.add(Pair.of(new Shader(manager, "sar_rendertype_translucent_overlay", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL),
                (shader) -> SarShaders.translucentOverlay = shader));
    }
}
