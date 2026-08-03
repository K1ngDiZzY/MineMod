package net.minemod.onepiecemod.entity.npcs.neutral.navy;

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
import net.minemod.onepiecemod.entity.interfaces.Bribable;
import net.minemod.onepiecemod.entity.npcs.neutral.AbstractNeutralNPC;
import net.minemod.onepiecemod.entity.npcs.neutral.pirate.PirateNPC;

public abstract class AbstractNavyNPC extends AbstractNeutralNPC implements Bribable {

    /** Variables */
    private final int DEFAULT_NAVY_BRIBE_COST = 5;
    private final float DEFAULT_NAVY_BRIBE_CHANCE = 0.5f;

    private BribeState bribeState = BribeState.CAN_BRIBE;

    /** Constructor */
    public AbstractNavyNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
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

    /**
     * Bribable Interface methods. Defines PirateNPC Bribe behavior
     * -getBribeItem(): returns the set Bribe Item.
     * -getBribeCost(): returns the number of Bribe Items consumed per Bribe Attempt.
     * -getBribeChance(): returns the chance that a Bribe Attempt will succeed.
     * -isBribable(): returns true if Bribe Attempt can be made, false otherwise.
     * -getBribeState(): returns the Bribe State of the NPC.
     * -setBribeState(): set the Bribe State of the NPC
     */
    @Override
    public Item getBribeItem() {
        return Items.EMERALD;
    }

    @Override
    public int getBribeCost() {
        return DEFAULT_NAVY_BRIBE_COST;
    }

    @Override
    public float getBribeChance() {
        return DEFAULT_NAVY_BRIBE_CHANCE;
    }

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
        if (this.getBribeState(player) != BribeState.FAILED_PERMANENT) {
            InteractionResult bribeResult = this.processBribe(this, player, hand);
            if (bribeResult.consumesAction()) {
                return bribeResult;
            }
        }

        // 2. Add future interactions here cleanly (e.g. Trading, Pickpocketing)

        return super.mobInteract(player, hand);
    }

}
