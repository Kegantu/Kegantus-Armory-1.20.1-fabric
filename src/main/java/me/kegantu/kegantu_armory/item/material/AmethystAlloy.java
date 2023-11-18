package me.kegantu.kegantu_armory.item.material;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public enum AmethystAlloy implements ToolMaterial {

    AmethystAlloy(1500, 2.0f, 0f, 18);

    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;

    private AmethystAlloy(int itemDurability, float miningSpeed, float attackDamage, int enchantability) {
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.AMETHYST_SHARD);
    }
}
