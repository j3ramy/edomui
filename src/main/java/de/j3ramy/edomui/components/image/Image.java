package de.j3ramy.edomui.components.image;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public final class Image extends Widget {
    private ResourceLocation textureLoc;
    private int uOffset, vOffset, textureWidth, textureHeight;

    public void setTextureLoc(ResourceLocation textureLoc) {
        this.textureLoc = textureLoc;
    }

    public void setUOffset(int uOffset) {
        this.uOffset = uOffset;
    }

    public void setVOffset(int vOffset) {
        this.vOffset = vOffset;
    }

    public void setTextureWidth(int textureWidth) {
        this.textureWidth = textureWidth;
    }

    public void setTextureHeight(int textureHeight) {
        this.textureHeight = textureHeight;
    }

    public Image(int x, int y, int width, int height, ResourceLocation textureLoc){
        this(x, y, width, height, 0, 0, width, height, textureLoc);
    }

    public Image(int x, int y, int width, int height, int uOffset, int vOffset, int textureWidth, int textureHeight, ResourceLocation textureLoc){
        super(x, y, width, height);

        this.textureLoc = textureLoc;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void render(PoseStack matrixStack){
        if(this.isHidden()){
            return;
        }

        if(this.textureLoc != null){
            RenderSystem.setShaderTexture(0, this.textureLoc);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            AbstractContainerScreen.blit(matrixStack, this.getLeftPos(), this.getTopPos(), this.uOffset, this.vOffset,
                    this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
    }
}

