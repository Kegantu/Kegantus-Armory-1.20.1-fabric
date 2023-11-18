package me.kegantu.kegantu_armory.enchantment;

import me.kegantu.kegantu_armory.init.ModEnchantments;
import me.kegantu.kegantu_armory.init.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class DashEnchantment extends Enchantment {
    public DashEnchantment(Rarity weight) {
        super(weight, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public int getMaxPower(int level) {
        return 1;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isOf(ModItems.AMETHYST_KATANA) && EnchantmentHelper.getLevel(ModEnchantments.BLOCKING_ENCHANTMENT, stack) > 0;
    }
}