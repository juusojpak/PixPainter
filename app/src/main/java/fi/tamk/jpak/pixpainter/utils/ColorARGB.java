package fi.tamk.jpak.pixpainter.utils;

import android.graphics.Color;

/**
 * Class representing the color used to draw on
 * {@link fi.tamk.jpak.pixpainter.DrawingView canvas}.
 *
 * @author Juuso Pakarinen
 * @version 17.04.2017
 */
public class ColorARGB {

    /**
     * Integer values for alpha, red, green and blue.
     */
    private int a, r, g, b;

    /**
     * Default constructor.
     */
    public ColorARGB() {
        this.a = 0;
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }

    /**
     * Construcotr.
     *
     * @param a Alpha.
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public ColorARGB(int a, int r, int g, int b) {
        this.a = 0;
        this.r = 0;
        this.g = 0;
        this.b = 0;
        setARGB(a, r, g, b);
    }

    /**
     * Returns value of alpha.
     * @return value of alpha.
     */
    public int getA() {
        return a;
    }

    /**
     * Sets the value of alpha.
     * @param a Value for alpha.
     */
    public void setA(int a) {
        if (a >= 0 && a <= 255) this.a = a;
    }

    /**
     * Returns value of red.
     * @return value of red.
     */
    public int getR() {
        return r;
    }

    /**
     * Sets the value of red.
     * @param r Value for red.
     */
    public void setR(int r) {
        if (r >= 0 && r <= 255) this.r = r;
    }

    /**
     * Returns value of green.
     * @return value of green.
     */
    public int getG() {
        return g;
    }

    /**
     * Sets the value of green.
     * @param g Value for green.
     */
    public void setG(int g) {
        if (g >= 0 && g <= 255) this.g = g;
    }

    /**
     * Returns value of blue.
     * @return value of blue.
     */
    public int getB() {
        return b;
    }

    /**
     * Sets the value of blue.
     * @param b Value for blue.
     */
    public void setB(int b) {
        if (b >= 0 && b <= 255) this.b = b;
    }

    /**
     * Set values for alpha, red, green and blue.
     *
     * @param a Alpha.
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public void setARGB(int a, int r, int g, int b) {
        if (a >= 0 && a <= 255) this.a = a;
        if (r >= 0 && r <= 255) this.r = r;
        if (g >= 0 && g <= 255) this.g = g;
        if (b >= 0 && b <= 255) this.b = b;
    }

    /**
     * Returns string representation of the color in form of
     * "(alpha) (red) (green) (blue)".
     *
     * @return string representation of the color.
     */
    public String toString() {
        return (a + " " + r + " " + g + " " + b);
    }

    /**
     * Returns string representation of the hexadecimal value of color in form
     * of "AARRGGBB".
     *
     * @return string representation of the hexadecimal value of color.
     */
    public String toHexString() {
        String hexColor = String.format("#%08X", Color.argb(a,r,g,b));
        return hexColor;
    }

    /**
     * Returns integer value of the color.
     * @return integer value of the color.
     */
    public int toInt() {
        return Color.argb(a, r, g, b);
    }
}
