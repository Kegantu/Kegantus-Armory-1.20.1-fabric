package me.kegantu.kegantu_armory.init;

import me.kegantu.kegantu_armory.KegantuArmory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ModSounds {
    Map<SoundEvent, Identifier> SOUNDS = new LinkedHashMap<>();

    Identifier AMETHYST_KATANA_SOUND_IDENTIFIER = new Identifier(KegantuArmory.MOD_ID, "amethyst_katana_sound");
    Identifier PARRY_SOUND_IDENTIFIER = new Identifier(KegantuArmory.MOD_ID, "parry_sound");
    Identifier DASH_SOUND_IDENTIFIER = new Identifier(KegantuArmory.MOD_ID, "dash_sound");
    Identifier SLICE_SOUND_IDENTIFIER = new Identifier(KegantuArmory.MOD_ID, "slice_sound");

    SoundEvent AMETHYST_KATANA_SOUND_EVENT = createSound(SoundEvent.of(AMETHYST_KATANA_SOUND_IDENTIFIER));
    SoundEvent PARRY_SOUND_EVENT = createSound(SoundEvent.of(PARRY_SOUND_IDENTIFIER));
    SoundEvent DASH_SOUND_EVENT = createSound(SoundEvent.of(DASH_SOUND_IDENTIFIER));
    SoundEvent SLICE_SOUND_EVENT = createSound(SoundEvent.of(SLICE_SOUND_IDENTIFIER));

    private static <T extends SoundEvent> T createSound(T soundEvent) {
        SOUNDS.put(soundEvent, new Identifier(KegantuArmory.MOD_ID, soundEvent.getId().getPath()));
        return soundEvent;
    }

    static void initialize() {
        SOUNDS.keySet().forEach(soundEvent -> {
            Registry.register(Registries.SOUND_EVENT, SOUNDS.get(soundEvent), soundEvent);
        });
    }
}
