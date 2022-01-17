package net.immortaldevs.sar.mixin.client;

import net.immortaldevs.sar.base.client.SpriteExt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Sprite.class)
public abstract class SpriteMixin implements SpriteExt {
    @Unique
    private int atlasWidth;

    @Unique
    private int atlasHeight;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(SpriteAtlasTexture atlas, Sprite.Info info, int maxLevel, int atlasWidth, int atlasHeight, int x, int y, NativeImage image, CallbackInfo ci) {
        this.atlasWidth = atlasWidth;
        this.atlasHeight = atlasHeight;
    }

    @Override
    public int sar$getAtlasWidth() {
        return this.atlasWidth;
    }

    @Override
    public int sar$getAtlasHeight() {
        return this.atlasHeight;
    }
}
