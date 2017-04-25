package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.ColorARGB;
import fi.tamk.jpak.pixpainter.Pixel;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public class Pencil extends Tool {

    public Pencil() {
        super(ToolType.PEN);
    }

    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels,
                           ColorARGB color1, ColorARGB color2) {

        pixels[row][col].setColor(new ColorARGB(color1.getA(), color1.getR(),
                color1.getG(), color1.getB()));
    }
}
