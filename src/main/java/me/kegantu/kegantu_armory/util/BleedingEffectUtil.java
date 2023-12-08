package me.kegantu.kegantu_armory.util;

import me.kegantu.kegantu_armory.init.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class BleedingEffectUtil {

    public static void randomlyApplyBleeding(LivingEntity target, float chance){
        Random random = new Random();
        float randomChance = 0f + random.nextFloat();

        if (randomChance < chance){
            if (target.getStatusEffect(ModStatusEffects.BLEEDING).getAmplifier() >= 2){
                return;
            }

            target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEEDING, 200, target.getStatusEffect(ModStatusEffects.BLEEDING) == null ? 0 : target.getStatusEffect(ModStatusEffects.BLEEDING).getAmplifier() + 1));
        }
    }
}
