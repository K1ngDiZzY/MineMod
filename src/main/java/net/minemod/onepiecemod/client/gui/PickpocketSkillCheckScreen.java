package net.minemod.onepiecemod.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PickpocketSkillCheckScreen extends Screen {
    protected PickpocketSkillCheckScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void finishSkillCheck(boolean success) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
