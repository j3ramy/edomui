package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.View;
import de.j3ramy.edomui.interfaces.IAction;

import javax.annotation.Nullable;

public final class DatetimeField extends TextField {
    private final View view;

    public DatetimeField(int x, int y, int width, int height, String placeholderText, String tooltipText, @Nullable IAction onTextChangeAction,
                         @Nullable IAction onPressEnterAction){
        super(x, y, width, height, placeholderText, onTextChangeAction, onPressEnterAction);

        this.view = new View();
        this.view.addWidget(new Tooltip(tooltipText, this.toRect()));
    }

    public DatetimeField(int x, int y, int width, int height, String tooltipText){
        this(x, y, width, height, "", tooltipText, null, null);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(!this.isHidden()){
            super.render(poseStack);

            this.view.render(poseStack);
        }
    }

    @Override
    public void update(int x, int y) {
        if(!this.isHidden()){
            super.update(x, y);

            this.view.update(x, y);
        }
    }
}

