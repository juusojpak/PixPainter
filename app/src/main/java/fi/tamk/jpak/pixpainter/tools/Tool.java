package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.ColorARGB;
import fi.tamk.jpak.pixpainter.Pixel;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public abstract class Tool {

    private ToolType type;

    public Tool() {
        this.type = null;
    }

    public Tool(ToolType type){
        this.type = type;
    }

    public ToolType getType() {
        return type;
    }

    public void setType(ToolType type) {
        this.type = type;
    }

    public abstract void handleDraw(int row, int col, Pixel[][] pixels,
                                    ColorARGB color1, ColorARGB color2);
}
