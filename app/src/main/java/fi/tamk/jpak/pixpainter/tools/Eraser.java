package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Eraser tool.
 *
 * Erases with hard stroke.
 *
 * @author Juuso Pakarinen
 * @version 25.04.2017
 */
public class Eraser extends Tool {

    /**
     * Default constructor.
     */
    public Eraser() {
        super(ToolType.ERASE);
    }

    /**
     * Constructor.
     *
     * Initializes stroke size.
     * @param strokeSize Size of stroke.
     */
    public Eraser(int strokeSize) {
        super(ToolType.ERASE, strokeSize);
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

        handleStroke(row, col, pixels, new ColorARGB(), false, null);
    }
}
