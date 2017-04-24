package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.ColorARGB;
import fi.tamk.jpak.pixpainter.Pixel;

/**
 * Created by Juuso Pakarinen on 24/04/2017.
 */
public class Shape extends Tool {

    private ShapeType type;
    private int width;
    private int height;

    public Shape() {
        super("Shape", 6);
        this.type = ShapeType.RECTANGLE;
        this.width = 6;
        this.height = 6;
    }

    public Shape(ShapeType type, int width, int height) {
        this.type = type;
        this.width = width;
        this.height = height;
    }

    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels) {

        System.out.println("SHAPE DRAW");

        switch (this.type) {
            case RECTANGLE:
                drawRectangle(row, col, pixels);
                break;
            default:
                drawRectangle(row, col, pixels);
                break;
        }
    }

    public void drawRectangle(int row, int col, Pixel[][] pixels) {

        /* Left side */
        for (int i = 0; i < height; i++) {
            if ((row + i) < pixels.length) {
                Pixel p = pixels[row + i][col];
                drawPixel(p, getPrimaryColor());
            }
        }

        /* Top side */
        for (int i = 0; i < width; i++) {
            if ((col + i) < pixels[0].length) {
                Pixel p = pixels[row][col + i];
                drawPixel(p, getPrimaryColor());
            }
        }

        /* Right side */
        if ((col + width) < pixels[0].length) {
            for (int i = 0; i < height; i++) {
                if ((row + i) < pixels.length) {
                    Pixel p = pixels[row + i][((col + width) - 1)];
                    drawPixel(p, getPrimaryColor());
                }
            }
        }

        /* Bottom side */
        if ((row + height) < pixels.length) {
            for (int i = 0; i < width; i++) {
                if ((col + i) < pixels[0].length) {
                    Pixel p = pixels[((row + height) - 1)][col + i];
                    drawPixel(p, getPrimaryColor());
                }
            }
        }
    }

    public void drawPixel(Pixel p, ColorARGB color) {
        p.getColor().setARGB(color.getA(), color.getR(), color.getG(), color.getB());
        p.setChecked(true);
    }
}
