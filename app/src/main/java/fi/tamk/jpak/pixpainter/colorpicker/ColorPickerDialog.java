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
 * Created by Juuso Pakarinen on 16/04/2017.
 */
public class ColorPickerDialog extends DialogFragment {

    private ColorARGB selectedColor;
    private ColorPickerListener cpListener;
    private SeekBar redBar, greenBar, blueBar;

    public ColorPickerDialog() {}

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

    @Override
    public void onResume() {

        try {

            /*
             * Fix layout dimensions to fit parent dialog
             */
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

            redBar = (SeekBar) getDialog().findViewById(R.id.redBar);
            greenBar = (SeekBar) getDialog().findViewById(R.id.greenBar);
            blueBar = (SeekBar) getDialog().findViewById(R.id.blueBar);

            ColorBarChangeListener barListener = new ColorBarChangeListener();
            redBar.setOnSeekBarChangeListener(barListener);
            greenBar.setOnSeekBarChangeListener(barListener);
            blueBar.setOnSeekBarChangeListener(barListener);

            if (selectedColor != null) {
                redBar.setProgress(selectedColor.getR());
                greenBar.setProgress(selectedColor.getG());
                blueBar.setProgress(selectedColor.getB());
                setColorTexts();
                setColorArea();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    public void setColorPickerListener(ColorPickerListener listener) {
        cpListener = listener;
    }

    public void setColorTexts() {
        String rStr = getString(R.string.redVal) + " " + selectedColor.getR();
        String gStr = getString(R.string.greenVal) + " " + selectedColor.getG();
        String bStr = getString(R.string.blueVal) + " " + selectedColor.getB();
        ((TextView) getDialog().findViewById(R.id.redText)).setText(rStr);
        ((TextView) getDialog().findViewById(R.id.greenText)).setText(gStr);
        ((TextView) getDialog().findViewById(R.id.blueText)).setText(bStr);
    }

    public void setColorArea() {
        View area = getDialog().findViewById(R.id.colorArea);
        int colorInt = Color.parseColor(selectedColor.toHexString());
        area.setBackgroundColor(colorInt);
    }

    public class ColorBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (seekBar.getId() == R.id.redBar) {
                selectedColor.setR(progress);
            } else if (seekBar.getId() == R.id.greenBar) {
                selectedColor.setG(progress);
            } else if (seekBar.getId() == R.id.blueBar) {
                selectedColor.setB(progress);
            }

            setColorTexts();
            setColorArea();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}
