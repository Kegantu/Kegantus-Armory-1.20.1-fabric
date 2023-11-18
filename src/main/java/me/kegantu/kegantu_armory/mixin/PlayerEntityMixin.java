package me.kegantu.kegantu_armory.mixin;

import me.kegantu.kegantu_armory.component.KatanaComponent;
import me.kegantu.kegantu_armory.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        KatanaComponent katana = KatanaComponent.get((PlayerEntity) (LivingEntity)this);
        Entity entity = source.getSource();

        if (entity == null) {
            return;
        }

        if(katana.isBlocking()){
            if(katana.isParrying()){
                this.getWorld().playSound(null, this.getBlockPos(), ModSounds.PARRY_SOUND_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                if(!this.getWorld().isClient()){
                    this.heal(5);

                    if (entity instanceof PersistentProjectileEntity){
                        entity.addVelocity(entity.getVelocity().multiply(3));
                    }

                    katana.setCoolDown(180);
                }
                return;
            }

            if (!this.getWorld().isClient()){
                katana.damageKatana();
            }
        }
    }

    @Override
    public boolean isImmuneToExplosion() {
        KatanaComponent katana = KatanaComponent.get((PlayerEntity) (LivingEntity)this);
        return katana.isDashing();
    }
}
