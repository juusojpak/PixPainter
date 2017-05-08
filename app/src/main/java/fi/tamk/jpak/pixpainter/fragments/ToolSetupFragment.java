package fi.tamk.jpak.pixpainter.fragments;

import android.content.Context;
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
 * Created by Juuso Pakarinen on 27/04/2017.
 */
public class ToolSetupFragment extends Fragment {

    private OnToolSetupChanged callback;
    private Spinner sizeSpin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (OnToolSetupChanged) getActivity();
        System.out.println("ToolSetupFragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_toolsetup, container, false);

        TextView sizeText = (TextView) view.findViewById(R.id.sizeText);
        sizeSpin = (Spinner) view.findViewById(R.id.sizeSpinner);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("ToolSetupFragment on attach");
    }
}
