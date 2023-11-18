package me.kegantu.kegantu_armory.mixin;

import me.kegantu.kegantu_armory.component.KatanaComponent;
import me.kegantu.kegantu_armory.init.ModDamageSources;
import me.kegantu.kegantu_armory.init.ModItems;
import me.kegantu.kegantu_armory.init.ModStatusEffects;
import me.kegantu.kegantu_armory.item.Katana;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{
    @Shadow protected ItemStack activeItemStack;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract boolean isUsingItem();

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Inject(method = "isBlocking", at = @At("HEAD"), cancellable = true)
    private void isBlocking(CallbackInfoReturnable<Boolean> cir){
        if (!this.activeItemStack.isEmpty() && this.activeItemStack.getItem() instanceof Katana && this.isUsingItem()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "applyDamage", at = @At("HEAD"))
    private void damage(DamageSource source, float amount, CallbackInfo ci){
        Entity entity = source.getAttacker();

        if (entity instanceof PlayerEntity player){
            if (player.getMainHandStack().isOf(ModItems.AMETHYST_KATANA)){
                KatanaComponent katana = KatanaComponent.get(player);

                katana.addBlood(50);
            }
        }

    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bleeding(CallbackInfo ci){
        if (this.hasStatusEffect(ModStatusEffects.BLEEDING) && (this.age % (20 / (MathHelper.clamp(this.getStatusEffect(ModStatusEffects.BLEEDING).getAmplifier() + 1, 1, 20))) == 0)) {
            this.damage(ModDamageSources.of(this.getWorld(), ModDamageSources.BLEEDING_DAMAGE_TYPE), 1);
            this.timeUntilRegen = 0;
        }
    }
}
