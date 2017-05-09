package fi.tamk.jpak.pixpainter.fragments;

import fi.tamk.jpak.pixpainter.tools.ShapeFillType;
import fi.tamk.jpak.pixpainter.tools.ShapeType;

/**
 * Created by Juuso on 9.5.2017.
 */
public interface OnShapeSetupChanged {

    void handleShapeChange(ShapeType type);
    void handleShapeFillChange(ShapeFillType fillType);
    void handleShapeSizeChange(int width, int height);
}
