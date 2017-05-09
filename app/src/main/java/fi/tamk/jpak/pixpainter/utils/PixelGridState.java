package fi.tamk.jpak.pixpainter.utils;

import fi.tamk.jpak.pixpainter.tools.Tool;

/**
 * Created by Juuso on 3.5.2017.
 */
public class PixelGridState {

    private static Pixel[][] pixels;
    private static Tool activeTool;
    private static ColorARGB primaryColor;
    private static ColorARGB secondaryColor;

    public static Pixel[][] getPixels() {
        return PixelGridState.pixels;
    }

    public static void setPixels(Pixel[][] pixels) {
        if (pixels != null) {
            PixelGridState.pixels = pixels;
        }
    }

    public static Tool getActiveTool() {
        return PixelGridState.activeTool;
    }

    public static void setActiveTool(Tool activeTool) {
        if (activeTool != null) {
            PixelGridState.activeTool = activeTool;
        }
    }

    public static ColorARGB getPrimaryColor() {
        return PixelGridState.primaryColor;
    }

    public static void setPrimaryColor(ColorARGB primaryColor) {
        if (primaryColor != null) {
            PixelGridState.primaryColor = primaryColor;
        }
    }

    public static ColorARGB getSecondaryColor() {
        return PixelGridState.secondaryColor;
    }

    public static void setSecondaryColor(ColorARGB secondaryColor) {
        if (secondaryColor != null) {
            PixelGridState.secondaryColor = secondaryColor;
        }
    }

    public static void clear() {
        PixelGridState.pixels = null;
        PixelGridState.activeTool = null;
        PixelGridState.primaryColor = null;
        PixelGridState.secondaryColor = null;
    }
}
