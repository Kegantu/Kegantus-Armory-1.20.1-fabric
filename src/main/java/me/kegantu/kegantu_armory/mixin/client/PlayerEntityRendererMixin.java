package me.kegantu.kegantu_armory.mixin.client;

import me.kegantu.kegantu_armory.component.KatanaComponent;
import me.kegantu.kegantu_armory.init.ModItems;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir){
        ItemStack stackInHand = player.getStackInHand(hand);
        KatanaComponent katana = KatanaComponent.get(player);

        if (stackInHand.isOf(ModItems.AMETHYST_KATANA)){
            if (katana.isBlocking()){
                return;
            }
            if (hand == Hand.OFF_HAND) {
                cir.setReturnValue(BipedEntityModel.ArmPose.BLOCK);
            } else{
                cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_CHARGE);
            }
        }
    }
}
