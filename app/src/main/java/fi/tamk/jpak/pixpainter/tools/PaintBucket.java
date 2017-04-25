package fi.tamk.jpak.pixpainter.tools;

import java.util.LinkedList;
import java.util.Queue;

import fi.tamk.jpak.pixpainter.ColorARGB;
import fi.tamk.jpak.pixpainter.Pixel;

/**
 * Created by Juuso Pakarinen on 25/04/2017.
 */
public class PaintBucket extends Tool {

    public PaintBucket() {
        super(ToolType.FILL, 1);
    }

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
                            p.setColor(newColor);

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

    public boolean isColorMatch(ColorARGB color1, ColorARGB color2) {
        return (color1.toHexString().equals(color2.toHexString()));
    }
}
