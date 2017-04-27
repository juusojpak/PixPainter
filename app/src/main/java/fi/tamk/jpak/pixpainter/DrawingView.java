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
import fi.tamk.jpak.pixpainter.tools.Tool;
import fi.tamk.jpak.pixpainter.tools.ToolType;
import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Created by Juuso Pakarinen on 15/04/2017.
 */
public class DrawingView extends AppCompatImageView {

    private int numColumns, numRows;
    private float cellWidth, cellHeight;
    private Pencil defaultTool;
    private Tool tool;
    private Paint paint;
    private ColorARGB primaryColor;
    private ColorARGB secondaryColor;
    private Pixel[][] pixels;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.TRANSPARENT);

        defaultTool = new Pencil();
        tool = defaultTool;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setARGB(0, 0, 0, 0);
        numColumns = 1;
        numRows = 1;
        calculateDimensions();
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

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

        /* Invalidate view so it's redrawn */
        invalidate();
    }

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int column = (int) (event.getX() / cellWidth);
        int row = (int) (event.getY() / cellHeight);

        if (tool.getType() == ToolType.FILL) {
            tool.handleDraw(row, column, pixels, primaryColor,
                    pixels[row][column].getColor());
        } else {
            tool.handleDraw(row, column, pixels, primaryColor, secondaryColor);
        }

        invalidate();
        return true;
    }

    public void initializePixels() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                pixels[i][j] = new Pixel(j, i);
            }
        }
    }

    public void setColors(ColorARGB primaryColor, ColorARGB secondaryColor) {
        if (primaryColor != null) this.primaryColor = primaryColor;
        if (secondaryColor != null) this.secondaryColor = secondaryColor;
    }

    public void setTool(Tool tool) {
        if (tool != null) {
            this.tool = tool;
        } else {
            this.tool = defaultTool;
        }
    }
}
