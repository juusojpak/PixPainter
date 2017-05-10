package fi.tamk.jpak.pixpainter.tools;

import android.support.annotation.Nullable;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.Pixel;

/**
 * Base class for editor tools.
 *
 * @author Juuso Pakarinen
 * @version 24.04.2017
 */
public abstract class Tool {

    /**
     * Type of the tool.
     */
    private ToolType type;

    /**
     * Size of the tool stroke.
     */
    private int strokeSize;

    /**
     * Default constructor. Calls main constructor.
     */
    public Tool() {
        this(null, 1);
    }

    /**
     * Constructor. Calls main constructor.
     *
     * @param type Type of the tool.
     */
    public Tool(ToolType type){
        this(type, 1);
    }

    /**
     * Main constructor.
     *
     * @param type Type of the tool.
     * @param strokeSize Size of the tool stroke.
     */
    public Tool(ToolType type, int strokeSize){
        this.type = type;
        if (strokeSize > 0 && strokeSize < 100) {
            this.strokeSize = strokeSize;
        } else {
            this.strokeSize = 1;
        }
    }

    /**
     * Returns type of the tool.
     * @return type of the tool.
     */
    public ToolType getType() {
        return type;
    }

    /**
     * Sets type of the tool.
     * @param type Type of the tool.
     */
    public void setType(ToolType type) {
        this.type = type;
    }

    /**
     * Returns size of the tool stroke.
     * @return size of the tool stroke.
     */
    public int getStrokeSize() {
        return strokeSize;
    }

    /**
     * Sets size of the tool stroke.
     * @param strokeSize Size of the tool stroke.
     */
    public void setStrokeSize(int strokeSize) {
        if (strokeSize > 0 && strokeSize < 100) this.strokeSize = strokeSize;
    }

    /**
     * Handle drawing.
     *
     * Implemented in subclasses.
     *
     * @param row Row of the origin point.
     * @param col Column of the origin point.
     * @param pixels Reference to pixel grid.
     * @param color1 Primary color.
     * @param color2 Secondary color.
     */
    public abstract void handleDraw(int row, int col, Pixel[][] pixels,
                                    ColorARGB color1, ColorARGB color2);

    /**
     * Handle drawing with stroke.
     *
     * Draw so that adjacent pixels of the origin are drawn according to the
     * stroke size. Selected color can either blend with or replace the color
     * of adjacent pixels and it can be faded gradually.
     *
     * @param row Row of the origin point.
     * @param col Column of the origin point.
     * @param pixels Reference to pixel grid.
     * @param color Selected color.
     * @param blend Whether to blend with or replace the color
     *              of adjacent pixels
     * @param fadeValues Array holding fade values. Can be null.
     */
    public void handleStroke(int row, int col, Pixel[][] pixels,
                             ColorARGB color, boolean blend, @Nullable int[] fadeValues) {

        int lineLenght = 1;
        int height = pixels.length;
        int width = pixels[0].length;
        pixels[row][col].setColor(color);

        if (strokeSize > 1) {

            for (int i = 1; i < strokeSize; i++) {
                lineLenght += 2;

                /* Draw from upper left corner to right  */
                for (int j = 0; j < (lineLenght - 1); j++) {
                    if ((row - i) >= 0 && (col - i) >= 0 && ((col - i) + j) < width) {

                        if (blend) {
                            if (fadeValues != null) {
                                pixels[row - i][(col - i) + j].blendColor(color, fadeValues[i]);
                            } else {
                                pixels[row - i][(col - i) + j].blendColor(color, 0);
                            }
                        } else {
                            pixels[row - i][(col - i) + j].setColor(new ColorARGB(
                                    color.getA(), color.getR(),
                                    color.getG(), color.getB()));
                        }
                    }
                }

                /* Draw from upper right corner to down  */
                for (int j = 0; j < (lineLenght - 1); j++) {
                    if ((row - i) >= 0 && (col + i) < width && ((row - i) + j) < height) {

                        if (blend) {
                            if (fadeValues != null) {
                                pixels[(row - i) + j][col + i].blendColor(color, fadeValues[i]);
                            } else {
                                pixels[(row - i) + j][col + i].blendColor(color, 0);
                            }
                        } else {
                            pixels[(row - i) + j][col + i].setColor(new ColorARGB(
                                    color.getA(), color.getR(),
                                    color.getG(), color.getB()));
                        }
                    }
                }

                /* Draw from lower right corner to left  */
                for (int j = 0; j < (lineLenght - 1); j++) {
                    if ((row + i) < height && (col + i) < width && ((col + i) - j) >= 0) {

                        if (blend) {
                            if (fadeValues != null) {
                                pixels[row + i][(col + i) - j].blendColor(color, fadeValues[i]);
                            } else {
                                pixels[row + i][(col + i) - j].blendColor(color, 0);
                            }
                        } else {
                            pixels[row + i][(col + i) - j].setColor(new ColorARGB(
                                    color.getA(), color.getR(),
                                    color.getG(), color.getB()));
                        }
                    }
                }

                /* Draw from lower left corner to up  */
                for (int j = 0; j < (lineLenght - 1); j++) {
                    if ((row + i) < height && (col - i) >= 0 && ((row + i) - j) >= 0) {

                        if (blend) {
                            if (fadeValues != null) {
                                pixels[(row + i) - j][col - i].blendColor(color, fadeValues[i]);
                            } else {
                                pixels[(row + i) - j][col - i].blendColor(color, 0);
                            }
                        } else {
                            pixels[(row + i) - j][col - i].setColor(new ColorARGB(
                                    color.getA(), color.getR(),
                                    color.getG(), color.getB()));
                        }
                    }
                }
            }
        }
    }
}
