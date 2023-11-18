package me.kegantu.kegantu_armory.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AmethystTemperedShard extends Item {
    public AmethystTemperedShard(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.kegantu-armory.amethyst_tempered_shard").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("tooltip.kegantu-armory.amethyst_tempered_shard.chance").formatted(Formatting.DARK_PURPLE));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
