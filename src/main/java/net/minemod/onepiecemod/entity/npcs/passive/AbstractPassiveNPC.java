package net.minemod.onepiecemod.entity.npcs.passive;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minemod.onepiecemod.entity.interfaces.Bribable;
import net.minemod.onepiecemod.entity.interfaces.Pickpocketable;
import net.minemod.onepiecemod.entity.interfaces.Tradable;
import net.minemod.onepiecemod.entity.npcs.AbstractNPC;
import net.minemod.onepiecemod.entity.settings.DifficultyBuilder;
import net.minemod.onepiecemod.entity.trade.Trade;

import java.util.List;


/**
 * This Class is the Parent Class to any NPC Group that will be Passive by default.
 * - Any passive-specific behavior will go here.
 * - (Example: Will never attack, Flees when attacked, etc.)
 */
public abstract class AbstractPassiveNPC extends AbstractNPC implements Tradable {

    /** Variables */
    private final DifficultyBuilder difficulty;


    /** Constructors */
    // This constructor uses Custom Difficulty Settings
    public AbstractPassiveNPC(EntityType<? extends PathfinderMob> type, Level level, DifficultyBuilder difficulty) {
        super(type, level);
        this.difficulty = difficulty;
    }

    // This constructor uses Default Difficulty Settings
    public AbstractPassiveNPC(EntityType<? extends PathfinderMob> type, Level level)
    {
        this(type, level, new DifficultyBuilder());
    }

    /**
     * registerGoals()
     * - Goals that will be applied globally to all PassiveNPCs.
     * - (Example: Randomly walking, Actively avoid certain NPCs, etc.)
     */
    @Override
    protected void registerGoals() {
        // Pull shared goals from AbstractNPC
        super.registerGoals();

        // Movement and idle behavior
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.5D));
    }

    // Trade List - Get Method
    @Override public List<Trade> getTrades() { return this.difficulty.trades; }

    /**
     * mobInteract()
     * - Handles player interactions.
     * - (Handling events for: Bribe Interaction, Trade Interaction, Pickpocket Interaction, etc
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        //  Process Trade Interaction
        if (this.isTradable(player)) {
            return this.processTrade(this, player, hand);
        }
        return super.mobInteract(player, hand);
    }
}
