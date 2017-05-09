package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public class Shape extends Tool {

    private ShapeType type;
    private int width;
    private int height;
    private ColorARGB[][] colorState;
    private boolean stateSaved;

    public Shape() {
        super(ToolType.SHAPE);
        this.type = ShapeType.RECTANGLE;
        this.width = 6;
        this.height = 6;
        this.stateSaved = false;
    }

    public Shape(ShapeType type, int width, int height) {
        super(ToolType.SHAPE);
        this.type = type;
        this.width = width;
        this.height = height;
        this.stateSaved = false;
    }

    public ShapeType getShapeType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void handlePlacement(int row, int col, Pixel[][] pixels,
                                ColorARGB color1, ColorARGB color2) {

        switch (this.type) {
            case RECTANGLE:
                drawRectangle(row, col, pixels, color1, color2, false);
                break;
            default:
                drawRectangle(row, col, pixels, color1, color2, false);
                break;
        }

        stateSaved = false;
    }

    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels,
                           ColorARGB color1, ColorARGB color2) {

        if (!stateSaved) saveColorState(pixels);
        switch (this.type) {
            case RECTANGLE:
                drawRectangle(row, col, pixels, color1, color2, true);
                break;
            default:
                drawRectangle(row, col, pixels, color1, color2, true);
                break;
        }
    }

    public void drawRectangle(int row, int col, Pixel[][] pixels,
                              ColorARGB color1, ColorARGB color2, boolean dragging) {

        if (dragging) {
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    drawPixel(pixels[i][j], colorState[i][j]);
                }
            }
        }

        /* Left side */
        for (int i = 0; i < height; i++) {
            if ((row + i) < pixels.length) {
                Pixel p = pixels[row + i][col];
                drawPixel(p, color1);
            }
        }

        /* Top side */
        for (int i = 0; i < width; i++) {
            if ((col + i) < pixels[0].length) {
                Pixel p = pixels[row][col + i];
                drawPixel(p, color1);
            }
        }

        /* Right side */
        if ((col + width) < pixels[0].length) {
            for (int i = 0; i < height; i++) {
                if ((row + i) < pixels.length) {
                    Pixel p = pixels[row + i][((col + width) - 1)];
                    drawPixel(p, color1);
                }
            }
        }

        /* Bottom side */
        if ((row + height) < pixels.length) {
            for (int i = 0; i < width; i++) {
                if ((col + i) < pixels[0].length) {
                    Pixel p = pixels[((row + height) - 1)][col + i];
                    drawPixel(p, color1);
                }
            }
        }
    }

    public void drawPixel(Pixel p, ColorARGB color) {
        p.getColor().setARGB(color.getA(), color.getR(), color.getG(), color.getB());
    }

    public void saveColorState(Pixel[][] pixels) {
        if ((pixels != null) && (pixels.length > 0) && (pixels[0].length > 0)) {
            colorState = new ColorARGB[pixels.length][pixels[0].length];
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    ColorARGB c = pixels[i][j].getColor();
                    colorState[i][j] = new ColorARGB(c.getA(), c.getR(),
                            c.getG(), c.getB());
                }
            }

            stateSaved = true;
        }
    }
}
