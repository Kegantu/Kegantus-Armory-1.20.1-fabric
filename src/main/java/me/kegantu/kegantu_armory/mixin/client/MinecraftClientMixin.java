package me.kegantu.kegantu_armory.mixin.client;

import me.kegantu.kegantu_armory.KegantuArmory;
import me.kegantu.kegantu_armory.component.KatanaComponent;
import me.kegantu.kegantu_armory.init.ModEnchantments;
import me.kegantu.kegantu_armory.init.ModItems;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Final public GameOptions options;

    private int ticksBetweenDash = 0;
    private boolean wasPressingActivationKey = false;

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z", ordinal = 10))
    private void katanaDash(CallbackInfo ci){
        if (this.player != null && this.player.getMainHandStack().isOf(ModItems.AMETHYST_KATANA) && KatanaComponent.get(player).isBlocking() && EnchantmentHelper.getLevel(ModEnchantments.DASH_ENCHANTMENT, this.player.getMainHandStack()) > 0){

            if (ticksBetweenDash > 0) {
                ticksBetweenDash--;
            }
            if (this.options.attackKey.wasPressed() && !wasPressingActivationKey) {
                if (ticksBetweenDash > 0) {
                    ticksBetweenDash = 0;
                    ClientPlayNetworking.send(KegantuArmory.DASH_PACKET, PacketByteBufs.empty());
                } else {
                    ticksBetweenDash = 7;
                }
            }
            wasPressingActivationKey = this.options.attackKey.wasPressed();
        }
    }
}
