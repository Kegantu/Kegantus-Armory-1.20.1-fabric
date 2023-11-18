package me.kegantu.kegantu_armory.mixin;

import me.kegantu.kegantu_armory.component.KatanaComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends ProjectileEntity {

    public FireworkRocketEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;explodeAndRemove()V"), cancellable = true)
    private void explode(EntityHitResult entityHitResult, CallbackInfo ci){
        Entity entity = entityHitResult.getEntity();

        if (!(entity instanceof PlayerEntity)){
            return;
        }

        KatanaComponent katana = KatanaComponent.get((PlayerEntity) entity);

        if (!this.getWorld().isClient()){
            if (katana.isBlocking()){
                if (katana.isParrying()){
                    this.addVelocity(this.getVelocity().negate());
                    ci.cancel();
                }
            }
        }
    }
}
