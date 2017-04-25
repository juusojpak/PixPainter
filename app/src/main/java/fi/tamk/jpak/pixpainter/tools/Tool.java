package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.ColorARGB;
import fi.tamk.jpak.pixpainter.Pixel;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public abstract class Tool {

    private ToolType type;
    private int strokeSize;

    public Tool() {
        this.type = null;
        this.strokeSize = 1;
    }

    public Tool(ToolType type, int strokeSize){
        this.type = type;
        this.strokeSize = strokeSize;
    }

    public ToolType getType() {
        return type;
    }

    public void setType(ToolType type) {
        this.type = type;
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    public abstract void handleDraw(int row, int col, Pixel[][] pixels,
                                    ColorARGB color1, ColorARGB color2);
}
