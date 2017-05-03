package fi.tamk.jpak.pixpainter.utils;

import java.io.Serializable;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public class Pixel implements Serializable {

    private int x;
    private int y;
    private ColorARGB color;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = new ColorARGB();
    }

    public Pixel(int x, int y, ColorARGB color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ColorARGB getColor() {
        return color;
    }

    public void setColor(ColorARGB color) {
        this.color = color;
    }

    public void blendColor(ColorARGB blendColor, int fade) {
        int a, r, g, b;
        int rDiff = Math.abs(this.color.getR() - blendColor.getR());
        int gDiff = Math.abs(this.color.getG() - blendColor.getG());
        int bDiff = Math.abs(this.color.getB() - blendColor.getB());

        a = this.color.getA() + (blendColor.getA() / fade);
        r = this.color.getR() + (rDiff / fade);
        g = this.color.getG() + (gDiff / fade);
        b = this.color.getB() + (bDiff / fade);
        if (a > 255) a = 255;
        if (r > 255) r = 255;
        if (g > 255) g = 255;
        if (b > 255) b = 255;

        this.color.setARGB(a, r, g, b);
    }
}
