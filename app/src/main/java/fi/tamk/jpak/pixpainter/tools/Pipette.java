package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Created by Juuso Pakarinen on 08/05/2017.
 */
public class Pipette extends Tool {

    private PipetteListener callback;

    public interface PipetteListener {
        void handlePipetteDraw(ColorARGB color);
    }

    public Pipette(PipetteListener callback) {
        super(ToolType.PIP);
        this.callback = callback;
    }

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
