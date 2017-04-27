package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Created by Juuso Pakarinen on 27/04/2017.
 */
public class Eraser extends Tool {

    public Eraser() {
        super(ToolType.ERASE);
    }

    public Eraser(int strokeSize) {
        super(ToolType.ERASE, strokeSize);
    }

    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels,
                           ColorARGB color1, ColorARGB color2) {

        handleStroke(row, col, pixels, new ColorARGB(), false, null);
    }
}
