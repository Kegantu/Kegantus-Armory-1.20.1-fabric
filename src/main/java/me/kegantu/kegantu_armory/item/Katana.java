package me.kegantu.kegantu_armory.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import me.kegantu.kegantu_armory.component.KatanaComponent;
import me.kegantu.kegantu_armory.init.*;
import me.kegantu.kegantu_armory.util.BleedingEffectUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Katana extends ToolItem {
    private final float attackDamage;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public Katana(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, settings);
        this.attackDamage = (float)attackDamage + toolMaterial.getAttackDamage();
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION)
        );
        builder.put(
                EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION)
        );
        this.attributeModifiers = builder.build();
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (state.isOf(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return state.isIn(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        PlayerEntity player = (PlayerEntity) attacker;
        World world = player.getWorld();

        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        float attackCooldown = player.getAttackCooldownProgress(0.0f);

        world.playSound(null, attacker.getBlockPos(), ModSounds.AMETHYST_KATANA_SOUND_EVENT, SoundCategory.PLAYERS, 0.8f, 0.8f);

        if (!world.isClient()){
            for(LivingEntity livingEntity : player.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(1.5, 0.25, 1.5))) {
                if (livingEntity != player
                        && livingEntity != target
                        && !player.isTeammate(livingEntity)
                        && (!(livingEntity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingEntity).isMarker())
                        && player.squaredDistanceTo(livingEntity) < 9.0) {
                    livingEntity.takeKnockback(
                            0.4F, MathHelper.sin(player.getYaw() * (float) (Math.PI / 180.0)), -MathHelper.cos(player.getYaw() * (float) (Math.PI / 180.0))
                    );
                    livingEntity.damage(player.getDamageSources().playerAttack(player), 1 + attackDamage);
                }
            }
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);

        this.spawnSweepAttackParticles(world, player);

        if (EnchantmentHelper.getLevel(ModEnchantments.BLEEDING_ENCHANTMENT, stack) > 0){
            if (attackCooldown > 1.0f){
                return false;
            }
            if (target instanceof SkeletonEntity || target instanceof SkeletonHorseEntity){
                return false;
            }
            if (this.hasCorrectArmorOn(ArmorMaterials.LEATHER, player) || this.hasCorrectArmorOn(ArmorMaterials.CHAIN, player)){
                BleedingEffectUtil.randomlyApplyBleeding(target, 0.6f);
            } else {
                BleedingEffectUtil.randomlyApplyBleeding(target, 0.2f);
            }
        }
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0F) {
            stack.damage(2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }

        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        KatanaComponent katana = KatanaComponent.get(user);
        if (hand == Hand.OFF_HAND){
            return TypedActionResult.fail(stack);
        }
        if (user.getItemCooldownManager().isCoolingDown(ModItems.AMETHYST_KATANA)){
            return TypedActionResult.fail(stack);
        }
        if (user.isSneaking()){
            if (katana.getBloodPercentage() >= 6){
                world.playSound(null, user.getBlockPos(), ModSounds.SLICE_SOUND_EVENT, SoundCategory.PLAYERS, 1f ,1f);
                if (!world.isClient()){
                    int amountOfHeal = 0;
                    List<LivingEntity> livingEntities = world.getEntitiesByClass(LivingEntity.class, user.getBoundingBox().expand(16), LivingEntity::isAlive);

                    for (LivingEntity livingEntity: livingEntities) {
                        amountOfHeal++;

                        if (livingEntity instanceof PlayerEntity player){
                            if (player == user){
                                continue;
                            }
                        }

                        livingEntity.damage(livingEntity.getDamageSources().playerAttack(user), 5.5f);
                    }

                    user.heal(amountOfHeal * 2);
                    katana.subtractBlood(2400);
                }
                return TypedActionResult.success(stack);
            }
        }
        if (EnchantmentHelper.getLevel(ModEnchantments.BLOCKING_ENCHANTMENT, stack) <= 0){
            return TypedActionResult.fail(stack);
        }
        if (!world.isClient()){
            user.setCurrentHand(hand);
            user.setSprinting(false);
            katana.setBlocking(true);
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient()){
            if (user instanceof PlayerEntity player){
                KatanaComponent.get(player).setBlocking(false);
            }
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return state.isOf(Blocks.COBWEB);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.kegantu-armory.amethyst_katana").formatted(Formatting.DARK_GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }

    public void spawnSweepAttackParticles(World world, PlayerEntity player) {
        double d = (-MathHelper.sin(player.getYaw() * (float) (Math.PI / 180.0)));
        double e = (double)MathHelper.cos(player.getYaw()) * (float) (Math.PI / 180.0);
        if (world instanceof ServerWorld) {
            ((ServerWorld)world).spawnParticles(ModParticles.PURPLE_SWEEP_ATTACK, player.getX() + d, player.getBodyY(0.5), player.getZ() + e, 0, d, 0.0, e, 0.0);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return false;
    }

    private boolean hasCorrectArmorOn(ArmorMaterial armorMaterial, PlayerEntity player){
        for (ItemStack stack: player.getInventory().armor) {
            if (!(stack.getItem() instanceof ArmorItem)){
                return false;
            } else if (stack.isEmpty()) {
                return true;
            }
        }

        ArmorItem helmet = (ArmorItem) player.getInventory().getArmorStack(3).getItem();
        ArmorItem chestPlate = (ArmorItem) player.getInventory().getArmorStack(2).getItem();
        ArmorItem leggings = (ArmorItem) player.getInventory().getArmorStack(1).getItem();
        ArmorItem boots = (ArmorItem) player.getInventory().getArmorStack(0).getItem();

        return helmet.getMaterial() == armorMaterial || chestPlate.getMaterial() == armorMaterial
                || leggings.getMaterial() == armorMaterial
                || boots.getMaterial() == armorMaterial;
    }
}