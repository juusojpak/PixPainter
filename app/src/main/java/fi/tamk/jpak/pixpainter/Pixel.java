package fi.tamk.jpak.pixpainter;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public class Pixel {

    private ColorARGB color;
    private boolean isChecked;

    public Pixel() {
        this.color = new ColorARGB(0, 0, 0, 0);
        this.isChecked = false;
    }

    public Pixel(ColorARGB color, boolean isChecked) {
        this.color = color;
        this.isChecked = isChecked;
    }

    public ColorARGB getColor() {
        return color;
    }

    public void setColor(ColorARGB color) {
        this.color = color;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
