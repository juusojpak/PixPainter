package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.ColorARGB;
import fi.tamk.jpak.pixpainter.Pixel;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public class Pencil extends Tool {

    public Pencil() {
        super("Pencil", 1);
    }

    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels) {

        System.out.println("PENCIL DRAW");

        ColorARGB tmp = pixels[row][col].getColor();
        tmp.setARGB(getPrimaryColor().getA(), getPrimaryColor().getR(),
                getPrimaryColor().getG(), getPrimaryColor().getB());

        pixels[row][col].setChecked(true);
        pixels[row][col].setColor(tmp);
    }
}
