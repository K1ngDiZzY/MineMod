package net.minemod.onepiecemod.entity.npcs.navy;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minemod.onepiecemod.entity.npcs.pirate.PirateNPC;
import net.minemod.onepiecemod.item.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractNavyNPC extends PathfinderMob implements NeutralMob {

    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20,39);
    private static final AtomicBoolean playerAggroEnabled = new AtomicBoolean(false);

    public AbstractNavyNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    //TODO: Register new goals

    /**
     * registerGoals()
     * This method is where we can select Goals from the minecraft.world.entity.ai.goal library.
     * (Eventually we can create custom Goals that pertain to our mod, such as sink goals, targetNavy/Pirate goals, etc.)
     */
    @Override
    protected void registerGoals() {
        // Combat behavior
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.2D, true)); // Attacks target when in range

        // Movement and idle behavior
        this.goalSelector.addGoal(1, new FloatGoal(this)); // Allows swimming
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D)); // Wanders around
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Looks at nearby players
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this)); // Idle head movement

        // Targeting logic
        // AlertOthers makes nearby NavyNPCs angry at whatever hit this mob.
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers()); // Retaliates when attacked

        // This makes the NPC automatically target whatever player/entity it is angtry at via NeutralMob
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PirateNPC.class, 10, true, false, null));
    }

    /**
     * Custom behavior would go here. This would be inherited by any NPC that extends this class.
     * -
     * isPushable()
     * Abstract NPC will move when nudged by the Player.
     */
    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = pRemainingPersistentAngerTime;
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
      return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {
        this.persistentAngerTarget = pPersistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }



    @Override
    protected void customServerAiStep(ServerLevel pLevel) {
        // Access pLevel directly
        this.updatePersistentAnger(pLevel, true);

        // Pass parameter to super method
        super.customServerAiStep(pLevel);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // Check if we are on the server side so wee can safely cast the level
        if(!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {

            // Use the updated interface check requiring the ServerLevel
            if (this.isAngryAt(player, serverLevel) && itemStack.is(Items.EMERALD)) {

                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                this.stopBeingAngry();
                
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

}
