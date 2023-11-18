package me.kegantu.kegantu_armory.mixin.client;

import me.kegantu.kegantu_armory.init.ModItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms> extends FeatureRenderer<T, M>{
    public HeldItemFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        ItemStack itemStack = entity.getMainHandStack();
        boolean mainArm = (entity.getMainArm() == arm);
        if (itemStack.isOf(ModItems.AMETHYST_KATANA) &&
                !mainArm) {
            matrices.push();
            getContextModel().setArmAngle(arm, matrices);
            matrices.pop();
            ci.cancel();
        }
    }
}
