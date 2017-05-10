package fi.tamk.jpak.pixpainter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.support.v7.widget.AppCompatImageView;

import fi.tamk.jpak.pixpainter.tools.Pencil;
import fi.tamk.jpak.pixpainter.tools.Shape;
import fi.tamk.jpak.pixpainter.tools.Tool;
import fi.tamk.jpak.pixpainter.tools.ToolType;
import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;
import fi.tamk.jpak.pixpainter.utils.PixelGridState;

/**
 * View that is used for drawing onto.
 *
 * The view represents a drawing canvas which contains a grid with
 * {@link Pixel cells} that can be coloured with different tools.
 *
 * @author Juuso Pakarinen
 * @version 15.04.2017
 */
public class DrawingView extends AppCompatImageView {

    /**
     * Number of columns.
     */
    private int numColumns;

    /**
     * Number of rows.
     */
    private int numRows;

    /**
     * Width of a single grid cell.
     */
    private float cellWidth;

    /**
     * Height of a single grid cell.
     */
    private float cellHeight;

    /**
     * Default tool.
     */
    private Pencil defaultTool;

    /**
     * Selected tool.
     */
    private Tool tool;

    /**
     * Paint used to draw on the view.
     */
    private Paint paint;

    /**
     * Primary color.
     */
    private ColorARGB primaryColor;

    /**
     * Secondary color.
     */
    private ColorARGB secondaryColor;

    /**
     * State of the pixel grid.
     */
    private Pixel[][] pixels;

    /**
     * Constructor.
     *
     * @param context Context.
     * @param attrs Attribute set.
     */
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.TRANSPARENT);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setARGB(0, 0, 0, 0);
        numColumns = 1;
        numRows = 1;

        defaultTool = new Pencil();
        tool = PixelGridState.getActiveTool();
        if (tool == null) tool = defaultTool;

        primaryColor = PixelGridState.getPrimaryColor();
        if (primaryColor == null) primaryColor = new ColorARGB(255, 0, 0, 0);

        calculateDimensions();
    }

    /**
     * Sets the number of columns.
     * @param numColumns number of columns.
     */
    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    /**
     * Sets the number of rows.
     * @param numRows number of rows.
     */
    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    /**
     * This is called during layout when the size of this view has changed.
     *
     * @param w Current width of this view.
     * @param h Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    /**
     * Calculates and sets pixel grid dimensions and state.
     *
     * Validates number of rows and columns and calculates width and height
     * of a single cell based on them. Creates new instance of pixel grid
     * and loads stored state if one is found.
     */
    private void calculateDimensions() {

        if (numColumns < 1) {
            numColumns = 1;
        }

        if (numRows < 1) {
            numRows = 1;
        }

        cellWidth = (float) getWidth() / numColumns;
        cellHeight = (float) getHeight() / numRows;
        pixels = new Pixel[numRows][numColumns];
        initializePixels();
        loadPixelState();

        /* Invalidate view so it's redrawn */
        invalidate();
    }

    /**
     * Handles drawing on the {@link android.view.View view's} {@link Canvas canvas}.
     *
     * Draws rectangles, representing pixels in the {@link DrawingView#pixels grid},
     * with the width and height of one grid cell (pixel) and with it's color.
     *
     * @param canvas View's canvas.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);

        /* Fill selected cells */
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                ColorARGB c = pixels[i][j].getColor();
                paint.setARGB(c.getA(), c.getR(), c.getG(), c.getB());
                canvas.drawRect(
                        j * cellWidth,
                        i * cellHeight,
                        (j + 1) * cellWidth,
                        (i + 1) * cellHeight,
                        paint
                );
            }
        }
    }

    /**
     * Handles touch screen motion events.
     *
     * Calls selected tool's {@link Tool#handleDraw(int, int, Pixel[][], ColorARGB, ColorARGB)}
     * method while touch is down. If selected tool is the {@link Shape shape} tool,
     * place down the drawn shape when touch is lifted.
     *
     * @param event Touch screen motion event.
     * @return true.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int column = (int) (event.getX() / cellWidth);
        int row = (int) (event.getY() / cellHeight);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (tool.getType() == ToolType.SHAPE) {
                Shape s = (Shape) tool;
                s.handlePlacement(row, column, pixels, primaryColor, secondaryColor);
            }
        } else {
            if (tool.getType() == ToolType.FILL) {
                tool.handleDraw(row, column, pixels, primaryColor,
                        pixels[row][column].getColor());
            } else {
                tool.handleDraw(row, column, pixels, primaryColor, secondaryColor);
            }
        }

        PixelGridState.setPixels(pixels);
        invalidate();
        return true;
    }

    /**
     * Load the stored {@link PixelGridState state} of the pixel grid.
     */
    public void loadPixelState() {
        Pixel[][] pixelState = PixelGridState.getPixels();
        Tool toolState = PixelGridState.getActiveTool();
        ColorARGB pColorState = PixelGridState.getPrimaryColor();
        ColorARGB sColorState = PixelGridState.getSecondaryColor();

        if ((pixelState != null) && (pixelState.length == numRows)) {

            for (int i = 0; i < numRows; i++) {
                if (pixelState[i].length == numColumns) {

                    for (int j = 0; j < numColumns; j++) {
                        if (pixelState[i][j] != null) {
                            ColorARGB color = pixelState[i][j].getColor();
                            pixels[i][j] = new Pixel(j, i, new ColorARGB(
                                    color.getA(), color.getR(),
                                    color.getG(), color.getB()));
                        } else {
                            pixels[i][j] = new Pixel(j, i);
                        }
                    }
                }
            }

            invalidate();
        }

        if (toolState != null) {
            this.tool = toolState;
        }

        if (pColorState != null) {
            this.primaryColor = pColorState;
        }

        if (sColorState != null) {
            this.secondaryColor = sColorState;
        }
    }

    /**
     * Fill the {@link DrawingView#pixels pixel grid} with fully transparent
     * pixels.
     */
    public void initializePixels() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                pixels[i][j] = new Pixel(j, i);
            }
        }
    }

    /**
     * Set primary and secondary colors.
     *
     * @param primaryColor Primary color.
     * @param secondaryColor Secondary color.
     */
    public void setColors(ColorARGB primaryColor, ColorARGB secondaryColor) {
        if (primaryColor != null) this.primaryColor = primaryColor;
        if (secondaryColor != null) this.secondaryColor = secondaryColor;
        PixelGridState.setPrimaryColor(this.primaryColor);
        PixelGridState.setSecondaryColor(this.secondaryColor);
    }

    /**
     * Set tool.
     * @param tool Tool to be used.
     */
    public void setTool(Tool tool) {
        if (tool != null) {
            this.tool = tool;
        } else {
            this.tool = defaultTool;
        }

        PixelGridState.setActiveTool(this.tool);
    }
}
