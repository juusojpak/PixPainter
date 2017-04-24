package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.ColorARGB;
import fi.tamk.jpak.pixpainter.Pixel;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public abstract class Tool {

    private String name;
    private int strokeSize;
    private ColorARGB primaryColor;
    private ColorARGB secondaryColor;

    public Tool() {
        this.name = "Unknown";
        this.strokeSize = 1;
    }

    public Tool(String name, int strokeSize){
        this.name = name;
        this.strokeSize = strokeSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    public ColorARGB getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(ColorARGB primaryColor) {
        this.primaryColor = primaryColor;
    }

    public ColorARGB getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(ColorARGB secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public abstract void handleDraw(int row, int col, Pixel[][] pixels);
}
