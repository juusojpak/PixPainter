package fi.tamk.jpak.pixpainter.utils;

import fi.tamk.jpak.pixpainter.tools.Tool;

/**
 * Static class for storing data associated with
 * {@link fi.tamk.jpak.pixpainter.EditorActivity editor}.
 *
 * The data stored by this class is used to restore editor state when activity is
 * recreated, for example upon device orientation change.
 *
 * @author Juuso Pakarinen
 * @version 03.05.2017
 */
public class PixelGridState {

    /**
     * State of the {@link fi.tamk.jpak.pixpainter.DrawingView#pixels pixel grid}.
     */
    private static Pixel[][] pixels;

    /**
     * Selected tool.
     */
    private static Tool activeTool;

    /**
     * Selected primary color.
     */
    private static ColorARGB primaryColor;

    /**
     * Selected secondary color.
     */
    private static ColorARGB secondaryColor;

    /**
     * Returns the stored state of pixel grid.
     * @return stored state of pixel grid.
     */
    public static Pixel[][] getPixels() {
        return PixelGridState.pixels;
    }

    /**
     * Sets the state of pixel grid to be stored.
     * @param pixels State of pixel grid.
     */
    public static void setPixels(Pixel[][] pixels) {
        if (pixels != null) {
            PixelGridState.pixels = pixels;
        }
    }

    /**
     * Returns selected tool of the stored state.
     * @return selected tool.
     */
    public static Tool getActiveTool() {
        return PixelGridState.activeTool;
    }

    /**
     * Sets selected tool to be stored.
     * @param activeTool Selected tool.
     */
    public static void setActiveTool(Tool activeTool) {
        if (activeTool != null) {
            PixelGridState.activeTool = activeTool;
        }
    }

    /**
     * Returns selected primary color of the stored state.
     * @return selected primary color
     */
    public static ColorARGB getPrimaryColor() {
        return PixelGridState.primaryColor;
    }

    /**
     * Sets selected primary color to be stored.
     * @param primaryColor Selected primary color.
     */
    public static void setPrimaryColor(ColorARGB primaryColor) {
        if (primaryColor != null) {
            PixelGridState.primaryColor = primaryColor;
        }
    }

    /**
     * Returns selected secondary color of the stored state.
     * @return selected secondary color
     */
    public static ColorARGB getSecondaryColor() {
        return PixelGridState.secondaryColor;
    }

    /**
     * Sets selected secondary color to be stored.
     * @param secondaryColor Selected secondary color.
     */
    public static void setSecondaryColor(ColorARGB secondaryColor) {
        if (secondaryColor != null) {
            PixelGridState.secondaryColor = secondaryColor;
        }
    }

    /**
     * Clear all stored data.
     */
    public static void clear() {
        PixelGridState.pixels = null;
        PixelGridState.activeTool = null;
        PixelGridState.primaryColor = null;
        PixelGridState.secondaryColor = null;
    }
}
