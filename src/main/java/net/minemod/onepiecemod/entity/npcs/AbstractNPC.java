package net.minemod.onepiecemod.entity.npcs;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

import javax.annotation.Nullable;

/**
 * This Class is the Parent Class to all NPCs.
 * - Any global behavior should go here. Implements "PathfinderMob" to allow pathfinding for NPCs.
 * - (Example: Devil Fruit checks, Inventory declaration, Trade/Bribe/Pickpocket mechanics, etc.)
 */
public abstract class AbstractNPC extends PathfinderMob {

    /** Variables */
    protected static final int DEFAULT_INVENTORY_SIZE = 9;
    protected SimpleContainer inventory;

    /** Constructor */
    public AbstractNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
        this.inventory = new SimpleContainer(this.getInventorySize());
    }

    /**
     * registerGoals()
     * - Goals that will be applied globally to all NPCs.
     * (Example: Devil Fruit behavior, NPC head-movement, etc.)
     */
    @Override
    protected void registerGoals() {
        // Movement and idle behavior
        // If Devil Fruit, disable FloatGoal (upcoming Feature)
        this.goalSelector.addGoal(1, new FloatGoal(this)); // Allows swimming

        // If Devil Fruit, enable WaterAvoidingRandomStrollGoal (upcoming Feature)
        //this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D)); // Wanders around and avoids water

        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Looks at nearby players
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this)); // Idle head movement
    }

    /** Inventory Methods
     * -
     * Defines the inventory capacity for this NPC.
     * Override this in child classes (e.g., AbstractNavyNPC or PirateMerchantNPC)
     * to give specific mobs larger or smaller inventories.
     */
    public int getInventorySize() {
        return DEFAULT_INVENTORY_SIZE;
    }

    /**
     * Getter for the NPC's inventory container.
     * Useful for Pickpocketable, Tradeable, or Bribable interactions.
     */
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    /**
     * NBT Persistence - Saving of NPC Inventories
     */
    @Override
    protected void addAdditionalSaveData(ValueOutput pOutput) {
        super.addAdditionalSaveData(pOutput);
        // Let pOutput create/handle the typed list safely
        this.inventory.storeAsItemList(pOutput.list("Items", ItemStack.CODEC));
    }

    /**
     * NBT Persistence - Loading of NPC Inventories
     */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        // Use .ifPresent(...) because input.list(...) returns Optional
        input.list("Items", ItemStack.CODEC).ifPresent(this.inventory::fromItemList);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);

        if (this.inventory != null) {
            this.inventory.removeAllItems().forEach(stack -> this.spawnAtLocation(level, stack));
        }
    }

    /**
     * Called when the mob is spawned in the world (naturally, via spawn egg, or command).
     */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, EntitySpawnReason pSpawnReason, @Nullable SpawnGroupData pSpawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(pLevel, pDifficulty, pSpawnReason, pSpawnGroupData);

        // Only populate items if this is a fresh spawn (not loading from world save NBT)
        this.populateDefaultInventory(pLevel.getRandom());

        return data;
    }

    /**
     * Override this in child classes (e.g., PirateNPC, NavyNPC) to define default inventory items.
     */
    protected void populateDefaultInventory(RandomSource random) {
        // Default base items for ALL NPCs (optional)
        // Example:
        this.getInventory().addItem(new ItemStack(Items.BREAD, 2));
    }

    /**
     * isPushable()
     * - Abstract NPC will move when nudged by the Player.
     */
    @Override
    public boolean isPushable() {
        return true;
    }
}
