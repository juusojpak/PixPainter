package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Created by Juuso Pakarinen on 25/04/2017.
 */
public class Brush extends Tool {

    private int[] fadeValues;

    public Brush() {
        super(ToolType.BRUSH);
    }

    public Brush(int strokeSize) {
        super(ToolType.BRUSH, strokeSize);
    }

    public void updateFadeValues() {
        fadeValues = new int[getStrokeSize()];

        for (int i = 0; i < getStrokeSize(); i++) {
            fadeValues[i] = 5 * (i + 1);
        }
    }

    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels,
                           ColorARGB color1, ColorARGB color2) {

        updateFadeValues();
        ColorARGB newColor = new ColorARGB(color1.getA(), color1.getR(),
                color1.getG(), color1.getB());
        handleStroke(row, col, pixels, newColor, true, fadeValues);
    }
}
