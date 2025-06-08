package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IAction;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public final class ImageButton extends Button {
    private ResourceLocation textureLoc;
    private final int hoverYOffset, uOffset, vOffset, textureWidth, textureHeight;

    public void setTexture(ResourceLocation textureLoc) {
        this.textureLoc = textureLoc;
    }

    //for sprite sheets with multiple images/icons, supports hover states
    public ImageButton(int x, int y, int width, int height, int uOffset, int vOffset, int textureWidth, int textureHeight, int hoverYOffset,
                       ResourceLocation textureLoc, IAction leftClickAction, IAction rightClickAction) {
        super(x, y, width, height, "", FontSize.S, leftClickAction, rightClickAction, ButtonType.DEFAULT);

        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureLoc = textureLoc;
        this.hoverYOffset = hoverYOffset;
    }

    public ImageButton(int x, int y, int width, int height, int uOffset, int vOffset, int textureWidth, int textureHeight, int hoverYOffset,
                       ResourceLocation textureLoc, IAction leftClickAction) {
       this(x, y, width, height, uOffset, vOffset, textureWidth, textureHeight, hoverYOffset, textureLoc, leftClickAction, null);
    }

    //for single image
    public ImageButton(int x, int y, int width, int height, ResourceLocation textureLoc, IAction leftClickAction, IAction rightClickAction) {
        this(x, y, width, height, 0, 0, width, height, 0, textureLoc, leftClickAction, rightClickAction);
    }

    public ImageButton(int x, int y, int width, int height, ResourceLocation textureLoc, IAction leftClickAction) {
        this(x, y, width, height, 0, 0, width, height, 0, textureLoc, leftClickAction, null);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(this.isHidden()){
            return;
        }

        if(this.textureLoc != null){
            super.render(poseStack);

            RenderSystem.setShaderTexture(0, this.textureLoc);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            if(this.isMouseOver() && this.isEnabled())
                AbstractContainerScreen.blit(poseStack, this.getLeftPos(), this.getTopPos(), this.uOffset, this.hoverYOffset,
                        this.getWidth(), getHeight(), this.textureWidth, this.textureHeight);
            else
                AbstractContainerScreen.blit(poseStack, this.getLeftPos(), this.getTopPos(), this.uOffset, this.vOffset,
                        this.getWidth(), getHeight(), this.textureWidth, this.textureHeight);
        }
    }
}
