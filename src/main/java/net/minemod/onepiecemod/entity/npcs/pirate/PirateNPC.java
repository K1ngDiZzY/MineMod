package net.minemod.onepiecemod.entity.npcs.pirate;

import net.minecraft.Util;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minemod.onepiecemod.client.pirate.PirateVariant;
import org.jetbrains.annotations.Nullable;

public class PirateNPC extends AbstractPirateNPC {

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(PirateNPC.class, EntityDataSerializers.INT);

    public PirateNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    /**
     * createAttributes()
     * This method is where we assign custom attributes to the NPCs (such as Speed, Health, Devil Fruit Effects, etc.)
     * @return custom attributes for the NPC that extends AbstractNPC
     */
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0D) //  Default is 20 HP. (Set to 2 HP for testing)
                .add(Attributes.MOVEMENT_SPEED, 0.125D); // Test walking speed
    }

    /* VARIANT */
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
    }

    private int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public PirateVariant getVariant() {
        return PirateVariant.byId(this.entityData.get(VARIANT));
    }

    private void setVariant(PirateVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput pOutput) {
        super.addAdditionalSaveData(pOutput);
        pOutput.putInt("Variant", this.getTypeVariant());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput pInput) {
        super.readAdditionalSaveData(pInput);
        pInput.getInt("Variant").ifPresent(value -> this.entityData.set(VARIANT, value));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, EntitySpawnReason pSpawnReason, @Nullable SpawnGroupData pSpawnGroupData) {
        PirateVariant variant = Util.getRandom(PirateVariant.values(), this.random);
        this.setVariant(variant);
        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnReason, pSpawnGroupData);
    }
}
