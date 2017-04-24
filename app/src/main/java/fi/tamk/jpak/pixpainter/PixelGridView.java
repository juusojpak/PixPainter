package fi.tamk.jpak.pixpainter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import fi.tamk.jpak.pixpainter.tools.Pencil;
import fi.tamk.jpak.pixpainter.tools.Tool;

/**
 * Created by Juuso Pakarinen on 15/04/2017.
 */
public class PixelGridView extends View {

    private int numColumns, numRows;
    private float cellWidth, cellHeight;
    private Pencil defaultTool;
    private Tool tool;
    private Paint paint;
    private Pixel[][] pixels;

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        int width = getWidth();
        int height = getHeight();
        canvas.drawColor(Color.WHITE);

        /* Fill selected cells */
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (pixels[i][j].isChecked()) {

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

        paint.setARGB(255, 0, 0, 0);

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, paint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int column = (int)(event.getX() / cellWidth);
        int row = (int)(event.getY() / cellHeight);
        tool.handleDraw(row, column, pixels);

        invalidate();
        return true;
    }

    public void initializePixels() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                pixels[i][j] = new Pixel();
            }
        }
    }

    public void setTool(Tool tool) {
        if (tool != null) {
            this.tool = tool;
        } else {
            this.tool = defaultTool;
        }
    }
}
