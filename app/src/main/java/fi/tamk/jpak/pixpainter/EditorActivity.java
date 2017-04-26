package fi.tamk.jpak.pixpainter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fi.tamk.jpak.pixpainter.colorpicker.ColorPickerDialog;
import fi.tamk.jpak.pixpainter.colorpicker.ColorPickerListener;
import fi.tamk.jpak.pixpainter.tools.Brush;
import fi.tamk.jpak.pixpainter.tools.PaintBucket;
import fi.tamk.jpak.pixpainter.tools.Pencil;
import fi.tamk.jpak.pixpainter.tools.Shape;
import fi.tamk.jpak.pixpainter.tools.Tool;

public class EditorActivity extends AppCompatActivity implements ColorPickerListener {

    private PixelGridView pixelgrid;
    private ArrayList<Button> toolButtons;
    private ColorARGB primaryColor;
    private ColorARGB secondaryColor;
    private Tool activeTool;
    private Pencil pencilTool;
    private Brush brushTool;
    private Shape shapeTool;
    private PaintBucket bucketTool;
    private AlertDialog clearAlert;

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
        brushTool = new Brush(3);
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

    public void handleBrushClick(View v) {
        this.activeTool = brushTool;
        updatePixelGrid();
        highlightToolButton();
    }

    public void handleMenuClick(View v) {
        showOperationsMenu(v);
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

        switch (activeTool.getType()) {
            case PEN:
                toolButtons.get(0).setTextColor(activeColor);
                break;
            case BRUSH:
                toolButtons.get(1).setTextColor(activeColor);
                break;
            case SHAPE:
                toolButtons.get(2).setTextColor(activeColor);
                break;
            case FILL:
                toolButtons.get(3).setTextColor(activeColor);
                break;
        }
    }

    public void showOperationsMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.operations_menu);
        invalidateOptionsMenu();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.saveLocal:
                        saveImageToFile(false);
                        return true;
                    case R.id.exportGallery:
                        saveImageToFile(true);
                        return true;
                    case R.id.clearEditor:
                        clearImage();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }

    public void saveImageToFile(boolean exportToGallery) {
        FileOutputStream outStream = null;

        //Define a bitmap with the same size as the view
        Bitmap bitmap = Bitmap.createBitmap(pixelgrid.getWidth(),
                pixelgrid.getHeight(), Bitmap.Config.ARGB_8888);

        //Bind a canvas to it
        Canvas canvas = new Canvas(bitmap);

        //Get the view's background
        Drawable bgDrawable = pixelgrid.getBackground();

        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }

        // draw the view on the canvas
        pixelgrid.draw(canvas);

        try {

            if (exportToGallery) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String dateStr = dateFormat.format(new Date());
                System.out.println(dateStr);
                String fileName = dateStr +".jpg";

                // Bitmap to Gallery (as .jpg)
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                        fileName, ("PixPainter image, created " + dateStr));

                Toast.makeText(this,
                        getResources().getString(R.string.exportedToGalleryToast),
                        Toast.LENGTH_SHORT).show();
            } else {
                outStream =  this.openFileOutput("pixpainter_save.png", Context.MODE_PRIVATE);

                // Bitmap to file
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
            }

        } catch (FileNotFoundException | SecurityException e) {
            e.printStackTrace();

        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear image");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                pixelgrid.initializePixels();
                pixelgrid.invalidate();
            }
        });
        builder.setNegativeButton("No", null);

        clearAlert = builder.create();
        clearAlert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                clearAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xff3f8c00);
                clearAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xffe22b28);
            }
        });

        clearAlert.show();
    }
}
