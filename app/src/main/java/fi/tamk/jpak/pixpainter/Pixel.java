package fi.tamk.jpak.pixpainter;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public class Pixel {

    private int x;
    private int y;
    private ColorARGB color;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = new ColorARGB();
    }

    public Pixel(int x, int y, ColorARGB color, boolean isChecked) {
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
}
