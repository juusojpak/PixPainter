package fi.tamk.jpak.pixpainter.fragments;

import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
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
 * Fragment for shape setup.
 *
 * @author Juuso Pakarinen
 * @version 09.05.2017
 */
public class ShapeSetupFragment extends Fragment {

    /**
     * Callback interface for handling changes in shape setup.
     */
    private OnShapeSetupChanged callback;

    /**
     * Container for shape size {@link EditText input fields}.
     */
    private PercentRelativeLayout sizeArea;

    /**
     * Input for width.
     */
    private EditText widthEdit;

    /**
     * Input for height.
     */
    private EditText heightEdit;

    /**
     * Input for radius. Radius is treated as width.
     */
    private EditText radiusEdit;

    /**
     * Current set state of width or radius.
     */
    private int width;

    /**
     * Current state of height.
     */
    private int height;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after
     *                            previously being shut down then this Bundle
     *                            contains the saved data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (OnShapeSetupChanged) getActivity();
        System.out.println("ShapeSetupFragment created");
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

        View view = inflater.inflate(R.layout.fragment_shapesetup, container, false);
        sizeArea = (PercentRelativeLayout) view.findViewById(R.id.shapeSizeArea);

        heightEdit = (EditText) view.findViewById(R.id.heightEdit);
        widthEdit = (EditText) view.findViewById(R.id.widthEdit);
        radiusEdit = (EditText) view.findViewById(R.id.radiusEdit);
        widthEdit.addTextChangedListener(new ValueWatcher(0));
        heightEdit.addTextChangedListener(new ValueWatcher(1));
        radiusEdit.addTextChangedListener(new ValueWatcher(2));

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
                switch (position) {
                    case 0:
                        callback.handleShapeFillChange(ShapeFillType.OUTLINE);
                        break;
                    case 1:
                        callback.handleShapeFillChange(ShapeFillType.SOLID);
                        break;
                    case 2:
                        callback.handleShapeFillChange(ShapeFillType.FILL);
                        break;
                    default:
                        callback.handleShapeFillChange(ShapeFillType.OUTLINE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    /**
     * Show input fields for width and height.
     */
    public void showRectangleSizeSetup() {
        sizeArea.removeAllViewsInLayout();
        sizeArea.addView(heightEdit);
        sizeArea.addView(widthEdit);
    }

    /**
     * Show input fields for radius (affects width).
     */
    public void showCircleSizeSetup() {
        sizeArea.removeAllViewsInLayout();
        sizeArea.addView(radiusEdit);
    }

    /**
     * Listener for changes in {@link EditText input fields}.
     */
    private class ValueWatcher implements TextWatcher {

        /**
         * Whether to check width, height or radius input.
         */
        public int checkMode;

        /**
         * Constructor.
         * @param checkMode Whether to check width, height or radius input.
         */
        public ValueWatcher(int checkMode) {
            this.checkMode = checkMode;
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
            if (s.length() > 0) {
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
        }

        /**
         * This method is called to notify you that, somewhere within s, the text has been changed.
         * @param s Editable.
         */
        @Override
        public void afterTextChanged(Editable s) {}
    }
}
