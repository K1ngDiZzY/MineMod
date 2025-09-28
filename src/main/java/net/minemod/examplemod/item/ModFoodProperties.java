package net.minemod.examplemod.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class ModFoodProperties {
    public static final Consumable DEVIL_FRUIT_CONSUMABLE = Consumables.defaultFood()
            .soundAfterConsume(SoundEvents.GENERIC_EAT)
            .onConsume(
                    new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3), 1.0f)
            )
            .build();

    public static final FoodProperties DEVIL_FRUIT = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(0.25f)
            .alwaysEdible()
            .build();
}