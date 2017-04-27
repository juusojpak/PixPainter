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

    private OnSetupChanged callback;
    private Spinner sizeSpin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (OnSetupChanged) getActivity();
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
                int selectedSize = 1;

                try {
                    TextView text = (TextView) view;

                    if (text != null) {
                        String str = text.getText().toString();
                        selectedSize = Integer.parseInt(str.substring(0,1));
                    }
                } catch (ClassCastException | NumberFormatException | NullPointerException e) {
                    e.printStackTrace();
                }

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

    public void updateSelectionToStokeSize(int strokeSize) {

        if (sizeSpin != null) {
            switch (strokeSize) {
                case 3:
                    sizeSpin.setSelection(1);
                    break;
                case 5:
                    sizeSpin.setSelection(2);
                    break;
                case 7:
                    sizeSpin.setSelection(3);
                    break;
                case 9:
                    sizeSpin.setSelection(4);
                    break;
                default:
                    sizeSpin.setSelection(0);
                    break;
            }
        }
    }
}
