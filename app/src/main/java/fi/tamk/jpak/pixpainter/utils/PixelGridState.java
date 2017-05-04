package fi.tamk.jpak.pixpainter.utils;

import fi.tamk.jpak.pixpainter.tools.Tool;

/**
 * Created by Juuso on 3.5.2017.
 */
public class PixelGridState {

    private static Pixel[][] pixels;
    private static Tool activeTool;
    private static ColorARGB primaryColor;

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
}
