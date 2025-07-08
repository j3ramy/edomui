package de.j3ramy.edomui.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.InputStream;
import java.util.Locale;

public class GuiMathUtils  {
    public static int clamp(int value, int min, int max){
        return java.lang.Math.max(min, java.lang.Math.min(max, value));
    }

    public static boolean isNumeric(String str, boolean isFloat, boolean allowNegative) {
        if (str == null || str.isEmpty()) return false;

        String pattern = allowNegative ? "-?" : "";
        pattern += isFloat ? "\\d+(\\.\\d+)?" : "\\d+";

        return str.matches(pattern);
    }

    public static boolean isNumeric(String str, boolean isFloat) {
        return isNumeric(str, isFloat, true);
    }

    public static boolean isPositiveInteger(String str) {
        return str != null && str.matches("\\d+");
    }

    public static long convertHourToTicks(int hour) {
        long ticksPerHour = 1000L;
        long tickOffset = 6000L; // Ticks are 0 at 6 AM
        long tickValue = (hour % 24) * ticksPerHour;

        return (24000 - tickOffset + tickValue) % 24000;
    }

    public static String formatFloatByLocale(float f, Locale locale, String currency) {
        return formatFloatByLocale(f, locale) + " " + currency;
    }

    public static String formatFloatByLocale(float f, Locale locale) {
        return String.format(locale, "%.2f", f);
    }

    public static int calculateHeightFromWidth(int width, ResourceLocation textureLoc){
        try {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            Resource resource = resourceManager.getResource(textureLoc);
            InputStream inputStream = resource.getInputStream();

            NativeImage nativeImage = NativeImage.read(inputStream);
            int originalWidth = nativeImage.getWidth();
            int originalHeight = nativeImage.getHeight();

            nativeImage.close();
            inputStream.close();
            
            return Math.round(width * originalHeight / (float) originalWidth);
        } catch (Exception e) {
            e.printStackTrace();
            
            return 0;
        }
    }
    
    public static float getRatio(int width, int height){
        return (float) width / (float) height;
    }
}
