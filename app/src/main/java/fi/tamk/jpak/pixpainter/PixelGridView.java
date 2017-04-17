package fi.tamk.jpak.pixpainter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Juuso Pakarinen on 15/04/2017.
 */
public class PixelGridView extends View {

    private int numColumns, numRows;
    private float cellWidth, cellHeight;
    private Paint paint = new Paint();
    private ColorARGB paintColor;
    private ColorARGB[][] pixels;
    private boolean[][] cellChecked;

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintColor = new ColorARGB();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setARGB(255, 0, 0, 0);
        numColumns = 1;
        numRows = 1;
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
        cellChecked = new boolean[numColumns][numRows];
        pixels = new ColorARGB[numColumns][numRows];
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
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j]) {
                    ColorARGB c = pixels[i][j];
                    System.out.println("c: " + c.toHexString());
                    paint.setARGB(c.getA(), c.getR(), c.getG(), c.getB());
                    canvas.drawRect(
                        i * cellWidth,
                        j * cellHeight,
                        (i + 1) * cellWidth,
                        (j + 1) * cellHeight,
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

        paint.setARGB(paintColor.getA(), paintColor.getR(), paintColor.getG(),
                paintColor.getB());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int column = (int)(event.getX() / cellWidth);
        int row = (int)(event.getY() / cellHeight);

        cellChecked[column][row] = true;
        System.out.println("PAINT: " + paintColor.toString());
        pixels[column][row].setA(paintColor.getA());
        pixels[column][row].setR(paintColor.getR());
        pixels[column][row].setG(paintColor.getG());
        pixels[column][row].setB(paintColor.getB());

        invalidate();
        return true;
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        System.out.println("dragX: " + event.getX());
        System.out.println("dragY: " + event.getY());

        return true;
    }

    public void initializePixels() {
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                pixels[i][j] = new ColorARGB(0, 255, 255, 255);
            }
        }
    }

    public void setPaintColor(ColorARGB color) {
        paintColor = color;
        paint.setARGB(color.getA(), color.getR(), color.getG(), color.getB());
    }
}
