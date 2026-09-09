package net.minemod.onepiecemod.entity.npcs;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * This Class is the Parent Class to all NPCs.
 * - Any global behavior should go here. Implements "PathfinderMob" to allow pathfinding for NPCs.
 * - (Example: Devil Fruit checks, Inventory declaration, Trade/Bribe/Pickpocket mechanics, etc.)
 */
public abstract class AbstractNPC extends PathfinderMob implements Merchant {

    /** Variables */
    protected static final int DEFAULT_INVENTORY_SIZE = 9;
    protected SimpleContainer inventory;

    @Nullable
    private Player tradingPlayer;
    @Nullable
    protected MerchantOffers offers;

    /** Constructor */
    public AbstractNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
        this.inventory = new SimpleContainer(this.getInventorySize());
    }

    /** Merchant Interface Implementation */

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Override
    public @Nullable Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
        }
        return this.offers; // MUST return this.offers, NOT a new MerchantOffers()!
    }

    @Override
    public void overrideOffers(MerchantOffers offers) {
        this.offers = offers;
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        offer.increaseUses();
        this.playSound(SoundEvents.VILLAGER_YES, this.getSoundVolume(), this.getVoicePitch());
    }

    @Override
    public void notifyTradeUpdated(ItemStack stack) {}

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int xp) {}

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    @Override
    public boolean canRestock() {
        return true;
    }

    @Override
    public boolean isClientSide() {
        return this.level().isClientSide();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.isAlive()
                && this.distanceToSqr(player) <= 64.0D; // 8 blocks distance check
    }

    /**
     * getNPCInventoryLootTable()
     * - Child classes can define their own Loot Pools.
     */
    public abstract ResourceKey<LootTable> getNPCInventoryLootTable();

    // Populate inventory using Minecraft's LootTable system
    public void generateNPCInventory() {
        if (!this.level().isClientSide() && this.getNPCInventoryLootTable() != null) {
            LootTable lootTable = Objects.requireNonNull(this.level().getServer())
                    .reloadableRegistries()
                    .getLootTable(this.getNPCInventoryLootTable());

            LootParams lootParams = new LootParams.Builder((ServerLevel) this.level())
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .create(LootContextParamSets.CHEST); // Uses chest-like generation

            // Fills the SimpleContainer with items from the table
            lootTable.fill(this.inventory, lootParams, this.getRandom().nextLong());
        }
    }

    /**
     * registerGoals()
     * - Goals that will be applied globally to all NPCs.
     * - (Example: Devil Fruit behavior, NPC head-movement, etc.)
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

    /**
     * dropCustomDeathLoot()
     * - This method defines what drops when the NPC dies. By default, entire inventory will drop.
     * - (Eventually, can have certain items that will drop 100% and some that drop at lower chances.)
     * */
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
        // Only populate items if this is a fresh spawn (not loading from world save NBT)
        this.populateDefaultInventory(pLevel.getRandom());
        this.generateNPCInventory();
        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnReason, pSpawnGroupData);
    }

    /**
     * Override this in child classes (e.g., PirateNPC, NavyNPC) to define default inventory items.
     */
    protected void populateDefaultInventory(RandomSource random) {
        // Left blank, so Child NPC classes can Override and implement their own inventories.
        // Example: this.getInventory().addItem(new ItemStack(Items.BREAD, 2));
    }

    /**
     * isPushable()
     * - All NPCs have collision with the Player (will move when nudged by the Player).
     */
    @Override
    public boolean isPushable() {
        return true;
    }
}
