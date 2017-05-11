package fi.tamk.jpak.pixpainter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import fi.tamk.jpak.pixpainter.R;

/**
 * Fragment for custom canvas dimensions setup.
 *
 * @author Juuso Pakarinen
 * @version 06.05.2017
 */
public class CanvasSetupFragment extends Fragment {

    /**
     * Callback interface for sending input to
     * {@link fi.tamk.jpak.pixpainter.SetupActivity activity}.
     */
    private OnCanvasSetupChanged callback;

    /**
     * Array for holding chosen width and height values.
     */
    private int[] dimensions;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after
     *                            previously being shut down then this Bundle
     *                            contains the saved data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The {@link LayoutInflater} object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_canvassetup, container, false);
        EditText colEdit = (EditText) view.findViewById(R.id.colEdit);
        EditText rowEdit = (EditText) view.findViewById(R.id.rowEdit);
        dimensions = new int[] {1,1};
        callback = (OnCanvasSetupChanged) getActivity();
        colEdit.addTextChangedListener(new ValueWatcher(false));
        rowEdit.addTextChangedListener(new ValueWatcher(true));

        return view;
    }

    /**
     * Listener for changes in {@link EditText input fields}.
     */
    private class ValueWatcher implements TextWatcher {

        /**
         * Whether to check height input.
         */
        private boolean checkRows;

        /**
         * Constructor.
         * @param checkRows Whether to check height input.
         */
        public ValueWatcher(boolean checkRows) {
            this.checkRows = checkRows;
        }

        /**
         * This method is called to notify you that changes are about to happen.
         *
         * @param s {@link CharSequence Text} to be watched.
         * @param start Starting position.
         * @param count How many character are affected.
         * @param after Length of the text if intended changes are made.
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        /**
         * This method is called to notify you that changes have happened.
         *
         * @param s {@link CharSequence Text} to be watched.
         * @param start Starting position.
         * @param count How many character are affected.
         * @param before Length of replaced text.
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int pos = 0;
            if (checkRows) pos = 1;

            try {
                dimensions[pos] = Integer.parseInt(s.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                dimensions[pos] = 1;
            }

            callback.handleCanvasSetupChange(dimensions);
        }

        /**
         * This method is called to notify you that, somewhere within s, the text has been changed.
         * @param s Editable.
         */
        @Override
        public void afterTextChanged(Editable s) {}
    }
}
