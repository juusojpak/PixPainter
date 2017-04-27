package fi.tamk.jpak.pixpainter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
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
import fi.tamk.jpak.pixpainter.utils.ColorARGB;

public class EditorActivity extends AppCompatActivity implements ColorPickerListener {

    private DrawingView drawing;
    private PixelGridView grid;
    private ArrayList<Button> toolButtons;
    private ColorARGB primaryColor;
    private ColorARGB secondaryColor;
    private Tool activeTool;
    private Pencil pencilTool;
    private Brush brushTool;
    private Shape shapeTool;
    private PaintBucket bucketTool;
    private AlertDialog clearAlert;
    private int cols, rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        cols = 20;
        rows = 24;

        if (intent != null) {
            cols = intent.getExtras().getInt("columns");
            rows = intent.getExtras().getInt("rows");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        grid = (PixelGridView) findViewById(R.id.pixelGridView);
        drawing = (DrawingView) findViewById(R.id.drawingView);
        setEditorDimensions();
        toolButtons = new ArrayList<>();
        primaryColor = new ColorARGB(255, 0, 0, 0);
        secondaryColor = new ColorARGB(255, 255, 255, 255);
        drawing.setColors(primaryColor, secondaryColor);

        pencilTool = new Pencil();
        brushTool = new Brush(3);
        shapeTool = new Shape();
        bucketTool = new PaintBucket();
        activeTool = pencilTool;
        drawing.setTool(activeTool);

        /*
        LinearLayout editorLayout = (LinearLayout) findViewById(R.id.rootLayout)
                .findViewById(R.id.editorLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        if (editorLayout != null) {
            editorLayout.addView(drawing, params);
        }
        */

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
        updateDrawingView();
        highlightToolButton();
    }

    public void handleShapeClick(View v) {
        this.activeTool = shapeTool;
        updateDrawingView();
        highlightToolButton();
    }

    public void handleFillClick(View v) {
        this.activeTool = bucketTool;
        updateDrawingView();
        highlightToolButton();
    }

    public void handleBrushClick(View v) {
        this.activeTool = brushTool;
        updateDrawingView();
        highlightToolButton();
    }

    public void handleMenuClick(View v) {
        showOperationsMenu(v);
    }

    @Override
    public void onColorChanged(ColorARGB color) {
        this.primaryColor = color;
        updateDrawingView();
    }

    public void updateDrawingView() {
        drawing.setTool(activeTool);
        drawing.setColors(primaryColor, secondaryColor);
    }

    public void setEditorDimensions() {
        grid.setNumRows(rows);
        grid.setNumColumns(cols);
        drawing.setNumRows(rows);
        drawing.setNumColumns(cols);
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
        FileOutputStream out = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String dateStr = dateFormat.format(new Date());
            String fileName = dateStr +".png";

            String sdcard = Environment.getExternalStorageDirectory().toString();
            File root = new File(sdcard + "/pixpainter_saves");
            System.out.println(root.getPath());
            System.out.println(root.getAbsolutePath());
            root.mkdirs();
            File f = new File(root, fileName);
            f.createNewFile();
            System.out.println("file created " + f.toString());

            out = new FileOutputStream(f);
            Bitmap bitmap = Bitmap.createBitmap(drawing.getWidth(),
                    drawing.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawing.getBackground().draw(canvas);
            drawing.draw(canvas);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

            if (exportToGallery) {
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        f.getAbsolutePath(),
                        fileName,
                        "PixPainter image");

                Toast.makeText(this,
                    getResources().getString(R.string.exportedToGalleryToast),
                    Toast.LENGTH_SHORT).show();
            }

        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
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
                drawing.initializePixels();
                drawing.invalidate();
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
