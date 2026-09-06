package net.minemod.onepiecemod.entity.settings;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minemod.onepiecemod.entity.interfaces.Pickpocketable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

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

    // - Default Skill Check Key Pool ("W", "A", "S", and "D")
    public List<Pickpocketable.SkillCheckKey> skillCheckKeyPool = List.of(
            new Pickpocketable.SkillCheckKey(GLFW.GLFW_KEY_W, "W"),
            new Pickpocketable.SkillCheckKey(GLFW.GLFW_KEY_A, "A"),
            new Pickpocketable.SkillCheckKey(GLFW.GLFW_KEY_S, "S"),
            new Pickpocketable.SkillCheckKey(GLFW.GLFW_KEY_D, "D")
    );

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
        // Pickpocket Variables
        this.pickpocketChance = pickpocketChance;
        this.requiresSkillCheck = skillCheck;
        this.skillCheckMaxTicks = maxTicks;
        this.skillCheckKeyCount = keyCount;
        return this;
    }

    /**
     * keyPool()
     * - Builder Method to handle an NPC's customizable Skill Check Key Pool from a string of characters.
     * - Converts character inputs (e.g. "WASD" or "QTE") into appropriate GLFW key codes and labels.
     */
    public DifficultyBuilder keyPool(String sequence) {
        if (sequence == null || sequence.isEmpty()) {
            return this;
        }

        List<Pickpocketable.SkillCheckKey> newPool = new ArrayList<>();

        for (char c : sequence.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                // GLFW key codes for A-Z range sequentially from GLFW_KEY_A to GLFW_KEY_Z
                int keyCode = GLFW.GLFW_KEY_A + (c - 'A');
                String label = String.valueOf(c);

                // Prevent adding duplicate keys to the key pool
                if (newPool.stream().noneMatch(k -> k.keyCode() == keyCode)) {
                    newPool.add(new Pickpocketable.SkillCheckKey(keyCode, label));
                }
            } else if (c >= '0' && c <= '9') {
                // GLFW key codes for 0-9 range sequentially from GLFW_KEY_0 to GLFW_KEY_9
                int keyCode = GLFW.GLFW_KEY_0 + (c - '0');
                String label = String.valueOf(c);

                if (newPool.stream().noneMatch(k -> k.keyCode() == keyCode)) {
                    newPool.add(new Pickpocketable.SkillCheckKey(keyCode, label));
                }
            }
        }

        if (!newPool.isEmpty()) {
            this.skillCheckKeyPool = List.copyOf(newPool);
        }

        return this;
    }
}
