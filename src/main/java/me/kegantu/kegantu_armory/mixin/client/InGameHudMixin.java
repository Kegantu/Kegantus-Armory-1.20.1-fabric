package me.kegantu.kegantu_armory.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kegantu.kegantu_armory.init.ModStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private static Identifier ICONS;
    @Unique
    private static final Identifier BLEEDING_HEARTS = new Identifier("kegantu-armory", "textures/gui/bleeding_hearts.png");

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void drawBleedingHeart(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci){
        if (!blinking && type == InGameHud.HeartType.NORMAL && MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player && (player.hasStatusEffect(ModStatusEffects.BLEEDING))) {
            if (player.hasStatusEffect(ModStatusEffects.BLEEDING)) {
                RenderSystem.setShaderTexture(0, BLEEDING_HEARTS);
            }

            context.drawTexture(BLEEDING_HEARTS, x, y, halfHeart ? 9 : 0, v, 9, 9);
            RenderSystem.setShaderTexture(0, ICONS);
            ci.cancel();
        }
    }
}
