package me.kegantu.kegantu_armory;

import me.kegantu.kegantu_armory.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class KegantuArmoryDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(KegantuArmoryRecipeProvider::new);
	}

	private static class KegantuArmoryRecipeProvider extends FabricRecipeProvider{

		public KegantuArmoryRecipeProvider(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generate(Consumer<RecipeJsonProvider> exporter) {
			ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.AMETHYST_KATANA, 1)
					.pattern(" gl")
					.pattern("iag")
					.pattern("si ")
					.input('s', Items.NETHERITE_SWORD)
					.input('i', Items.IRON_INGOT)
					.input('a', ModItems.AMETHYST_ALLOY)
					.input('l', ModItems.LAYERS_OF_AMETHYST_TEMPERED_SHARD)
					.input('g', ModItems.AMETHYST_TEMPERED_SHARD)
					.criterion(hasItem(Items.NETHERITE_SWORD), conditionsFromItem(Items.NETHERITE_SWORD))
					.criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
					.criterion(hasItem(ModItems.AMETHYST_ALLOY), conditionsFromItem(ModItems.AMETHYST_ALLOY))
					.criterion(hasItem(ModItems.LAYERS_OF_AMETHYST_TEMPERED_SHARD), conditionsFromItem(ModItems.LAYERS_OF_AMETHYST_TEMPERED_SHARD))
					.criterion(hasItem(ModItems.AMETHYST_TEMPERED_SHARD), conditionsFromItem(ModItems.AMETHYST_TEMPERED_SHARD))
					.offerTo(exporter, new Identifier(getRecipeName(ModItems.AMETHYST_KATANA)));

			RecipeProvider.offerBlasting(exporter, List.of(Items.AMETHYST_SHARD), RecipeCategory.MISC, ModItems.AMETHYST_TEMPERED_SHARD, 0.05f, 200, "smelted_shard");
			offerAmethystAlloySmithingTemplateCopyingRecipe(exporter, ModItems.AMETHYST_ALLOY_UPGRADE_SMITHING_TEMPLATE, Items.CALCITE);
			SmithingTransformRecipeJsonBuilder.create(
							Ingredient.ofItems(ModItems.AMETHYST_ALLOY_UPGRADE_SMITHING_TEMPLATE), Ingredient.ofItems(Items.NETHERITE_INGOT), Ingredient.ofItems(ModItems.LAYERS_OF_AMETHYST_TEMPERED_SHARD), RecipeCategory.MISC, ModItems.AMETHYST_ALLOY
					)
					.criterion("has_netherite_ingot", conditionsFromItem(Items.NETHERITE_INGOT))
					.offerTo(exporter, getItemPath(ModItems.AMETHYST_ALLOY) + "_smithing");

			offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.AMETHYST_TEMPERED_SHARD, RecipeCategory.MISC, ModItems.LAYERS_OF_AMETHYST_TEMPERED_SHARD);
		}

		public static void offerAmethystAlloySmithingTemplateCopyingRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible template, ItemConvertible resource) {
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, template, 2)
					.input('#', Items.DIAMOND)
					.input('I', ModItems.AMETHYST_TEMPERED_SHARD)
					.input('C', resource)
					.input('S', template)
					.pattern("ISI")
					.pattern("#C#")
					.pattern("I#I")
					.criterion(hasItem(template), conditionsFromItem(template))
					.offerTo(exporter);
		}
	}
}
