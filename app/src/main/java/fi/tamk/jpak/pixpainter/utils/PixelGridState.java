package fi.tamk.jpak.pixpainter.utils;

import java.util.LinkedList;

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
     * Current state of the {@link fi.tamk.jpak.pixpainter.DrawingView#pixels pixel grid}.
     */
    private static Pixel[][] pixels;

    /**
     * List of previous pixel grid states.
     */
    private static LinkedList<Pixel[][]> history = new LinkedList<>();

    /**
     * Current position in history timeline.
     */
    private static int historyCursor = 0;

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
     * Returns current position in history timeline.
     * @return current position in history timeline.
     */
    public static int getHistoryCursor() {
        return historyCursor;
    }

    /**
     * Returns previous grid state from cursor.
     * @return previous grid state from cursor or null if out of bounds.
     */
    public static Pixel[][] getPreviousStateFromHistory() {
        if (history != null && history.size() > 0) {
            if ((historyCursor + 1) < history.size()) {
                historyCursor++;
                return history.get(historyCursor);
            }
        }

        return null;
    }

    /**
     * Returns next grid state from cursor.
     * @return next grid state from cursor or null if out of bounds.
     */
    public static Pixel[][] getNextStateFromHistory() {
        if (history != null && history.size() > 0) {
            if (historyCursor > 0) {
                historyCursor--;
                return history.get(historyCursor);
            }
        }

        return null;
    }

    /**
     * Add current pixel grid state to history.
     * @param pixels Current pixel grid state.
     */
    public static void saveToHistory(Pixel[][] pixels) {
        if (pixels != null && pixels.length > 0 && pixels[0].length > 0) {
            Pixel[][] state = new Pixel[pixels.length][pixels[0].length];

            if (history.size() > 10) {
                history.removeLast();
            }

            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    ColorARGB c = pixels[i][j].getColor();
                    state[i][j] = new Pixel(j, i,
                            new ColorARGB(c.getA(), c.getR(), c.getG(), c.getB()));
                }
            }

            while (historyCursor > 0) {
                history.removeFirst();
                historyCursor--;
            }

            history.addFirst(state);
            historyCursor = 0;
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
