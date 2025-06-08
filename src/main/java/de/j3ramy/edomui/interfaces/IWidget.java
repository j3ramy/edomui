package de.j3ramy.edomui.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IWidget {
    void update(int mouseX, int mouseY);
    void tick();
    void render(PoseStack poseStack);
    void onClick(int mouseButton);
    void onScroll(double delta);
    void keyPressed(int keyCode);
    void charTyped(char codePoint);
    void onMouseDrag(int mouseButton, double newX, double newY, int mouseX, int mouseY);
}
