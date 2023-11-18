package me.kegantu.kegantu_armory.enchantment;

import me.kegantu.kegantu_armory.init.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class BleedingEnchantment extends Enchantment {
    public BleedingEnchantment(Rarity weight) {
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
        return stack.isOf(ModItems.AMETHYST_KATANA);
    }
}
