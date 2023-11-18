package me.kegantu.kegantu_armory;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import me.kegantu.kegantu_armory.component.KatanaComponent;
import me.kegantu.kegantu_armory.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KegantuArmory implements ModInitializer, EntityComponentInitializer {

	public static final String MOD_ID = "kegantu-armory";

	private static final ItemGroup KEGANTU_ARMORY_ITEM_GROUP = FabricItemGroup.builder().displayName(Text.translatable("itemGroup.kegantu-armory.kegantu-armory_group")).
			icon(() -> new ItemStack(ModItems.AMETHYST_ALLOY)).entries((displayContext, entries) -> {
				for (Item item : ModItems.ITEMS.keySet()) {
					entries.add(item);
				}
			}).build();

	public static final ComponentKey<KatanaComponent> KATANA =
			ComponentRegistry.getOrCreate(new Identifier("kegantu-armory", "katana"), KatanaComponent.class);

	public static final Identifier DASH_PACKET = new Identifier("kegantu-armory:dash");

	public static final Identifier ANCIENT_CITY_CHESTS = new Identifier("minecraft", "chests/ancient_city");

    public static final Logger LOGGER = LoggerFactory.getLogger("kegantu-armory");

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModSounds.initialize();
		ModParticles.initialize();
		ModEnchantments.initialize();
		ModStatusEffects.initialize();
		Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "kegantu-armory_group"), KEGANTU_ARMORY_ITEM_GROUP);

		ConstantLootNumberProvider lootTableRange = ConstantLootNumberProvider.create(1);
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (ANCIENT_CITY_CHESTS.equals(id)){
				ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
				EnchantedBookItem.addEnchantment(book, new EnchantmentLevelEntry(ModEnchantments.BLOCKING_ENCHANTMENT, 1));
				LootPool lootPool = LootPool.builder()
						.rolls(lootTableRange)
						.conditionally(RandomChanceLootCondition.builder(1f).build())
						.build();
				tableBuilder.pool(lootPool);

				ItemStack book1 = new ItemStack(Items.ENCHANTED_BOOK);
				EnchantedBookItem.addEnchantment(book1, new EnchantmentLevelEntry(ModEnchantments.BLEEDING_ENCHANTMENT, 1));
				LootPool lootPool1 = LootPool.builder()
						.rolls(lootTableRange)
						.conditionally(RandomChanceLootCondition.builder(0.3f).build())
						.build();
				tableBuilder.pool(lootPool1);

				ItemStack book2 = new ItemStack(Items.ENCHANTED_BOOK);
				EnchantedBookItem.addEnchantment(book2, new EnchantmentLevelEntry(ModEnchantments.DASH_ENCHANTMENT, 1));
				LootPool lootPool2 = LootPool.builder()
						.rolls(lootTableRange)
						.conditionally(RandomChanceLootCondition.builder(1f).build())
						.build();
				tableBuilder.pool(lootPool2);

				LootPool lootPool3 = LootPool.builder()
						.rolls(lootTableRange)
						.conditionally(RandomChanceLootCondition.builder(1f).build())
						.with(ItemEntry.builder(ModItems.AMETHYST_ALLOY_UPGRADE_SMITHING_TEMPLATE))
						.build();
				tableBuilder.pool(lootPool3);
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(DASH_PACKET, (server, player, handler, buf, responseSender) -> server.execute(() -> {
			if (player.getMainHandStack().isOf(ModItems.AMETHYST_KATANA)){
				KatanaComponent.get(player).useDash();
			}
		}));
	}

	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(PlayerEntity.class, KATANA).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(KatanaComponent::new);
	}

	public static Identifier id(String name) {
		return new Identifier("kegantu-armory", name);
	}
}