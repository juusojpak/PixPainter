package fi.tamk.jpak.pixpainter.fragments;

import fi.tamk.jpak.pixpainter.tools.ShapeFillType;
import fi.tamk.jpak.pixpainter.tools.ShapeType;

/**
 * Interface for listening changes in {@link fi.tamk.jpak.pixpainter.tools.Shape shape} setup.
 *
 * @author Juuso Pakarinen
 * @version 09.05.2017
 */
public interface OnShapeSetupChanged {

    /**
     * Called when {@link ShapeType} has been changed.
     * @param type Type of the shape.
     */
    void handleShapeChange(ShapeType type);

    /**
     * Called when {@link ShapeFillType} has been changed.
     * @param fillType Method of filling the inside of the shape.
     */
    void handleShapeFillChange(ShapeFillType fillType);

    /**
     * Called when width or height values have been changed.
     *
     * @param width Shape width.
     * @param height Shape height
     */
    void handleShapeSizeChange(int width, int height);
}
