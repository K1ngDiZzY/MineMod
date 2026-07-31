package net.minemod.onepiecemod.entity.npcs.neutral.navy;

import net.minecraft.Util;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minemod.onepiecemod.client.navy.NavyVariant;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a Navy NPC. (Should eventually change to NavyGruntNPC to allow for more individualization.)
 * - (Low level NPC belonging to the "Navy" NPC group.)
 */
public class NavyNPC extends AbstractNavyNPC {

    /** Variables */
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(NavyNPC.class, EntityDataSerializers.INT);

    /** Constructor */
    public NavyNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    /**
     * createAttributes()
     * - This method builds the attributes for a "Navy" NPC. (Has low HP, and low Speed values for testing.)
     * - TODO: change attributes to normal levels (not test levels).
     * @return custom attributes for the NPC that extends AbstractNPC
     */
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0D) // default 20 HP (Set to 2 for testing)
                .add(Attributes.MOVEMENT_SPEED, 0.25D) // Test walking speed
                .add(Attributes.FOLLOW_RANGE, 16.0D)    // Distance NPCs will track down its target
                .add(Attributes.ATTACK_DAMAGE, 4.0D);   // Attack Damage
    }

    /**
     * These methods define the "Variant" of the Navy NPC that is spawned.
     * - (TODO: Should eventually be abstracted out to AbstractNavyNPC...)
     * - defineSynchedData()
     * - getTypeVariant()
     * - getVariant()
     * - setVariant()
     * - addAdditionalSaveData()
     * - readAdditionalSaveData()
     * @param builder The data builder used to register network-synced entity parameters.
     */
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
    }

    private int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public NavyVariant getVariant()
    {
        return NavyVariant.byId(this.entityData.get(VARIANT));
    }

    private void setVariant(NavyVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    /**
     * Saves custom data to the NBT tag compound, including entity variants
     * and active NeutralMob persistent anger states.
     */
    @Override
    protected void addAdditionalSaveData(ValueOutput pOutput) {
        super.addAdditionalSaveData(pOutput);
        pOutput.putInt("Variant", this.getTypeVariant());
        this.addPersistentAngerSaveData(pOutput);
    }

    /**
     * Reads custom data from the saved NBT tag compound to restore variants
     * and persistent anger states upon entity load.
     */
    @Override
    protected void readAdditionalSaveData(ValueInput pInput) {
        super.readAdditionalSaveData(pInput);
        pInput.getInt("Variant").ifPresent(value -> this.entityData.set(VARIANT, value));

        if(this.level() instanceof ServerLevel serverLevel) {
            this.readPersistentAngerSaveData(serverLevel, pInput);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, EntitySpawnReason pSpawnReason, @Nullable SpawnGroupData pSpawnGroupData) {
        NavyVariant variant = Util.getRandom(NavyVariant.values(), this.random);
        this.setVariant(variant);
        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnReason, pSpawnGroupData);
    }
}
