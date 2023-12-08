package me.kegantu.kegantu_armory.mixin.client;

import me.kegantu.kegantu_armory.KegantuArmory;
import me.kegantu.kegantu_armory.init.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow public abstract ItemModels getModels();

    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useAmethystKatanaModel(BakedModel value, ItemStack stack, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrixStack, VertexConsumerProvider provider, int light, int overlay){
        if (stack.isOf(ModItems.AMETHYST_KATANA) && mode != ModelTransformationMode.GUI){
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player == null){
                return value;
            }

            if (client.player.isUsingItem()){
                return this.getModels().getModelManager().getModel(new ModelIdentifier(KegantuArmory.MOD_ID, "amethyst_katana_blocking", "inventory"));
            }
            return this.getModels().getModelManager().getModel(new ModelIdentifier(KegantuArmory.MOD_ID, "amethyst_katana_handheld", "inventory"));
        }
        return value;
    }
}
