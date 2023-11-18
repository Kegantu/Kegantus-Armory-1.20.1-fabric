package me.kegantu.kegantu_armory.mixin.client;

import me.kegantu.kegantu_armory.component.KatanaComponent;
import me.kegantu.kegantu_armory.init.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Shadow public abstract void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color);

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"))
    private void drawBloodBar(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci){
        if (MinecraftClient.getInstance().player == null){
            return;
        }
        if (stack.isOf(ModItems.AMETHYST_KATANA)){
            KatanaComponent katana = KatanaComponent.get(MinecraftClient.getInstance().player);
            int color = 13373696;
            int charge = katana.getBloodPercentage();
            if (charge >= 1.0F){
                fill(RenderLayer.getGuiOverlay(), x + 2, y + 13, x + 2 + charge, y + 14, color | 0xFF000000);
            }
        }
    }
}
