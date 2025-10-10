package net.minemod.onepiecemod.entity.npcs.pirate;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import org.jetbrains.annotations.Nullable;

public class PirateNPC extends AbstractPirateNPC {
    private static final EntityDataAccessor<Integer> SKIN_VARIANT =
            SynchedEntityData.defineId(PirateNPC.class, EntityDataSerializers.INT);

    public PirateNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(SKIN_VARIANT, 0);
    }

    // Pick a random skin on spawn
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level,
                                        DifficultyInstance difficulty,
                                        EntitySpawnReason reason,
                                        @Nullable SpawnGroupData spawnData) {
        this.getEntityData().set(SKIN_VARIANT, this.random.nextInt(5)); // pick random variant
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    /*
    // Save/load so the variant persists across world reloads
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SkinVariant", this.getSkinVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SkinVariant")) {
            this.getEntityData().set(SKIN_VARIANT, tag.getInt("SkinVariant"));
        }
    }
    */

    public int getSkinVariant() {
        return this.getEntityData().get(SKIN_VARIANT);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D) // default 20 HP
                .add(Attributes.MOVEMENT_SPEED, 0.50D); // 2x default walking speed
    }
}
