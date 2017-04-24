package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.ColorARGB;
import fi.tamk.jpak.pixpainter.Pixel;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public class Pencil extends Tool {

    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels) {

        ColorARGB tmp = pixels[col][row].getColor();
        tmp.setARGB(getPrimaryColor().getA(), getPrimaryColor().getR(),
                getPrimaryColor().getG(), getPrimaryColor().getB());

        pixels[col][row].setChecked(true);
        pixels[col][row].setColor(tmp);
    }
}
