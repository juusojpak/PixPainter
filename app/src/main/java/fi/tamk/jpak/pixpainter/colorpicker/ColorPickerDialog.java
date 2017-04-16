package fi.tamk.jpak.pixpainter.colorpicker;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import fi.tamk.jpak.pixpainter.R;

/**
 * Created by Juuso Pakarinen on 16/04/2017.
 */
public class ColorPickerDialog extends DialogFragment {

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
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        super.onResume();
    }
}
