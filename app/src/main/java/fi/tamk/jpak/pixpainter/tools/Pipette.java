package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Pipette tool.
 *
 * Selects the color of the selected pixel.
 *
 * @author Juuso Pakarinen
 * @version 08.05.2017
 */
public class Pipette extends Tool {

    /**
     * Instance of callback interface.
     */
    private PipetteListener callback;

    /**
     * Callback interface for setting the selected color as primary color of
     * {@link fi.tamk.jpak.pixpainter.EditorActivity editor}.
     */
    public interface PipetteListener {
        /**
         * Handle selecting new color.
         * @param color Selected color.
         */
        void handlePipetteDraw(ColorARGB color);
    }

    /**
     * Constructor.
     * @param callback Listener for the tool.
     */
    public Pipette(PipetteListener callback) {
        super(ToolType.PIP);
        this.callback = callback;
    }

    /**
     * Handle drawing.
     *
     * Selects the color of the pixel at origin.
     *
     * @param row Row of the origin point.
     * @param col Column of the origin point.
     * @param pixels Reference to pixel grid.
     * @param color1 Primary color. Not used.
     * @param color2 Secondary color. Not used.
     */
    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels, ColorARGB color1, ColorARGB color2) {
        ColorARGB selectedColor = pixels[row][col].getColor();
        if (callback != null) {
            callback.handlePipetteDraw(new ColorARGB(
                    selectedColor.getA(),
                    selectedColor.getR(),
                    selectedColor.getG(),
                    selectedColor.getB()));
        }
    }
}
