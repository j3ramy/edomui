package de.j3ramy.edomui;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import static de.j3ramy.edomui.EdomUiMod.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class EdomUiMod
{
    public static final String MOD_ID = "edomui";

    public EdomUiMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
