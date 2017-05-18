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
     * Number of rows and columns in the grid.
     */
    private static int rows, cols;

    /**
     * List of previous pixel grid states.
     */
    private static LinkedList<Pixel[][]> history = new LinkedList<>();

    /**
     * Current position in history timeline.
     */
    private static int historyCursor = 0;

    /**
     * Max number of pixel grid states saved in history.
     */
    private static final int HISTORY_LENGTH = 40;

    /**
     * Whether the starting pixel grid state is saved in history.
     */
    private static boolean startStateSaved = false;

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
        return PixelGridState.historyCursor;
    }

    /**
     * Returns previous grid state from cursor.
     * @return previous grid state from cursor or null if out of bounds.
     */
    public static Pixel[][] getPreviousStateFromHistory() {

        if (PixelGridState.history != null && PixelGridState.history.size() > 0) {
            if ((PixelGridState.historyCursor + 1) < PixelGridState.history.size()) {
                PixelGridState.historyCursor = PixelGridState.historyCursor + 1;

                return PixelGridState.history.get(PixelGridState.historyCursor);
            }
        }

        return null;
    }

    /**
     * Returns next grid state from cursor.
     * @return next grid state from cursor or null if out of bounds.
     */
    public static Pixel[][] getNextStateFromHistory() {

        if (PixelGridState.history != null && PixelGridState.history.size() > 0) {
            if (PixelGridState.historyCursor > 0) {
                PixelGridState.historyCursor = PixelGridState.historyCursor - 1;

                return PixelGridState.history.get(PixelGridState.historyCursor);
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

            if (PixelGridState.history.size() > PixelGridState.HISTORY_LENGTH) {
                PixelGridState.history.removeLast();
            }

            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    ColorARGB c = pixels[i][j].getColor();
                    state[i][j] = new Pixel(j, i,
                            new ColorARGB(c.getA(), c.getR(), c.getG(), c.getB()));
                }
            }

            PixelGridState.history.addFirst(state);
            PixelGridState.historyCursor = 0;
        }
    }

    public static int getRows() {
        return PixelGridState.rows;
    }

    public static void setRows(int rows) {
        if (rows > 0) PixelGridState.rows = rows;
    }

    public static int getCols() {
        return PixelGridState.cols;
    }

    public static void setCols(int cols) {
        if (cols > 0) PixelGridState.cols = cols;
    }

    public static boolean isStartStateSaved() {
        return PixelGridState.startStateSaved;
    }

    public static void setStartStateSaved(boolean startStateSaved) {
        PixelGridState.startStateSaved = startStateSaved;
    }

    public static int getHistorySize() {
        return PixelGridState.history.size();
    }

    public static void setStartStateToHistory(int rows, int cols) {
        Pixel[][] state = new Pixel[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                state[i][j] = new Pixel(j, i);
            }
        }

        PixelGridState.history.addFirst(state);
    }

    /**
     * Clear all stored data.
     */
    public static void clear() {
        PixelGridState.pixels = null;
        PixelGridState.activeTool = null;
        PixelGridState.primaryColor = null;
        PixelGridState.secondaryColor = null;
        PixelGridState.history.clear();
        PixelGridState.startStateSaved = false;
        PixelGridState.cols = 0;
        PixelGridState.rows = 0;
    }
}
