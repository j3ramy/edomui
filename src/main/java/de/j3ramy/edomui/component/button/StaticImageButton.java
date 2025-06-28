package de.j3ramy.edomui.component.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IAction;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public final class StaticImageButton extends Button {
    private final ResourceLocation texture;

    public StaticImageButton(int x, int y, int width, int height, ResourceLocation texture, IAction onClick) {
        super(x, y, width, height, "", onClick, ButtonType.IMAGE);
        this.texture = texture;
    }

    public StaticImageButton(int x, int y, int size, ResourceLocation texture, IAction onClick) {
        this(x, y, size, size, texture, onClick);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);
        RenderSystem.setShaderTexture(0, texture);
        AbstractContainerScreen.blit(poseStack, getLeftPos(), getTopPos(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    }
}
