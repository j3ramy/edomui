package de.j3ramy.edomui.component.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.theme.basic.ButtonStyle;
import de.j3ramy.edomui.theme.basic.ImageButtonStyle;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public final class SpriteImageButton extends Button {
    private final ResourceLocation texture;
    private final int uOffset, vOffset, textureWidth, textureHeight, clipSize;
    private final ImageButtonStyle imageButtonStyle;

    public SpriteImageButton(int x, int y, int width, int height, int u, int v, int texW, int texH, int clipSize, ResourceLocation texture, IAction onClick) {
        super(x, y, width, height, "", onClick, ButtonType.IMAGE);
        this.texture = texture;
        this.uOffset = u;
        this.vOffset = v;
        this.textureWidth = texW;
        this.textureHeight = texH;
        this.clipSize = clipSize;

        this.imageButtonStyle = new ImageButtonStyle(ThemeManager.getDefaultImageButtonStyle());
        this.setStyle(imageButtonStyle);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);

        if(this.texture != null) {
            RenderSystem.setShaderTexture(0, texture);
            AbstractContainerScreen.blit(poseStack, getLeftPos(), getTopPos(), uOffset, vOffset, clipSize, clipSize, textureWidth, textureHeight);
        }
    }

    @Override
    public ImageButtonStyle getStyle() {
        return this.imageButtonStyle;
    }
}

