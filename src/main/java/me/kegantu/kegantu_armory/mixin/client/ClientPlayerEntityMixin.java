package me.kegantu.kegantu_armory.mixin.client;

import com.mojang.authlib.GameProfile;
import me.kegantu.kegantu_armory.client.KegantuArmoryClient;
import me.kegantu.kegantu_armory.component.KatanaComponent;
import me.kegantu.kegantu_armory.init.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Shadow public abstract boolean isMainPlayer();

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void clientDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if (MinecraftClient.getInstance().player != null) {
            if (source.getSource() == null){
                return;
            }
            KatanaComponent katana = KatanaComponent.get(this);
            if (katana.isBlocking()){
                if (katana.isParrying()){
                    MinecraftClient.getInstance().player.playSound(ModSounds.PARRY_SOUND_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                    KegantuArmoryClient.parryTicks = 5;
                }
            }
        }
    }
}
