package fi.tamk.jpak.pixpainter.utils;

import java.io.Serializable;

/**
 * Class representing pixel in the
 * {@link fi.tamk.jpak.pixpainter.DrawingView#pixels grid} of the
 * {@link fi.tamk.jpak.pixpainter.DrawingView canvas}.
 *
 * @author Juuso Pakarinen
 * @version 24.04.2017
 */
public class Pixel implements Serializable {

    /**
     * X coordinate in the grid.
     */
    private int x;

    /**
     * Y coordinate in the grid.
     */
    private int y;

    /**
     * Color of the pixel.
     */
    private ColorARGB color;

    /**
     * Constructor.
     *
     * @param x X coordinate in the grid.
     * @param y Y coordinate in the grid.
     */
    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = new ColorARGB();
    }

    /**
     * Constructor.
     *
     * @param x X coordinate in the grid.
     * @param y Y coordinate in the grid.
     * @param color Color for the pixel.
     */
    public Pixel(int x, int y, ColorARGB color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Returns the X coordinate.
     * @return X coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the X coordinate.
     * @param x X coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the Y coordinate.
     * @return Y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the Y coordinate.
     * @param y Y coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns color of the pixel.
     * @return color of the pixel.
     */
    public ColorARGB getColor() {
        return color;
    }

    /**
     * Sets color of the pixel.
     * @param color Color of the pixel.
     */
    public void setColor(ColorARGB color) {
        this.color = color;
    }

    /**
     * Blends color with the color of this pixel.
     *
     * @param blendColor Color to be blended.
     * @param fade Amount of fade used to reduce added alpha.
     */
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
