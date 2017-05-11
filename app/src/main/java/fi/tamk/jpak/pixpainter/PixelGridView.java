package fi.tamk.jpak.pixpainter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;

/**
 * View that is used for displaying pixel grid below the {@link DrawingView drawing}.
 *
 * With the exact same dimensions as the {@link DrawingView} and the placement right
 * under it, this view draws {@link DrawingView#pixels pixel grid's} lines to
 * help the user to better comprehend pixel sizes and locations.
 *
 * @author Juuso Pakarinen
 * @version 27.04.2017
 */
public class PixelGridView extends AppCompatImageView {

    /**
     * Number of columns and rows.
     */
    private int numColumns, numRows;

    /**
     * Width and height of a single grid cell.
     */
    private float cellWidth, cellHeight;

    /**
     * Paint used to draw the gird lines to the view.
     */
    private Paint paint;

    /**
     * Constructor.
     *
     * @param context Context.
     * @param attrs Attribute set.
     */
    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paint.setARGB(255, 0, 0, 0);
        this.numColumns = 20;
        this.numRows = 24;
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
     * Calculates and sets pixel grid dimensions.
     *
     * Validates number of rows and columns and calculates width and height
     * of a single cell based on them.
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
        invalidate();
    }

    /**
     * Handles drawing on the {@link android.view.View view's} {@link Canvas canvas}.
     *
     * Draws the lines of the {@link DrawingView#pixels grid} separating columns
     * and rows.
     *
     * @param canvas View's canvas.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, paint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, paint);
        }
    }
}
