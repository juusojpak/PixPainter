package fi.tamk.jpak.pixpainter.colorpicker;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import fi.tamk.jpak.pixpainter.ColorARGB;
import fi.tamk.jpak.pixpainter.R;

/**
 * Created by Juuso Pakarinen on 16/04/2017.
 */
public class ColorPickerDialog extends DialogFragment {

    private ColorARGB selectedColor;
    private ColorPickerListener cpListener;

    public ColorPickerDialog() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.color_dialog, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getDialog().setTitle("Color Picker");
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
                    System.out.println("CANCEL");
                    dismiss();
                }
            });

            Button selectButton = (Button) getDialog().findViewById(R.id.selectButton);
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("SELECT");

                    if (cpListener != null) {
                        System.out.println("ColorPicker NOT NULL");
                        cpListener.onColorChanged(selectedColor);
                    }

                    dismiss();
                }
            });

            SeekBar redBar = (SeekBar) getDialog().findViewById(R.id.redBar);
            SeekBar greenBar = (SeekBar) getDialog().findViewById(R.id.greenBar);
            SeekBar blueBar = (SeekBar) getDialog().findViewById(R.id.blueBar);
            ColorBarChangeListener barListener = new ColorBarChangeListener();
            redBar.setOnSeekBarChangeListener(barListener);
            greenBar.setOnSeekBarChangeListener(barListener);
            blueBar.setOnSeekBarChangeListener(barListener);

            selectedColor = new ColorARGB();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    public void setColorPickerListener(ColorPickerListener listener) {
        cpListener = listener;
    }

    public class ColorBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            System.out.println(seekBar);
            System.out.println(progress);

            if (seekBar.getId() == R.id.redBar) {
                selectedColor.setR(progress);
            } else if (seekBar.getId() == R.id.greenBar) {
                selectedColor.setG(progress);
            } else if (seekBar.getId() == R.id.blueBar) {
                selectedColor.setB(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}
