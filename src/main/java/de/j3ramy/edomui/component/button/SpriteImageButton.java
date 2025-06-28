package de.j3ramy.edomui.component.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IAction;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public final class SpriteImageButton extends Button {
    private final ResourceLocation texture;
    private final int uOffset, vOffset, textureWidth, textureHeight;

    public SpriteImageButton(int x, int y, int width, int height, int u, int v, int texW, int texH, ResourceLocation texture, IAction onClick) {
        super(x, y, width, height, "", onClick, ButtonType.IMAGE);
        this.texture = texture;
        this.uOffset = u;
        this.vOffset = v;
        this.textureWidth = texW;
        this.textureHeight = texH;
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);
        RenderSystem.setShaderTexture(0, texture);

        AbstractContainerScreen.blit(poseStack, getLeftPos(), getTopPos(), uOffset, vOffset, getWidth(), getHeight(), textureWidth, textureHeight);
    }
}

