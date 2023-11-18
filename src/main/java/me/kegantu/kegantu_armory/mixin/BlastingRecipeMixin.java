package me.kegantu.kegantu_armory.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlastingRecipe.class)
public abstract class BlastingRecipeMixin extends AbstractCookingRecipe {
    public BlastingRecipeMixin(RecipeType<?> type, Identifier id, String group, CookingRecipeCategory category, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(type, id, group, category, input, output, experience, cookTime);
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        if (!inventory.getStack(0).isOf(Items.AMETHYST_SHARD)){
            return this.input.test(inventory.getStack(0));
        }
        return this.input.test(inventory.getStack(0)) && inventory.getStack(0).getCount() >= 9;
    }
}
