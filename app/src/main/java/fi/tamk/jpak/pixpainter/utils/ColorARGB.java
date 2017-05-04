package fi.tamk.jpak.pixpainter.utils;

import android.graphics.Color;

/**
 * Created by Juuso Pakarinen on 17/04/2017.
 */
public class ColorARGB {

    private int a, r, g, b;

    public ColorARGB() {
        this.a = 0;
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }

    public ColorARGB(int a, int r, int g, int b) {
        this.a = 0;
        this.r = 0;
        this.g = 0;
        this.b = 0;
        setARGB(a, r, g, b);
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        if (a >= 0 && a <= 255) this.a = a;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        if (r >= 0 && r <= 255) this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        if (g >= 0 && g <= 255) this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        if (b >= 0 && b <= 255) this.b = b;
    }

    public void setARGB(int a, int r, int g, int b) {
        if (a >= 0 && a <= 255) this.a = a;
        if (r >= 0 && r <= 255) this.r = r;
        if (g >= 0 && g <= 255) this.g = g;
        if (b >= 0 && b <= 255) this.b = b;
    }

    public String toString() {
        return (a + " " + r + " " + g + " " + b);
    }

    public String toHexString() {
        String hexColor = String.format("#%08X", Color.argb(a,r,g,b));
        return hexColor;
    }

    public String toRGBHexString() {
        String hexColor = String.format("#%06X", Color.rgb(r,g,b));
        return hexColor;
    }

    public int toInt() {
        return Color.argb(a, r, g, b);
    }
}
