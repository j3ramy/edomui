package de.j3ramy.edomui.events;

import de.j3ramy.edomui.View;
import de.j3ramy.edomui.EdomUiMod;
import de.j3ramy.edomui.ViewRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EdomUiMod.MOD_ID, value = Dist.CLIENT)
public class UiEvents {

    @SubscribeEvent
    public static void onMouseClickEvent(ScreenEvent.MouseClickedEvent.Pre event){
        for(View view : ViewRegistry.getRegisteredViews()){
            view.onClick(event.getButton());
        }
    }

    @SubscribeEvent
    public static void onKeyPressedEvent(ScreenEvent.KeyboardKeyPressedEvent.Pre event){
        for(View view : ViewRegistry.getRegisteredViews()){
            view.keyPressed(event.getKeyCode());
        }
    }

    @SubscribeEvent
    public static void onCharTypedEvent(ScreenEvent.KeyboardCharTypedEvent.Pre event){
        for(View view : ViewRegistry.getRegisteredViews()){
            view.charTyped(event.getCodePoint());
        }
    }

    @SubscribeEvent
    public static void onMouseScrollEvent(ScreenEvent.MouseScrollEvent.Pre event){
        for(View view : ViewRegistry.getRegisteredViews()){
            view.onScroll(event.getScrollDelta());
        }
    }

    @SubscribeEvent
    public static void onMouseDragEvent(ScreenEvent.MouseDragEvent event){
        for(View view : ViewRegistry.getRegisteredViews()){
            view.onMouseDrag(event.getMouseButton(), event.getDragX(), event.getDragY(), (int) event.getMouseX(), (int) event.getMouseY());
        }
    }

    @SubscribeEvent
    public static void onRender(ScreenEvent.DrawScreenEvent event){
        for(View view : ViewRegistry.getRegisteredViews()){
            view.update(event.getMouseX(), event.getMouseY());
            view.render(event.getPoseStack());
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event){
        for(View view : ViewRegistry.getRegisteredViews()){
            view.tick();
        }
    }
}
