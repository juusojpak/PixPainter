package fi.tamk.jpak.pixpainter.colorpicker;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;

/**
 * Created by Juuso Pakarinen on 17/04/2017.
 */
public interface ColorPickerListener {

    void onColorChanged(ColorARGB color);
}
