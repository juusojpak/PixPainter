package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Shape tool.
 *
 * Inserts circles or rectangles to canvas. These shapes can be only
 * {@link ShapeFillType#OUTLINE outlines} of the shape,
 * {@link ShapeFillType#SOLID solid} with one color,
 * or have outlines drawn with primary color and
 * {@link ShapeFillType#FILL filled} with secondary color.
 *
 * @author Juuso Pakarinen
 * @version 24.04.2017
 */
public class Shape extends Tool {

    /**
     * Type of the shape. Circle or rectangle.
     */
    private ShapeType type;

    /**
     * Type of the shape fill.
     */
    private ShapeFillType fillType;

    /**
     * Width of the shape.
     */
    private int width;

    /**
     * Height of the shape.
     */
    private int height;

    /**
     * Saved state of {@link fi.tamk.jpak.pixpainter.DrawingView#pixels pixel grid}
     * used to keep the starting state of canvas while
     * {@link Shape#handleDraw(int, int, Pixel[][], ColorARGB, ColorARGB) hovering}
     * the shape.
     */
    private ColorARGB[][] colorState;

    /**
     * Whether the the starting state of canvas is saved.
     */
    private boolean stateSaved;

    /**
     * Default constructor.
     */
    public Shape() {
        super(ToolType.SHAPE);
        this.type = ShapeType.RECTANGLE;
        this.fillType = ShapeFillType.OUTLINE;
        this.width = 6;
        this.height = 6;
        this.stateSaved = false;
    }

    /**
     * Constructor.
     *
     * @param type Type of the shape.
     * @param fillType Type of the shape fill.
     */
    public Shape(ShapeType type, ShapeFillType fillType) {
        super(ToolType.SHAPE);
        this.type = type;
        this.fillType = fillType;
        this.width = 6;
        this.height = 6;
        this.stateSaved = false;
    }

    /**
     * Returns type of the shape.
     * @return type of the shape.
     */
    public ShapeType getShapeType() {
        return type;
    }

    /**
     * Sets type of the shape.
     * @param type Type of the shape.
     */
    public void setShapeType(ShapeType type) {
        this.type = type;
    }

    /**
     * Returns type of the shape fill.
     * @return type of the shape fill.
     */
    public ShapeFillType getShapeFillType() {
        return fillType;
    }

    /**
     * Sets type of the shape fill.
     * @param fillType Type of the shape fill.
     */
    public void setShapeFillType(ShapeFillType fillType) {
        this.fillType = fillType;
    }

    /**
     * Returns width of the shape.
     * @return width of the shape.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width of the shape.
     * @param width Width of the shape.
     */
    public void setWidth(int width) {
        if (width > 0 && width <= 100) this.width = width;
    }

    /**
     * Returns height of the shape.
     * @return height of the shape.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height of the shape.
     * @param height Height of the shape.
     */
    public void setHeight(int height) {
        if (height > 0 && height <= 100) this.height = height;
    }

    /**
     * Handle placement of the shape.
     *
     * Places the shape to {@link fi.tamk.jpak.pixpainter.DrawingView canvas}
     * when touch is lifted.
     *
     * @param row Row of the origin point.
     * @param col Column of the origin point.
     * @param pixels Reference to pixel grid.
     * @param color1 Primary color.
     * @param color2 Secondary color.
     */
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

    /**
     * Handle drawing.
     *
     * Hovers the shape over the {@link fi.tamk.jpak.pixpainter.DrawingView canvas}
     * while touch is down.
     *
     * @param row Row of the origin point.
     * @param col Column of the origin point.
     * @param pixels Reference to pixel grid.
     * @param color1 Primary color.
     * @param color2 Secondary color.
     */
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

    /**
     * Draw rectangle.
     *
     * Draws the outlines first. Then fills the shape according to shape fill type.
     *
     * @param row Row of the origin point.
     * @param col Column of the origin point.
     * @param pixels Reference to pixel grid.
     * @param color1 Primary color.
     * @param color2 Secondary color.
     * @param dragging Whether the touch is down or lifted.
     */
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
            if ((row + i) < pixels.length && (row + i) >= 0 && col >= 0) {
                Pixel p = pixels[row + i][col];
                drawPixel(p, color1);
            }
        }

        /* Top side */
        for (int i = 0; i < width; i++) {
            if ((col + i) < pixels[0].length && (col + i) >= 0 && row >= 0) {
                Pixel p = pixels[row][col + i];
                drawPixel(p, color1);
            }
        }

        /* Right side */
        if ((col + width) < pixels[0].length) {
            for (int i = 0; i < height; i++) {
                if ((row + i) < pixels.length && (row + i) >= 0) {
                    Pixel p = pixels[row + i][((col + width) - 1)];
                    drawPixel(p, color1);
                }
            }
        }

        /* Bottom side */
        if ((row + height) < pixels.length) {
            for (int i = 0; i < width; i++) {
                if ((col + i) < pixels[0].length && (col + i) >= 0) {
                    Pixel p = pixels[((row + height) - 1)][col + i];
                    drawPixel(p, color1);
                }
            }
        }

        /* Fill inside of rectangle with color if selected so */
        if (fillType != ShapeFillType.OUTLINE) {
            ColorARGB c = color1;
            if (fillType == ShapeFillType.FILL) c = color2;

            for (int i = 0; i < height - 2; i++) {
                for (int j = 0; j < width - 2; j++) {
                    if ((row + (i + 1) < pixels.length) && (col + (j + 1) < pixels[0].length)) {
                        Pixel p = pixels[row + (i + 1)][col + (j + 1)];
                        drawPixel(p, c);
                    }
                }
            }
        }
    }

    /**
     * Draw circle.
     *
     * Draws circle using the midpoint circle algorithm.
     *
     * @param row Row of the origin point.
     * @param col Column of the origin point.
     * @param pixels Reference to pixel grid.
     * @param color1 Primary color.
     * @param color2 Secondary color.
     * @param dragging Whether the touch is down or lifted.
     */
    public void drawCircle(int row, int col, Pixel[][] pixels,
                              ColorARGB color1, ColorARGB color2, boolean dragging) {

        int x = width;
        int y = 0;
        int direction = 0;

        if (dragging) {
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    drawPixel(pixels[i][j], colorState[i][j]);
                }
            }
        }

        /* Fill the inside */
        if (fillType != ShapeFillType.OUTLINE) {
            ColorARGB c = color1;
            if (fillType == ShapeFillType.FILL) c = color2;

            while (x >= y) {

                for (int i = (col - y); i < (col + y); i++) {
                    if ((row + x < pixels.length) &&
                            (row + x >= 0) &&
                            (i >= 0) &&
                            (i < pixels[0].length)) {

                        drawPixel(pixels[row + x][i], c);
                    }
                }

                for (int i = (col - x); i < (col + x); i++) {
                    if ((row + y < pixels.length) &&
                            (row + y >= 0) &&
                            (i >= 0) &&
                            (i >= 0) &&
                            (i < pixels[0].length)) {

                        drawPixel(pixels[row + y][i], c);
                    }
                }

                for (int i = (col - y); i < (col + y); i++) {
                    if ((row - x < pixels.length) &&
                            (row - x >= 0) &&
                            (i >= 0) &&
                            (i < pixels[0].length)) {

                        drawPixel(pixels[row - x][i], c);
                    }
                }

                for (int i = (col - x); i < (col + x); i++) {
                    if ((row - y >= 0) &&
                            (row < pixels.length) &&
                            (i >= 0) &&
                            (i >= 0) &&
                            (i < pixels[0].length)) {

                        drawPixel(pixels[row - y][i], c);
                    }
                }

                y++;
                if (direction <= 0) {
                    direction += (2 * y + 1);
                } else {
                    x--;
                    direction -= (2 * x + 1);
                }
            }
        }

        x = width;
        y = 0;
        direction = 0;

        /*
         * Draws to every pixel that has the distance of length of the radius
         * from origin.
         */
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
                    (row < pixels.length) &&
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

    /**
     * Set color of the pixel.
     *
     * @param p Selected pixel.
     * @param color Selected color.
     */
    public void drawPixel(Pixel p, ColorARGB color) {
        p.getColor().setARGB(color.getA(), color.getR(), color.getG(), color.getB());
    }

    /**
     * Save state of the {@link fi.tamk.jpak.pixpainter.DrawingView#pixels pixel grid}
     * to keep the starting state of canvas while
     * {@link Shape#handleDraw(int, int, Pixel[][], ColorARGB, ColorARGB) hovering}
     * the shape.
     *
     * @param pixels Reference to pixel grid.
     */
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
