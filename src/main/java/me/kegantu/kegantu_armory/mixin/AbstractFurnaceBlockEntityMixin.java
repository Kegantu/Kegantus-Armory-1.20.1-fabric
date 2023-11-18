package me.kegantu.kegantu_armory.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

    @Shadow
    protected static boolean canAcceptRecipeOutput(DynamicRegistryManager registryManager, @Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        return false;
    }

    @Inject(method = "craftRecipe", at = @At("HEAD"), cancellable = true)
    private static void chanceToSmelt(DynamicRegistryManager registryManager, @Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> cir){
        if (recipe != null && canAcceptRecipeOutput(registryManager, recipe, slots, count)){
            ItemStack itemStack = slots.get(0);
            ItemStack stack = slots.get(2);
            ItemStack itemStack2 = recipe.getOutput(registryManager);
            if (itemStack.isOf(Items.AMETHYST_SHARD) && itemStack.getCount() >= 9){
                Random random = new Random();
                float randomChance = random.nextFloat();

                if (randomChance > 0.2f){
                    if (stack.isEmpty()) {
                        slots.set(2, itemStack2.copy());
                    }
                    cir.cancel();
                }
                itemStack.decrement(9);
            }
        }
    }
}
