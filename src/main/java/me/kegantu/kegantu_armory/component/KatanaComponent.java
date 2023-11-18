package me.kegantu.kegantu_armory.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import me.kegantu.kegantu_armory.KegantuArmory;
import me.kegantu.kegantu_armory.init.ModItems;
import me.kegantu.kegantu_armory.init.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class KatanaComponent implements AutoSyncedComponent, CommonTickingComponent {

    private final PlayerEntity player;

    private boolean blocking;

    private int blood;
    private int dashDuration;

    public static KatanaComponent get(@NotNull PlayerEntity player) {
        return KegantuArmory.KATANA.get(player);
    }

    public KatanaComponent(PlayerEntity player) {
        this.player = player;
        this.blocking = false;
        this.blood = 400;
        this.dashDuration = 0;
    }

    private void sync() {
        KegantuArmory.KATANA.sync(this.player);
    }

    @Override
    public void tick() {
        ItemStack stack = this.player.getMainHandStack();
        boolean isKatana = this.player.isAlive() && !this.player.isDead() && !this.player.isRemoved() && stack.isOf(ModItems.AMETHYST_KATANA);
        if (isDashing()){
            --this.dashDuration;
            if (this.blood <= 0|| !isKatana) {
                this.dashDuration = 0;
                this.sync();
            }
        }
    }

    @Override
    public void serverTick() {
        if (isDashing()){
            this.player.fallDistance = 0.0f;
        }
        this.tick();
    }

    @Override
    public void clientTick() {
        if (isDashing()){
            this.player.setVelocity(this.player.getRotationVector().multiply(3,2,3));
            this.addDashParticles(player);
        }
        this.tick();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.blocking = tag.getBoolean("IsBlocking");
        this.blood = tag.getInt("Blood");
        this.dashDuration = tag.getInt("DashDuration");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsBlocking", this.blocking);
        tag.putInt("Blood", this.blood);
        tag.putInt("DashDuration", this.dashDuration);
    }

    public boolean isBlocking() {
        if (!this.player.getWorld().isClient() && !(this.player.getMainHandStack().isOf(ModItems.AMETHYST_KATANA)) && !this.player.isUsingItem()){
            setBlocking(false);
        }
        return blocking;
    }

    public boolean isParrying(){
        if (!(this.player.getMainHandStack().isOf(ModItems.AMETHYST_KATANA)) && !this.player.isUsingItem()){
            return false;
        }

        //this.player.sendMessage(Text.literal(String.valueOf(this.player.getMainHandStack().getMaxUseTime() - this.player.getItemUseTime())));
        return this.player.getMainHandStack().getMaxUseTime() - this.player.getItemUseTimeLeft() <= 10;
    }

    public void setCoolDown(int duration){
        this.player.getItemCooldownManager().set(this.player.getMainHandStack().getItem(), duration);
    }

    public void damageKatana(){
        if (this.player.getMainHandStack().isOf(ModItems.AMETHYST_KATANA)) {
            if (!this.player.getWorld().isClient()) {
                this.player.incrementStat(Stats.USED.getOrCreateStat(this.player.getMainHandStack().getItem()));
            }

            Hand hand = this.player.getActiveHand();
            this.player.getMainHandStack().damage(1, this.player, player -> player.sendToolBreakStatus(hand));
            if (this.player.getMainHandStack().isEmpty()) {
                if (hand == Hand.MAIN_HAND) {
                    this.player.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }

                this.player.setStackInHand(hand, ItemStack.EMPTY);
            }
        }
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
        sync();
    }

    public int getBloodPercentage() {
        return this.blood / 400;
    }

    public void addBlood(int blood) {
        if (this.blood == 400 * 13){
            sync();
            return;
        }
        if (!this.player.getWorld().isClient() && this.player.getMainHandStack().isOf(ModItems.AMETHYST_KATANA)){
            this.blood += blood;
            this.blood = MathHelper.clamp(this.blood, 400, 400 * 13);
            //this.player.sendMessage(Text.literal(String.valueOf(this.blood)));
        }
        sync();
    }

    public void subtractBlood(int amount){
        if (!this.player.getWorld().isClient() && this.player.getMainHandStack().isOf(ModItems.AMETHYST_KATANA)){
            this.blood -= amount;
            this.blood = MathHelper.clamp(this.blood, 400, 400 * 13);
        }
        sync();
    }

    public void useDash(){
        if (getBloodPercentage() >= 3){
            this.dashDuration = 15;
            this.subtractBlood(800);
            this.player.getWorld().playSound(null, this.player.getBlockPos(), ModSounds.DASH_SOUND_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            sync();
        }
    }

    public boolean isDashing(){
        return this.dashDuration > 0;
    }

    public void addDashParticles(PlayerEntity entity) {
        if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
            for (int i = 0; i < 4; i++) {
                entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
            }
        }
    }
}
