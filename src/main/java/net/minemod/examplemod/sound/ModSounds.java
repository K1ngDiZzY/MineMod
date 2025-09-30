package net.minemod.examplemod.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.examplemod.ExampleMod;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ExampleMod.MODID);

    public static final RegistryObject<SoundEvent> GRUNT_AMBIENT = registerSoundEvent("grunt_noise");
    public static final RegistryObject<SoundEvent> GRUNT_HURT = registerSoundEvent("grunt_cheer");
    public static final RegistryObject<SoundEvent> GRUNT_DEATH = registerSoundEvent("grunt_hurt");
    public static final RegistryObject<SoundEvent> GRUNT_WALK = registerSoundEvent("grunt_death");


    public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, name)));
    }

    public static void register(FMLJavaModLoadingContext context) {
        SOUND_EVENTS.register(context.getModBusGroup());
    }
}
