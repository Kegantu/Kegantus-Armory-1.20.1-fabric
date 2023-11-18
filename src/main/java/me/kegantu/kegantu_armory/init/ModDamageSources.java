package me.kegantu.kegantu_armory.init;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public interface ModDamageSources {
    RegistryKey<DamageType> BLEEDING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("kegantu-armory", "bleeding_damage_type"));

    static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }
}
