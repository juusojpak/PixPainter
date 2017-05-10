package fi.tamk.jpak.pixpainter.colorpicker;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.R;

/**
 * @author Juuso Pakarinen
 * @version 16.04.2017
 *
 * Dialog for selecting color.
 *
 * Contains {@link SeekBar sliders} for setting current primary colors argb values.
 */
public class ColorPickerDialog extends DialogFragment {

    /**
     * Current selected color.
     */
    private ColorARGB selectedColor;

    /**
     * Callback interface for setting selected color as active
     * {@link fi.tamk.jpak.pixpainter.DrawingView#primaryColor color} in editor.
     */
    private ColorPickerListener cpListener;

    /**
     * Default constructor.
     */
    public ColorPickerDialog() {}

    /**
     * Creates new instance of color picker dialog to show in
     * {@link fi.tamk.jpak.pixpainter.EditorActivity#handleColorPickerClick(View) editor}.
     * Sets current editor primart color as selected color in dialog.
     *
     * @param selectedColor Current primary color in editor.
     * @return new instance of {@link ColorPickerDialog}.
     */
    public static ColorPickerDialog newInstance(ColorARGB selectedColor) {
        ColorPickerDialog cpd = new ColorPickerDialog();
        Bundle args = new Bundle();
        args.putInt("a", selectedColor.getA());
        args.putInt("r", selectedColor.getR());
        args.putInt("g", selectedColor.getG());
        args.putInt("b", selectedColor.getB());
        cpd.setArguments(args);

        return cpd;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedColor = new ColorARGB();
        selectedColor.setA(getArguments().getInt("a"));
        selectedColor.setR(getArguments().getInt("r"));
        selectedColor.setG(getArguments().getInt("g"));
        selectedColor.setB(getArguments().getInt("b"));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.color_dialog, container);
    }

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause().
     *
     * Initializes dialog views and sets listeners.
     */
    @Override
    public void onResume() {

        try {

            /* Fix layout dimensions to fit parent dialog */
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

            Button cancelButton = (Button) getDialog().findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            Button selectButton = (Button) getDialog().findViewById(R.id.selectButton);
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cpListener != null) {
                        cpListener.onColorChanged(selectedColor);
                    }

                    dismiss();
                }
            });

            SeekBar alphaBar = (SeekBar) getDialog().findViewById(R.id.alphaBar);
            SeekBar redBar = (SeekBar) getDialog().findViewById(R.id.redBar);
            SeekBar greenBar = (SeekBar) getDialog().findViewById(R.id.greenBar);
            SeekBar blueBar = (SeekBar) getDialog().findViewById(R.id.blueBar);

            ColorBarChangeListener barListener = new ColorBarChangeListener();
            alphaBar.setOnSeekBarChangeListener(barListener);
            redBar.setOnSeekBarChangeListener(barListener);
            greenBar.setOnSeekBarChangeListener(barListener);
            blueBar.setOnSeekBarChangeListener(barListener);

            if (selectedColor != null) {
                alphaBar.setProgress(selectedColor.getA());
                redBar.setProgress(selectedColor.getR());
                greenBar.setProgress(selectedColor.getG());
                blueBar.setProgress(selectedColor.getB());
                setColorTexts();
                setColorAreas(true);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    /**
     * Setter for callback interface.
     * @param listener Callback interface.
     */
    public void setColorPickerListener(ColorPickerListener listener) {
        cpListener = listener;
    }

    /**
     * Update {@link TextView textViews} representing selected alpha and color values.
     */
    public void setColorTexts() {
        String aStr = getString(R.string.alphaVal) + " " + selectedColor.getA();
        String rStr = getString(R.string.redVal) + " " + selectedColor.getR();
        String gStr = getString(R.string.greenVal) + " " + selectedColor.getG();
        String bStr = getString(R.string.blueVal) + " " + selectedColor.getB();
        ((TextView) getDialog().findViewById(R.id.alphaText)).setText(aStr);
        ((TextView) getDialog().findViewById(R.id.redText)).setText(rStr);
        ((TextView) getDialog().findViewById(R.id.greenText)).setText(gStr);
        ((TextView) getDialog().findViewById(R.id.blueText)).setText(bStr);
    }

    /**
     * Update background colors of the two areas representing current primary
     * color in editor and selected color in the dialog.
     *
     * @param setCurrent Whether the area showing current primary color in
     *                   editor should be updated. Should only be true when this
     *                   is called upon opening dialog.
     */
    public void setColorAreas(boolean setCurrent) {
        int colorInt = Color.parseColor(selectedColor.toHexString());
        TextView newColor = (TextView) getDialog().findViewById(R.id.colorArea);
        newColor.setBackgroundColor(colorInt);

        if (setCurrent) {
            TextView currentColor = (TextView) getDialog().findViewById(R.id.currentColorArea);
            currentColor.setBackgroundColor(colorInt);
            if (isDark()) {
                currentColor.setTextColor(0xffffffff);
            } else {
                currentColor.setTextColor(0xff000000);
            }
        }

        if (isDark()) {
            newColor.setTextColor(0xffffffff);
        } else {
            newColor.setTextColor(0xff000000);
        }
    }

    /**
     * Return whether current selected color is dark.
     *
     * Used to determine if text on this color should be light or dark.
     *
     * @return whether current selected color is dark.
     */
    public boolean isDark() {
        return (selectedColor.getR() < 60 &&
                selectedColor.getG() < 60 &&
                selectedColor.getB() < 60);
    }

    /**
     * {@link android.widget.SeekBar.OnSeekBarChangeListener Listener} for seekbar
     * changes.
     */
    public class ColorBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * Notification that the progress level has changed.
         *
         * @param seekBar Bar whose progress has changed.
         * @param progress The current progress level.
         * @param fromUser True if the progress change was initiated by the user.
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.alphaBar:
                    selectedColor.setA(progress);
                    break;
                case R.id.redBar:
                    selectedColor.setR(progress);
                    break;
                case R.id.greenBar:
                    selectedColor.setG(progress);
                    break;
                case R.id.blueBar:
                    selectedColor.setB(progress);
                    break;
                default:
                    break;
            }

            setColorTexts();
            setColorAreas(false);
        }

        /**
         * Notification that the user has started a touch gesture.
         * @param seekBar Bar in which the touch gesture began.
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        /**
         * Notification that the user has finished a touch gesture.
         * @param seekBar Bar in which the touch gesture began.
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}
