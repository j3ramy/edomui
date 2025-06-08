package de.j3ramy.edomui.events;

import com.mojang.blaze3d.platform.InputConstants;
import de.j3ramy.edomui.EdomUiMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = EdomUiMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static KeyMapping testScreenKey;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        testScreenKey = new KeyMapping(
                "key." + EdomUiMod.MOD_ID + ".open_test_screen",
                KeyConflictContext.IN_GAME,
                KeyModifier.NONE,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_F10,
                "key.categories." + EdomUiMod.MOD_ID
        );

        ClientRegistry.registerKeyBinding(testScreenKey);
    }
}
