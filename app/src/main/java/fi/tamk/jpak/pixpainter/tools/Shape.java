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

    public Shape(ShapeType type) {
        super(ToolType.SHAPE);
        this.type = type;
        this.width = 6;
        this.height = 6;
        this.stateSaved = false;
    }

    public ShapeType getShapeType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width > 0 && width <= 100) this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height > 0 && height <= 100) this.height = height;
    }

    public void handlePlacement(int row, int col, Pixel[][] pixels,
                                ColorARGB color1, ColorARGB color2) {

        switch (this.type) {
            case RECTANGLE:
                drawRectangle(row, col, pixels, color1, color2, false);
                break;
            case CIRCLE:
                drawCircle(row, col, pixels, color1, color2, false);
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
            case CIRCLE:
                drawCircle(row, col, pixels, color1, color2, true);
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

    public void drawCircle(int row, int col, Pixel[][] pixels,
                              ColorARGB color1, ColorARGB color2, boolean dragging) {

        int x = 15;
        int y = 0;
        int direction = 0;

        if (dragging) {
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    drawPixel(pixels[i][j], colorState[i][j]);
                }
            }
        }

        while (x >= y) {
            if ((row + x < pixels.length) &&
                    (row + x >= 0) &&
                    (col + y < pixels[0].length))
                drawPixel(pixels[row + x][col + y], color1);

            if ((row + y < pixels.length) &&
                    (col + x >= 0) &&
                    (col + x < pixels[0].length))
                drawPixel(pixels[row + y][col + x], color1);

            if ((row - y >= 0) &&
                    (col + x >= 0) &&
                    (col + x < pixels[0].length))
                drawPixel(pixels[row - y][col + x], color1);

            if ((row - x >= 0) &&
                    (row - x < pixels.length) &&
                    (col + y < pixels[0].length))
                drawPixel(pixels[row - x][col + y], color1);

            if ((row - x >= 0) &&
                    (row - x < pixels.length) &&
                    (col - y >= 0))
                drawPixel(pixels[row - x][col - y], color1);

            if ((row - y >= 0) &&
                    (col - x < pixels[0].length) &&
                    (col - x >= 0))
                drawPixel(pixels[row - y][col - x], color1);

            if ((row + y < pixels.length) &&
                    (col - x < pixels[0].length) &&
                    (col - x >= 0))
                drawPixel(pixels[row + y][col - x], color1);

            if ((row + x < pixels.length) &&
                    (row + x >= 0) &&
                    (col - y >= 0))
                drawPixel(pixels[row + x][col - y], color1);

            y++;
            if (direction <= 0) {
                direction += (2 * y + 1);
            } else {
                x--;
                direction -= (2 * x + 1);
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
