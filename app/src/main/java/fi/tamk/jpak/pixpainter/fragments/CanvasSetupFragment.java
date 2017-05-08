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
 * Created by Juuso Pakarinen on 06/05/2017.
 */
public class CanvasSetupFragment extends Fragment {

    private OnCanvasSetupChanged callback;
    private int[] dimensions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("CanvasSetupFragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_canvassetup, container, false);
        EditText colEdit = (EditText) view.findViewById(R.id.colEdit);
        EditText rowEdit = (EditText) view.findViewById(R.id.rowEdit);
        dimensions = new int[] {1,1};
        callback = (OnCanvasSetupChanged) getActivity();

        colEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    dimensions[0] = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    dimensions[0] = 1;
                }

                System.out.println(dimensions[0] + ", " + dimensions[1]);
                callback.handleCanvasSetupChange(dimensions);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        rowEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    dimensions[1] = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    dimensions[1] = 1;
                }

                System.out.println(dimensions[0] + ", " + dimensions[1]);
                callback.handleCanvasSetupChange(dimensions);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }
}
