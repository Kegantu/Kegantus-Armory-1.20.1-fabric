package me.kegantu.kegantu_armory.init;

import me.kegantu.kegantu_armory.KegantuArmory;
import me.kegantu.kegantu_armory.item.AmethystAlloySmithingTemplate;
import me.kegantu.kegantu_armory.item.AmethystTemperedShard;
import me.kegantu.kegantu_armory.item.Katana;
import me.kegantu.kegantu_armory.item.material.AmethystAlloy;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ModItems {

    Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    Item AMETHYST_KATANA = createItem("amethyst_katana", new Katana(AmethystAlloy.AmethystAlloy, 8, -2.5f, new FabricItemSettings().rarity(Rarity.RARE).fireproof()));
    Item AMETHYST_ALLOY = createItem("amethyst_alloy", new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    Item AMETHYST_TEMPERED_SHARD = createItem("amethyst_tempered_shard", new AmethystTemperedShard(new FabricItemSettings().rarity(Rarity.UNCOMMON).fireproof()));
    Item AMETHYST_ALLOY_UPGRADE_SMITHING_TEMPLATE = createItem("amethyst_alloy_upgrade_smithing_template", AmethystAlloySmithingTemplate.createAmethystAlloyUpgrade());
    Item LAYERS_OF_AMETHYST_TEMPERED_SHARD = createItem("layers_of_amethyst_tempered_shard", new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).fireproof()));

    private static <T extends Item> T createItem(String name, T item) {
        ITEMS.put(item, new Identifier(KegantuArmory.MOD_ID, name));
        return item;
    }

    static void initialize() {
        ITEMS.keySet().forEach(item -> {
            Registry.register(Registries.ITEM, ITEMS.get(item), item);
        });
    }
}
