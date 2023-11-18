package me.kegantu.kegantu_armory.mixin.client;

import me.kegantu.kegantu_armory.component.KatanaComponent;
import me.kegantu.kegantu_armory.init.ModItems;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> {
    @Shadow protected abstract Arm getPreferredArm(T entity);

    @Shadow protected abstract ModelPart getArm(Arm arm);

    @Shadow @Final public ModelPart head;

    @Shadow @Final public ModelPart body;

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "animateArms", at = @At("TAIL"))
    protected void twoHanding(T entity, float animationProgress, CallbackInfo ci) {
        ItemStack stack = entity.getMainHandStack();
        if (stack.isOf(ModItems.AMETHYST_KATANA) &&
                this.handSwingProgress > 0.0F) {
            Arm arm = (getPreferredArm(entity) == Arm.RIGHT) ? Arm.LEFT : Arm.RIGHT;
            ModelPart modelPart = getArm(arm);
            float f = 1.0F - this.handSwingProgress;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float g = MathHelper.sin(f * 3.1415927F);
            float h = MathHelper.sin(this.handSwingProgress * 3.1415927F) * -(this.head.pitch - 0.7F) * 0.75F;
            modelPart.pitch -= g * 1.2F + h;
            modelPart.yaw += this.body.yaw * 2.0F;
            modelPart.roll += MathHelper.sin(this.handSwingProgress * 3.1415927F) * -0.4F;
        }
    }

    @Inject(method = "positionRightArm", at = @At("HEAD"), cancellable = true)
    protected void positionRightArm(T entity, CallbackInfo ci){
        if (!(entity instanceof PlayerEntity)){
            return;
        }
        KatanaComponent katana = KatanaComponent.get((PlayerEntity) entity);

        if (entity.getMainHandStack().isOf(ModItems.AMETHYST_KATANA)){
            if (katana.isBlocking()){
                this.rightArm.pitch = this.rightArm.pitch * 0.5F - 0.9424779F + (float) Math.toRadians(-45);
                this.rightArm.yaw = (float) (-Math.PI / 6) + (float) Math.toRadians(-5);
                this.rightArm.roll = (float) Math.toRadians(45);

                this.leftArm.pitch = this.leftArm.pitch * 0.5F - 0.9424779F + (float) Math.toRadians(0);
                this.leftArm.yaw = (float) Math.toRadians(15);
                this.leftArm.roll = (float) Math.toRadians(45);
                ci.cancel();
            }
        }
    }
}
