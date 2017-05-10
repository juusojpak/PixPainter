package fi.tamk.jpak.pixpainter.fragments;

/**
 * Interface for listening changes in {@link fi.tamk.jpak.pixpainter.tools.Tool tool} setup.
 *
 * @author Juuso Pakarinen
 * @version 27.04.2017
 */
public interface OnToolSetupChanged {

    /**
     * Called when {@link fi.tamk.jpak.pixpainter.tools.Tool#strokeSize stroke size}
     * has been changed.
     *
     * @param size Size of the stroke.
     */
    void handleToolSetupChange(int size);
}
