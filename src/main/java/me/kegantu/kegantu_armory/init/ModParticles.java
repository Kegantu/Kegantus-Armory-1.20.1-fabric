package me.kegantu.kegantu_armory.init;

import me.kegantu.kegantu_armory.KegantuArmory;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface ModParticles {
    DefaultParticleType PURPLE_SWEEP_ATTACK = FabricParticleTypes.simple();

    static void initialize() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(KegantuArmory.MOD_ID, "purple_sweep_attack"), PURPLE_SWEEP_ATTACK);
    }
}
