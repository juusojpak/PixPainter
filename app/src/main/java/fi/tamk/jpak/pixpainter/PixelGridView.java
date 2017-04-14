package fi.tamk.jpak.pixpainter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Juuso Pakarinen on 15/04/2017.
 */
public class PixelGridView extends View {

    private int numColumns, numRows;
    private float cellWidth, cellHeight;
    private Paint paint = new Paint();
    private boolean[][] cellChecked;

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

        System.out.println("DEVICE W: " + getWidth());
        System.out.println("DEVICE H: " + getHeight());

        cellWidth = (float) getWidth() / numColumns;
        cellHeight = (float) getHeight() / numRows;
        cellChecked = new boolean[numColumns][numRows];

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

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, paint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);

            cellChecked[column][row] = !cellChecked[column][row];
            invalidate();
        }

        return true;
    }
}
