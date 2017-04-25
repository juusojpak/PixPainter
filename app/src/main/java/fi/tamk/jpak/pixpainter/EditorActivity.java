package fi.tamk.jpak.pixpainter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import fi.tamk.jpak.pixpainter.colorpicker.ColorPickerDialog;
import fi.tamk.jpak.pixpainter.colorpicker.ColorPickerListener;
import fi.tamk.jpak.pixpainter.tools.PaintBucket;
import fi.tamk.jpak.pixpainter.tools.Pencil;
import fi.tamk.jpak.pixpainter.tools.Shape;
import fi.tamk.jpak.pixpainter.tools.Tool;
import fi.tamk.jpak.pixpainter.tools.ToolType;

public class EditorActivity extends AppCompatActivity implements ColorPickerListener {

    private PixelGridView pixelgrid;
    private ArrayList<Button> toolButtons;
    private ColorARGB primaryColor;
    private ColorARGB secondaryColor;
    private Tool activeTool;
    private Pencil pencilTool;
    private Shape shapeTool;
    private PaintBucket bucketTool;

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
        toolButtons = new ArrayList<>();

        primaryColor = new ColorARGB(255, 0, 0, 0);
        secondaryColor = new ColorARGB(255, 255, 255, 255);
        pixelgrid.setColors(primaryColor, secondaryColor);

        pencilTool = new Pencil();
        shapeTool = new Shape();
        bucketTool = new PaintBucket();
        activeTool = pencilTool;
        pixelgrid.setTool(activeTool);

        LinearLayout editorLayout = (LinearLayout) findViewById(R.id.rootLayout)
                .findViewById(R.id.editorLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        if (editorLayout != null) {
            editorLayout.addView(pixelgrid, params);
        }

        LinearLayout toolbarLayout = (LinearLayout) findViewById(R.id.toolbarLayout);

        for (int i = 0; i < toolbarLayout.getChildCount(); i++) {
            View v = toolbarLayout.getChildAt(i);
            if (v instanceof Button) {
                Button b = (Button) v;
                toolButtons.add(b);
            }
        }

        highlightToolButton();
    }

    public void handleColorPickerClick(View v) {
        ColorPickerDialog cpDialog = ColorPickerDialog.newInstance(primaryColor);
        cpDialog.setColorPickerListener(this);
        cpDialog.show(getFragmentManager(), "ColorPickerDialog");
    }

    public void handlePencilClick(View v) {
        this.activeTool = pencilTool;
        updatePixelGrid();
        highlightToolButton();
    }

    public void handleShapeClick(View v) {
        this.activeTool = shapeTool;
        updatePixelGrid();
        highlightToolButton();
    }

    public void handleFillClick(View v) {
        this.activeTool = bucketTool;
        updatePixelGrid();
        highlightToolButton();
    }

    @Override
    public void onColorChanged(ColorARGB color) {
        this.primaryColor = color;
        updatePixelGrid();
    }

    public void updatePixelGrid() {
        pixelgrid.setTool(activeTool);
        pixelgrid.setColors(primaryColor, secondaryColor);
    }

    public void highlightToolButton() {
        for (int i = 0; i < toolButtons.size(); i++) {
            toolButtons.get(i).setTextColor(
                    ContextCompat.getColor(this, R.color.colorAccent));
        }

        int activeColor = 0xffed9135;

        if (activeTool.getType() == ToolType.PEN) {
            toolButtons.get(1).setTextColor(activeColor);
        } else if (activeTool.getType() == ToolType.SHAPE) {
            toolButtons.get(2).setTextColor(activeColor);
        } else if (activeTool.getType() == ToolType.FILL) {
            toolButtons.get(3).setTextColor(activeColor);
        }
    }
}
