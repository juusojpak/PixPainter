package fi.tamk.jpak.pixpainter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
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
     * Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after
     *                            previously being shut down then this Bundle
     *                            contains the saved data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (OnToolSetupChanged) getActivity();
        System.out.println("ToolSetupFragment created");
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

        TextView sizeText = (TextView) view.findViewById(R.id.sizeText);
        Spinner sizeSpin = (Spinner) view.findViewById(R.id.sizeSpinner);
        sizeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        sizeSpin.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        sizeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedSize = 1 + position;
                callback.handleToolSetupChange(selectedSize);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }
}
