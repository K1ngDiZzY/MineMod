package net.minemod.onepiecemod.entity.npcs.neutral.navy;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minemod.onepiecemod.datagen.ModLootProvider;
import net.minemod.onepiecemod.entity.interfaces.Bribable;
import net.minemod.onepiecemod.entity.interfaces.Pickpocketable;
import net.minemod.onepiecemod.entity.npcs.neutral.AbstractNeutralNPC;
import net.minemod.onepiecemod.entity.npcs.neutral.pirate.PirateNPC;
import net.minemod.onepiecemod.entity.settings.DifficultyBuilder;

public abstract class AbstractNavyNPC extends AbstractNeutralNPC implements Bribable, Pickpocketable {
    /** Variables */
    private final DifficultyBuilder difficulty;

    private BribeState bribeState = BribeState.CAN_BRIBE;
    private static final EntityDataAccessor<Byte> PICKPOCKET_STATE =
            SynchedEntityData.defineId(AbstractNavyNPC.class, EntityDataSerializers.BYTE);

    /** Constructors */
    // This constructor uses Custom Difficulty Settings
    public AbstractNavyNPC(EntityType<? extends PathfinderMob> type, Level level, DifficultyBuilder difficulty) {
        super(type, level);
        this.difficulty = difficulty;
    }

    // This constructor uses Default Difficulty Settings
    public AbstractNavyNPC(EntityType<? extends PathfinderMob> type, Level level)
    {
        this(type, level, new DifficultyBuilder());
    }

    @Override
    public ResourceKey<LootTable> getNPCInventoryLootTable(){
        return ModLootProvider.Gameplay.NAVY_INVENTORY;
    }

    /**
     * registerGoals()
     * - Goals that will be applied globally to all Navy NPCs.
     * - (Example: Attack PirateNPCs on sight, Bribable with Emeralds, Arrest PirateNPCs, etc.)
     */
    @Override
    protected void registerGoals() {
        // Pull shared goals from AbstractNPC
        super.registerGoals();

        // Targeting logic
        // AlertOthers makes nearby NavyNPCs angry at whatever hit this mob.
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers()); // Retaliates when attacked

        // Makes NavyNPCs attack PirateNPCs in its vicinity (if "persistentAngerTarget" isn't another entity)
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PirateNPC.class, 10, true, false, null));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    // Bribe Methods
    @Override public float getBribeChance() { return difficulty.bribeChance; }
    @Override public Item getBribeItem() { return difficulty.bribeItem; }
    @Override public int getBribeCost() { return difficulty.bribeCost; }

    // Pickpocket Methods
    @Override public float getPickpocketChance() { return difficulty.pickpocketChance; }
    @Override public boolean requiresSkillCheck() { return difficulty.requiresSkillCheck; }
    @Override public int getSkillCheckMaxTicks() { return difficulty.skillCheckMaxTicks; }
    @Override public int getSkillCheckKeyCount() { return difficulty.skillCheckKeyCount; }

    // Skill Check Key Pool
    // TODO:

    /** This method returns a boolean to signify if the current NPC is Bribable. */
    /** TODO: Change this to check Bribable state a different way instead of Hostility target. */
    @Override
    public boolean isBribable(Player player) {
        // Navy NPCs only allow bribing if currently hostile towards this player
        if (this.level() instanceof ServerLevel serverLevel) {
            return this.isAngryAt(player, serverLevel);
        }
        return false;
    }

    @Override
    public BribeState getBribeState(Player player) {
        return this.bribeState;
    }
    @Override
    public void setBribeState(BribeState state) {
        this.bribeState = state;
    }

    @Override
    public PickpocketState getPickpocketState(Player player) {
        byte ordinal = this.entityData.get(PICKPOCKET_STATE);
        return PickpocketState.values()[ordinal];
    }
    @Override
    public void setPickpocketState(PickpocketState state) {
        this.entityData.set(PICKPOCKET_STATE, (byte) state.ordinal());
    }

    // Register key in defineSynchedData
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PICKPOCKET_STATE, (byte) PickpocketState.CAN_PICKPOCKET.ordinal());
    }

    /**
     * Saves custom data to the NBT tag compound, including entity variants
     * and active NeutralMob persistent anger states.
     */
    @Override
    protected void addAdditionalSaveData(ValueOutput pOutput) {
        super.addAdditionalSaveData(pOutput);
        pOutput.putString("BribeState", this.bribeState.name());
    }

    /**
     * Reads custom data from the saved NBT tag compound to restore variants
     * and persistent anger states upon entity load.
     */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getString("BribeState").ifPresent(val -> this.bribeState = BribeState.valueOf(val));
    }

    /**
     * mobInteract()
     * - Handles player interactions.
     * - (Handling events for: Bribe Interaction, Trade Interaction, Pickpocket Interaction, etc
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // 1. Process Bribe attempt
        if ((this.getBribeState(player) != BribeState.FAILED_PERMANENT) && player.getItemInHand(hand).is(this.getBribeItem())) {
            InteractionResult bribeResult = this.processBribe(this, player, hand);
            if (bribeResult.consumesAction()) {
                return bribeResult;
            }
        }

        // 2. Process Pickpocket attempt
        if ((this.getPickpocketState(player) != PickpocketState.FAILED_PERMANENT) && player.isCrouching()) {
            return this.processPickpocket(this, player, hand);
        }

        // 3. Add future interactions here cleanly (e.g. Trading, etc)

        return super.mobInteract(player, hand);
    }

}
