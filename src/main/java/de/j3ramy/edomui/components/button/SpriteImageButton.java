package de.j3ramy.edomui.components.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IAction;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public final class SpriteImageButton extends Button {
    private final ResourceLocation texture;
    private final int uOffset, vOffset, hoverYOffset, textureWidth, textureHeight;

    public SpriteImageButton(int x, int y, int width, int height, int u, int v, int hoverV, int texW, int texH, ResourceLocation texture, IAction onClick) {
        super(x, y, width, height, "", FontSize.S, onClick);
        this.texture = texture;
        this.uOffset = u;
        this.vOffset = v;
        this.hoverYOffset = hoverV;
        this.textureWidth = texW;
        this.textureHeight = texH;
    }

    public SpriteImageButton(int x, int y, int width, int height, int u, int v, int hoverV, ResourceLocation texture, IAction onClick) {
        this(x, y, width, height, u, v, hoverV, width, height, texture, onClick);
    }

    public SpriteImageButton(int x, int y, int width, int height, int u, int v, int texW, int texH, ResourceLocation texture, IAction onClick) {
        this(x, y, width, height, u, v, v, texW, texH, texture, onClick);
    }

    public SpriteImageButton(int x, int y, int width, int height, ResourceLocation texture, IAction onClick) {
        this(x, y, width, height, 0, 0, 0, width, height, texture, onClick);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);
        RenderSystem.setShaderTexture(0, texture);

        int v = (isMouseOver() && isEnabled()) ? hoverYOffset : vOffset;
        AbstractContainerScreen.blit(poseStack, getLeftPos(), getTopPos(), uOffset, v, getWidth(), getHeight(), textureWidth, textureHeight);
    }
}

