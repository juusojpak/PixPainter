package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Pencil tool.
 *
 * Draws with hard stroke.
 *
 * @author Juuso Pakarinen
 * @version 24.04.2017
 */
public class Pencil extends Tool {

    /**
     * Default constructor.
     */
    public Pencil() {
        super(ToolType.PEN);
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

        ColorARGB newColor = new ColorARGB(color1.getA(), color1.getR(),
                color1.getG(), color1.getB());
        handleStroke(row, col, pixels, newColor, false, null);
    }
}
