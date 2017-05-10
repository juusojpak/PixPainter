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
        int a = this.color.getA() + (blendColor.getA() / fade);
        int r = (this.color.getR() + blendColor.getR()) / 2;
        int g = (this.color.getG() + blendColor.getG()) / 2;
        int b = (this.color.getB() + blendColor.getB()) / 2;
        if (a > 255 || a < 0) a = 255;
        if (r > 255 || r < 0) r = 255;
        if (g > 255 || g < 0) g = 255;
        if (b > 255 || b < 0) b = 255;

        this.color.setARGB(a, r, g, b);
    }
}
