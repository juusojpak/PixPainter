package fi.tamk.jpak.pixpainter.fragments;

/**
 * Interface for listening changes in {@link CanvasSetupFragment custom cannvas setup}.
 *
 * @author Juuso Pakarinen
 * @version 06.05.2017
 */
public interface OnCanvasSetupChanged {

    /**
     * Called when width or height values have been changed.
     * @param dimensions Width and height values for canvas.
     */
    void handleCanvasSetupChange(int[] dimensions);
}
