package me.kegantu.kegantu_armory.init;

import me.kegantu.kegantu_armory.KegantuArmory;
import me.kegantu.kegantu_armory.statuseffect.BleedingStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ModStatusEffects {
    Map<StatusEffect, Identifier> STATUS_EFFECTS = new LinkedHashMap<>();

    StatusEffect BLEEDING = createStatusEffect("bleeding", new BleedingStatusEffect());

    private static <T extends StatusEffect> T createStatusEffect(String name, T statusEffect) {
        STATUS_EFFECTS.put(statusEffect, new Identifier(KegantuArmory.MOD_ID, name));
        return statusEffect;
    }

    static void initialize() {
        STATUS_EFFECTS.keySet().forEach(statusEffect -> {
            Registry.register(Registries.STATUS_EFFECT, STATUS_EFFECTS.get(statusEffect), statusEffect);
        });
    }
}
