package net.minemod.onepiecemod.entity.settings;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * DifficultyBuilder class
 * - This class handles all the Difficulty Settings related to the Interfaces an NPC can implement.
 * - (Bribable, Pickpocketable, Tradable, etc).
 */
public class DifficultyBuilder {
    // Create Variables with Default values

    // - Default Bribe Difficulty Settings
    public float bribeChance = 1.0f;
    public Item bribeItem = Items.DIAMOND;
    public int bribeCost = 1;

    // - Default Pickpocket Difficulty Settings.
    public float pickpocketChance = 0.25f;
    public boolean requiresSkillCheck = true;
    public int skillCheckMaxTicks = 100;
    public int skillCheckKeyCount = 6;


    // Builder Methods
    /**
     * bribe()
     * - Builder Method to handle an NPCs Bribe Settings
     * - (Bribe Chance, Bribe Item, and Bribe Cost).
     */
    public DifficultyBuilder bribe(float bribeChance, Item bribeItem, int bribeCost) {
        this.bribeChance = bribeChance;
        this.bribeItem = bribeItem;
        this.bribeCost = bribeCost;
        return this;
    }

    /**
     * pickpocket()
     * - Builder Method to handle an NPCs Pickpocket Settings
     * - (Pickpocket Chance, Tick count, Key Count, and whether a Skill Check is needed or not).
     */
    public DifficultyBuilder pickpocket(float pickpocketChance, boolean skillCheck, int maxTicks, int keyCount) {
        this.pickpocketChance = pickpocketChance;
        this.requiresSkillCheck = skillCheck;
        this.skillCheckMaxTicks = maxTicks;
        this.skillCheckKeyCount = keyCount;
        return this;
    }
}
