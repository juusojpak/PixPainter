package fi.tamk.jpak.pixpainter.tools;

import java.util.LinkedList;
import java.util.Queue;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Paint bucket tool.
 *
 * Fills area of pixels that are the same color.
 *
 * @author Juuso Pakarinen
 * @version 25.04.2017
 */
public class PaintBucket extends Tool {

    /**
     * Default constructor.
     */
    public PaintBucket() {
        super(ToolType.FILL);
    }

    /**
     * Handle drawing.
     *
     * Fills all adjacent {@link Pixel pixels} that are the same exact color as origin.
     * Uses queue-based flood-fill algorithm.
     *
     * @param row Row of the origin point.
     * @param col Column of the origin point.
     * @param pixels Reference to pixel grid.
     * @param newColor Color that is set for the filled pixels.
     * @param targetColor Pixels with this color are filled.
     */
    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels,
                           ColorARGB newColor, ColorARGB targetColor) {

        if (pixels != null && pixels.length > 0) {
            boolean[][] marked = new boolean[pixels.length][pixels[0].length];

            if (isColorMatch(pixels[row][col].getColor(), targetColor)
                    && !marked[row][col]) {

                Queue<Pixel> queue = new LinkedList<>();
                queue.add(pixels[row][col]);

                while (!queue.isEmpty()) {
                    Pixel p = queue.remove();

                    if ((p.getX() >= 0) &&
                        (p.getX() < pixels[0].length) &&
                        (p.getY() >= 0) &&
                        (p.getY() < pixels.length)) {

                        if (isColorMatch(p.getColor(), targetColor)
                                && !marked[p.getY()][p.getX()]) {

                            marked[p.getY()][p.getX()] = true;
                            p.setColor(new ColorARGB(newColor.getA(),
                                    newColor.getR(), newColor.getG(),
                                    newColor.getB()));

                            if ((p.getX() + 1) < pixels[0].length) {
                                queue.add(pixels[p.getY()][p.getX() + 1]);
                            }

                            if ((p.getX() - 1) >= 0) {
                                queue.add(pixels[p.getY()][p.getX() - 1]);
                            }

                            if ((p.getY() + 1) < pixels.length) {
                                queue.add(pixels[p.getY() + 1][p.getX()]);
                            }

                            if ((p.getY() - 1) >= 0) {
                                queue.add(pixels[p.getY() - 1][p.getX()]);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Whether two colours are exactly the same.
     *
     * @param color1 Color 1.
     * @param color2 Color 2.
     * @return whether two colours are exactly the same.
     */
    public boolean isColorMatch(ColorARGB color1, ColorARGB color2) {
        return (color1.toHexString().equals(color2.toHexString()));
    }
}
