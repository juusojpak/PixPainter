package fi.tamk.jpak.pixpainter.tools;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Created by Juuso Pakarinen on 25/04/2017.
 */
public class Brush extends Tool {

    private int strokeSize;
    private int[] fadeValues;

    public Brush() {
        super(ToolType.BRUSH);
        this.strokeSize = 1;
        updateFadeValues();
    }

    public Brush(int strokeSize) {
        super(ToolType.BRUSH);
        this.strokeSize = strokeSize;
        updateFadeValues();
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
        updateFadeValues();
    }

    public void updateFadeValues() {
        fadeValues = new int[strokeSize];

        for (int i = 0; i < strokeSize; i++) {
            fadeValues[i] = 5 * (i + 1);
        }
    }

    @Override
    public void handleDraw(int row, int col, Pixel[][] pixels,
                           ColorARGB color1, ColorARGB color2) {

        ColorARGB newColor = new ColorARGB(color1.getA(), color1.getR(),
                color1.getG(), color1.getB());

        int lineLenght = 1;
        int height = pixels.length;
        int width = pixels[0].length;

        pixels[row][col].setColor(newColor);

        for (int i = 1; i <= strokeSize; i++) {
            lineLenght += 2;

            /* Draw from upper left corner to right  */
            for (int j = 0; j < (lineLenght - 1); j++) {

                if ((row - i) >= 0
                        && (col - i) >= 0
                        && ((col - i) + j) < width) {

                    System.out.println("row: " + row);
                    System.out.println("col: " + col);
                    System.out.println(row - i);
                    System.out.println((col - i) + j);

                    pixels[row - i][(col - i) + j].blendColor(newColor, fadeValues[i - 1]);
                }
            }

            /* Draw from upper right corner to down  */
            for (int j = 0; j < (lineLenght - 1); j++) {

                if ((row - i) >= 0
                        && (col + i) < width
                        && ((row - i) + j) < height) {

                    pixels[(row - i) + j][col + i].blendColor(newColor, fadeValues[i - 1]);
                }
            }

            /* Draw from lower right corner to left  */
            for (int j = 0; j < (lineLenght - 1); j++) {

                if ((row + i) < height
                        && (col + i) < width
                        && ((col + i) - j) >= 0) {

                    pixels[row + i][(col + i) - j].blendColor(newColor, fadeValues[i - 1]);
                }
            }

            /* Draw from lower left corner to up  */
            for (int j = 0; j < (lineLenght - 1); j++) {

                if ((row + i) < height
                        && (col - i) >= 0
                        && ((row + i) - j) >= 0) {

                    pixels[(row + i) - j][col - i].blendColor(newColor, fadeValues[i - 1]);
                }
            }
        }
    }
}
