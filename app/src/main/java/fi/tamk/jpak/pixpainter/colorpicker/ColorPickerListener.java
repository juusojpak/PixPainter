package fi.tamk.jpak.pixpainter.colorpicker;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;

/**
 * Listener interface for {@link ColorPickerDialog}.
 *
 * @author Juuso Pakarinen
 * @version 17.04.2017
 */
public interface ColorPickerListener {

    /**
     * Called when color or alpha values have been changed.
     * @param color Color received from the {@link ColorPickerDialog color picker}.
     */
    void onColorChanged(ColorARGB color);
}
