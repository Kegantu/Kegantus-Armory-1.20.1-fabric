package me.kegantu.kegantu_armory.client;

import me.kegantu.kegantu_armory.KegantuArmory;
import me.kegantu.kegantu_armory.client.screen.ParryScreen;
import me.kegantu.kegantu_armory.init.ModItems;
import me.kegantu.kegantu_armory.init.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.particle.SweepAttackParticle;

@Environment(EnvType.CLIENT)
public class KegantuArmoryClient implements ClientModInitializer {
	public static int parryTicks = 0;

	@Override
	public void onInitializeClient() {
		ParticleFactoryRegistry.getInstance().register(ModParticles.PURPLE_SWEEP_ATTACK, SweepAttackParticle.Factory::new);
		ModelPredicateProviderRegistry.register(ModItems.AMETHYST_KATANA, KegantuArmory.id("blocking"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (parryTicks > 0){
				parryTicks--;

				boolean bl = MinecraftClient.getInstance().isIntegratedServerRunning() && !MinecraftClient.getInstance().getServer().isRemote();
				if (bl) {
					MinecraftClient.getInstance().setScreen(new ParryScreen());
				}
			} else if (parryTicks == 0) {
				MinecraftClient.getInstance().setScreen(null);
				parryTicks = -1;
			}
		});
	}
}