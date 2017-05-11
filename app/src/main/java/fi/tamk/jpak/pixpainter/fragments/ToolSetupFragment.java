package fi.tamk.jpak.pixpainter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fi.tamk.jpak.pixpainter.R;

/**
 * Fragment for tool stroke size setup.
 *
 * @author Juuso Pakarinen
 * @version 27.04.2017
 */
public class ToolSetupFragment extends Fragment {

    /**
     * Callback interface for handling changes in tool setup.
     */
    private OnToolSetupChanged callback;

    /**
     * View showing current tool stroke size.
     */
    private TextView sizeText;

    /**
     * Selected stroke ize.
     */
    private int size;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after
     *                            previously being shut down then this Bundle
     *                            contains the saved data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (OnToolSetupChanged) getActivity();
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Initializes views and sets listeners.
     *
     * @param inflater The {@link LayoutInflater} object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_toolsetup, container, false);
        size = 1;
        callback.handleToolSetupChange(size);

        sizeText = (TextView) view.findViewById(R.id.sizeText);
        sizeText.setText(getString(R.string.size, getSizeDisplayNumber()));
        sizeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size++;
                if (size > 5 || size < 1) size = 1;
                callback.handleToolSetupChange(size);
                sizeText.setText(getString(R.string.size, getSizeDisplayNumber()));
            }
        });

        return view;
    }

    /**
     * Returns integer that is showed as size to user.
     * @return integer that is showed as size to user.
     */
    public int getSizeDisplayNumber() {
        return (1 + ((size - 1) * 2));
    }
}
