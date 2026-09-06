package net.minemod.onepiecemod.entity.settings;

public class DifficultyBuilder {
    // Create Variables with Default values

    // - Bribeable

    // TODO
    // - Default Pickpocketable Difficulty Settings.
    public float pickpocketChance = 0.25f;
    public boolean requiresSkillCheck = true;
    public int skillCheckMaxTicks = 100;
    public int skillCheckKeyCount = 6;


    // Builder Methods

    // - BribeDifficulty

    // TODO

    /**
     * pickpocket()
     * - Builder Method to handle an NPCs Pickpocket Settings
     * - (Pickpocket Chance, Tick count, Key Count, and whether a Skill Check is needed or not).
     */
    public DifficultyBuilder pickpocket(float chance, boolean skillCheck, int maxTicks, int keyCount) {
        this.pickpocketChance = chance;
        this.requiresSkillCheck = skillCheck;
        this.skillCheckMaxTicks = maxTicks;
        this.skillCheckKeyCount = keyCount;
        return this;
    }
}
