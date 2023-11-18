package me.kegantu.kegantu_armory.init;

import me.kegantu.kegantu_armory.KegantuArmory;
import me.kegantu.kegantu_armory.enchantment.BleedingEnchantment;
import me.kegantu.kegantu_armory.enchantment.BlockingEnchantment;
import me.kegantu.kegantu_armory.enchantment.DashEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ModEnchantments {
    Map<Enchantment, Identifier> ENCHANTMENTS = new LinkedHashMap<>();

    Enchantment BLOCKING_ENCHANTMENT = createEnchantment("blocking_enchantment", new BlockingEnchantment(Enchantment.Rarity.UNCOMMON));
    Enchantment DASH_ENCHANTMENT = createEnchantment("dash_enchantment", new DashEnchantment(Enchantment.Rarity.UNCOMMON));
    Enchantment BLEEDING_ENCHANTMENT = createEnchantment("bleeding_enchantment", new BleedingEnchantment(Enchantment.Rarity.UNCOMMON));

    private static <T extends Enchantment> T createEnchantment(String name, T enchantment) {
        ENCHANTMENTS.put(enchantment, new Identifier(KegantuArmory.MOD_ID, name));
        return enchantment;
    }

    static void initialize() {
        ENCHANTMENTS.keySet().forEach(item -> {
            Registry.register(Registries.ENCHANTMENT, ENCHANTMENTS.get(item), item);
        });
    }
}
