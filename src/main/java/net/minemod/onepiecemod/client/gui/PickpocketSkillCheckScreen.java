package net.minemod.onepiecemod.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.PathfinderMob;
import net.minemod.onepiecemod.network.PacketHandler;
import net.minemod.onepiecemod.network.SkillCheckResultPacket;

import java.util.List;

public class PickpocketSkillCheckScreen extends Screen {
    private final int mobEntityId;
    private final List<Integer> requiredKeys;
    private final List<String> keyLabels;
    private int currentStep = 0;

    private final int maxTicks = 100; // Expanded to 5 seconds so you have time to see it
    private int ticksRemaining = maxTicks;

    public PickpocketSkillCheckScreen(PathfinderMob mob, List<Integer> requiredKeys, List<String> keyLabels) {
        super(Component.literal("Pickpocket Skill Check"));
        this.mobEntityId = mob.getId();
        this.requiredKeys = requiredKeys;
        this.keyLabels = keyLabels;
    }

    @Override
    public void tick() {
        super.tick();
        ticksRemaining--;
        if (ticksRemaining <= 0) {
            finishSkillCheck(false);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (currentStep < requiredKeys.size()) {
            if (keyCode == requiredKeys.get(currentStep)) {
                currentStep++;
                if (currentStep >= requiredKeys.size()) {
                    finishSkillCheck(true);
                }
                return true;
            } else {
                finishSkillCheck(false);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void finishSkillCheck(boolean success) {
        PacketHandler.sendToServer(new SkillCheckResultPacket(mobEntityId, success));
        this.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 1. Fill translucent background (50% black overlay)
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // 2. Render Header (Using Full ARGB Alpha 0xFFFFFFFF)
        guiGraphics.drawCenteredString(this.font, "SKILL CHECK: Press Keys In Order!", centerX, centerY - 40, 0xFFFFFFFF);

        // 3. Build Sequence Display
        if (keyLabels != null && !keyLabels.isEmpty()) {
            StringBuilder sequenceDisplay = new StringBuilder();
            for (int i = 0; i < keyLabels.size(); i++) {
                if (i < currentStep) {
                    sequenceDisplay.append(" ✔ ");
                } else {
                    sequenceDisplay.append(" ").append(keyLabels.get(i)).append(" ");
                }
            }
            // Draw sequence with full ARGB yellow (0xFFFFFF00)
            guiGraphics.drawCenteredString(this.font, sequenceDisplay.toString(), centerX, centerY, 0xFFFFFF00);
        } else {
            // Fallback warning if list is empty
            guiGraphics.drawCenteredString(this.font, "ERROR: NO KEYS GENERATED", centerX, centerY, 0xFFFF0000);
        }

        // 4. Render Timer with full ARGB red (0xFFFF5555)
        float remainingSeconds = Math.max(0, ticksRemaining / 20.0f);
        String timeString = String.format("Time: %.1fs", remainingSeconds);
        guiGraphics.drawCenteredString(this.font, timeString, centerX, centerY + 30, 0xFFFF5555);

        // 5. Call super at the end to render standard components
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}