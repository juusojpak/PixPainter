package fi.tamk.jpak.pixpainter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import fi.tamk.jpak.pixpainter.colorpicker.ColorPickerDialog;
import fi.tamk.jpak.pixpainter.colorpicker.ColorPickerListener;

public class EditorActivity extends AppCompatActivity implements ColorPickerListener {

    private PixelGridView pixelgrid;
    private ColorARGB primaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        int cols = 20;
        int rows = 24;

        if (intent != null) {
            cols = intent.getExtras().getInt("columns");
            rows = intent.getExtras().getInt("rows");
        }

        pixelgrid = new PixelGridView(this, null);
        pixelgrid.setNumColumns(cols);
        pixelgrid.setNumRows(rows);
        primaryColor = new ColorARGB();

        LinearLayout editorLayout = (LinearLayout) findViewById(R.id.rootLayout)
                .findViewById(R.id.editorLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        if (editorLayout != null) {
            editorLayout.addView(pixelgrid, params);
        }
    }

    public void handleColorPickerClick(View v) {
        ColorPickerDialog cpDialog = ColorPickerDialog.newInstance(primaryColor);
        cpDialog.setColorPickerListener(this);
        cpDialog.show(getFragmentManager(), "ColorPickerDialog");
    }

    @Override
    public void onColorChanged(ColorARGB color) {
        System.out.println("Color changed: " + color.toHexString());
        System.out.println(color.toString());
        this.primaryColor = color;
        pixelgrid.setPaintColor(color);
    }
}
