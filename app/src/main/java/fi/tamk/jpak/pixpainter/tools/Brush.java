package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Brush tool.
 *
 * Draws with soft/gradient stroke.
 *
 * @author Juuso Pakarinen
 * @version 25.04.2017
 */
public class Brush extends Tool {

    /**
     * Array containing fade values used to make gradient effect.
     */
    private int[] fadeValues;

    /**
     * Default constructor.
     */
    public Brush() {
        super(ToolType.BRUSH);
    }

    /**
     * Constructor.
     *
     * Initializes stroke size.
     * @param strokeSize Size of stroke.
     */
    public Brush(int strokeSize) {
        super(ToolType.BRUSH, strokeSize);
    }

    /**
     * Updates fade values according to stroke size.
     */
    public void updateFadeValues() {
        fadeValues = new int[getStrokeSize()];

        for (int i = 0; i < getStrokeSize(); i++) {
            fadeValues[i] = 5 * (i + 1);
        }
    }

    /**
     * Handle drawing.
     *
     * @param row Row of the origin point.
     * @param col Column of the origin point.
     * @param pixels Reference to pixel grid.
     * @param color1 Primary color.
     * @param color2 Secondary color. Not used.
     */
    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels,
                           ColorARGB color1, ColorARGB color2) {

        updateFadeValues();
        ColorARGB newColor = new ColorARGB(color1.getA(), color1.getR(),
                color1.getG(), color1.getB());
        handleStroke(row, col, pixels, newColor, true, fadeValues);
    }
}
