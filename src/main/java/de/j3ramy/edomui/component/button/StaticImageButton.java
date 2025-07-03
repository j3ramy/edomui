package de.j3ramy.edomui.component.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.theme.basic.ImageButtonStyle;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public final class StaticImageButton extends Button {
    private final ResourceLocation texture;
    private final ImageButtonStyle imageButtonStyle;

    public StaticImageButton(int x, int y, int width, int height, ResourceLocation texture, IAction onClick) {
        this(x, y, width, height, texture, onClick, "");
    }

    public StaticImageButton(int x, int y, int width, int height, ResourceLocation texture, IAction onClick, String tooltip) {
        super(x, y, width, height, tooltip, onClick, ButtonType.IMAGE);
        this.texture = texture;

        this.imageButtonStyle = new ImageButtonStyle(ThemeManager.getDefaultImageButtonStyle());
        this.setStyle(imageButtonStyle);

        this.getTitle().setHidden(true);
        this.enableTooltip();
    }

    public StaticImageButton(int x, int y, int size, ResourceLocation texture, IAction onClick) {
        this(x, y, size, size, texture, onClick);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);

        if(this.texture != null) {
            RenderSystem.setShaderTexture(0, texture);
            AbstractContainerScreen.blit(poseStack, getLeftPos(), getTopPos(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
        }

        getTooltip().render(poseStack);
    }

    @Override
    public ImageButtonStyle getStyle() {
        return this.imageButtonStyle;
    }
}
