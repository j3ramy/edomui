package de.j3ramy.edomui.event;

import de.j3ramy.edomui.EdomUiMod;
import de.j3ramy.edomui.screen.TestScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Key Handler
@Mod.EventBusSubscriber(modid = EdomUiMod.MOD_ID, value = Dist.CLIENT)
public class KeyBindings {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ClientSetup.testScreenKey.consumeClick()) {
            Minecraft.getInstance().setScreen(new TestScreen());
        }
    }
}
