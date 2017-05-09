package fi.tamk.jpak.pixpainter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import fi.tamk.jpak.pixpainter.R;
import fi.tamk.jpak.pixpainter.tools.ShapeFillType;
import fi.tamk.jpak.pixpainter.tools.ShapeType;

/**
 * Created by Juuso Pakarinen on 9/05/2017.
 */
public class ShapeSetupFragment extends Fragment {

    private OnShapeSetupChanged callback;
    private LinearLayout sizeArea;
    private EditText widthEdit;
    private EditText heightEdit;
    private EditText radiusEdit;
    private int width;
    private int height;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (OnShapeSetupChanged) getActivity();
        System.out.println("ShapeSetupFragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shapesetup, container, false);
        sizeArea = (LinearLayout) view.findViewById(R.id.shapeSizeArea);
        heightEdit = (EditText) view.findViewById(R.id.heightEdit);
        widthEdit = (EditText) view.findViewById(R.id.widthEdit);
        radiusEdit = (EditText) view.findViewById(R.id.radiusEdit);
        Spinner shapeSpin = (Spinner) view.findViewById(R.id.shapeSpinner);
        Spinner fillSpin = (Spinner) view.findViewById(R.id.fillSpinner);
        width = 3;
        height = 3;

        shapeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    showRectangleSizeSetup();
                    callback.handleShapeChange(ShapeType.RECTANGLE);
                } else {
                    showCircleSizeSetup();
                    callback.handleShapeChange(ShapeType.CIRCLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        fillSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    callback.handleShapeFillChange(ShapeFillType.OUTLINE);
                } if (position == 0) {
                    callback.handleShapeFillChange(ShapeFillType.SOLID);
                } else {
                    callback.handleShapeFillChange(ShapeFillType.FILL);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    public void showRectangleSizeSetup() {
        sizeArea.removeAllViewsInLayout();
        sizeArea.addView(heightEdit);
        sizeArea.addView(widthEdit);
    }

    public void showCircleSizeSetup() {
        sizeArea.removeAllViewsInLayout();
        sizeArea.addView(radiusEdit);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("ToolSetupFragment on attach");
    }

    private class ValueWatcher implements TextWatcher {
        public int checkMode;

        public ValueWatcher(int checkMode) {
            this.checkMode = checkMode;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (checkMode <= 0) {
                width = Integer.parseInt(s.toString());
            } else if (checkMode == 1) {
                height = Integer.parseInt(s.toString());
            } else {
                width = Integer.parseInt(s.toString());
                height = Integer.parseInt(s.toString());
            }

            callback.handleShapeSizeChange(width, height);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }
}
