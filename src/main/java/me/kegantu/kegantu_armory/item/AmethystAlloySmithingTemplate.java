package me.kegantu.kegantu_armory.item;

import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public class AmethystAlloySmithingTemplate extends SmithingTemplateItem {

    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
    private static final Text AMETHYST_UPGRADE_TEXT = Text.translatable(Util.createTranslationKey("upgrade", new Identifier("kegantu-armory","amethyst_upgrade")))
            .formatted(TITLE_FORMATTING);
    private static final Text AMETHYST_UPGRADE_APPLIES_TO_TEXT = Text.translatable(
                    Util.createTranslationKey("item", new Identifier("kegantu-armory","smithing_template.amethyst_upgrade.applies_to"))
            )
            .formatted(DESCRIPTION_FORMATTING);
    private static final Text AMETHYST_UPGRADE_INGREDIENTS_TEXT = Text.translatable(
                    Util.createTranslationKey("item", new Identifier("kegantu-armory","smithing_template.amethyst_upgrade.ingredients"))
            )
            .formatted(DESCRIPTION_FORMATTING);
    private static final Text AMETHYST_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", new Identifier("kegantu-armory","smithing_template.amethyst_upgrade.base_slot_description"))
    );
    private static final Text AMETHYST_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", new Identifier("kegantu-armory","smithing_template.amethyst_upgrade.additions_slot_description"))
    );
    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE = new Identifier("item/empty_slot_ingot");
    private static final Identifier EMPTY_SLOT_TEMPERED_SHARD_TEXTURE = new Identifier("kegantu-armory","item/layers_of_amethyst_tempered_shard_empty");

    public AmethystAlloySmithingTemplate(Text appliesToText, Text ingredientsText, Text titleText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures) {
        super(appliesToText, ingredientsText, titleText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures);
    }

    public static SmithingTemplateItem createAmethystAlloyUpgrade() {
        return new SmithingTemplateItem(
                AMETHYST_UPGRADE_APPLIES_TO_TEXT,
                AMETHYST_UPGRADE_INGREDIENTS_TEXT,
                AMETHYST_UPGRADE_TEXT,
                AMETHYST_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT,
                AMETHYST_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
                getAmethystAlloyUpgradeEmptyAdditionsSlotTextures(),
                getAmethystAlloyUpgradeEmptyBaseSlotTextures()
        );
    }

    private static List<Identifier> getAmethystAlloyUpgradeEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_INGOT_TEXTURE);
    }

    private static List<Identifier> getAmethystAlloyUpgradeEmptyBaseSlotTextures() {
        return List.of(EMPTY_SLOT_TEMPERED_SHARD_TEXTURE);
    }
}
