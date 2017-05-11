package fi.tamk.jpak.pixpainter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fi.tamk.jpak.pixpainter.colorpicker.ColorPickerDialog;
import fi.tamk.jpak.pixpainter.colorpicker.ColorPickerListener;
import fi.tamk.jpak.pixpainter.fragments.OnShapeSetupChanged;
import fi.tamk.jpak.pixpainter.fragments.OnToolSetupChanged;
import fi.tamk.jpak.pixpainter.fragments.ShapeSetupFragment;
import fi.tamk.jpak.pixpainter.fragments.ToolSetupFragment;
import fi.tamk.jpak.pixpainter.tools.Brush;
import fi.tamk.jpak.pixpainter.tools.Eraser;
import fi.tamk.jpak.pixpainter.tools.PaintBucket;
import fi.tamk.jpak.pixpainter.tools.Pencil;
import fi.tamk.jpak.pixpainter.tools.Pipette;
import fi.tamk.jpak.pixpainter.tools.Shape;
import fi.tamk.jpak.pixpainter.tools.ShapeFillType;
import fi.tamk.jpak.pixpainter.tools.ShapeType;
import fi.tamk.jpak.pixpainter.tools.Tool;
import fi.tamk.jpak.pixpainter.utils.ColorARGB;
import fi.tamk.jpak.pixpainter.utils.PixelGridState;

/**
 * Editor Activity.
 *
 * The activity containing the art editor. Has header and footer for setup and
 * tool buttons and between those the {@link DrawingView drawing canvas}.
 *
 * @author Juuso Pakarinen
 * @version 20.04.2017
 */
public class EditorActivity extends AppCompatActivity implements
        ColorPickerListener, OnToolSetupChanged, OnShapeSetupChanged, Pipette.PipetteListener {

    /**
     * The view representing drawing canvas.
     */
    private DrawingView drawing;

    /**
     * The view that is displayed under {@link EditorActivity#drawing} to represent
     * areas of each pixel.
     */
    private PixelGridView grid;

    /**
     * Fragment for tool setup.
     */
    private ToolSetupFragment setupFrag;

    /**
     * Fragment for shape tool setup.
     */
    private ShapeSetupFragment shapeFrag;

    /**
     * List holding the tool buttons.
     */
    private ArrayList<ImageButton> toolButtons;

    /**
     * Current primary color.
     */
    private ColorARGB primaryColor;

    /**
     * Current secondary color.
     */
    private ColorARGB secondaryColor;

    /**
     * Current active tool.
     */
    private Tool activeTool;

    /**
     * The pencil tool.
     */
    private Pencil pencilTool;

    /**
     * The brush tool.
     */
    private Brush brushTool;

    /**
     * The eraser tool.
     */
    private Eraser eraserTool;

    /**
     * The shape tool.
     */
    private Shape shapeTool;

    /**
     * The paint bucket (fill) tool.
     */
    private PaintBucket bucketTool;

    /**
     * The pipette (color selector) tool.
     */
    private Pipette pipetteTool;

    /**
     * Alert shown before clearing canvas.
     */
    private AlertDialog clearAlert;

    /**
     * Number of {@link DrawingView#pixels pixel grids} columns and rows.
     */
    private int cols, rows;

    /**
     * Selected size of tool stroke.
     */
    private int selectedStrokeSize;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after
     *                            previously being shut down then this Bundle
     *                            contains the saved data.
     */
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

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause().
     *
     * Handles initialization of the classes attributes and tools and stuff.
     */
    @Override
    protected void onResume() {
        super.onResume();

        grid = (PixelGridView) findViewById(R.id.pixelGridView);
        drawing = (DrawingView) findViewById(R.id.drawingView);
        setEditorDimensions();

        setupFrag = (ToolSetupFragment) getSupportFragmentManager().findFragmentById(R.id.setupFrag);
        shapeFrag = (ShapeSetupFragment) getSupportFragmentManager().findFragmentById(R.id.shapeFrag);

        toolButtons = new ArrayList<>();
        pencilTool = new Pencil();
        brushTool = new Brush();
        eraserTool = new Eraser();
        shapeTool = new Shape();
        bucketTool = new PaintBucket();
        pipetteTool = new Pipette(this);
        activeTool = PixelGridState.getActiveTool();
        if (activeTool == null) activeTool = pencilTool;
        selectedStrokeSize = 1;

        primaryColor = PixelGridState.getPrimaryColor();
        secondaryColor = PixelGridState.getSecondaryColor();
        if (primaryColor == null) primaryColor = new ColorARGB(255, 0, 0, 0);
        if (secondaryColor == null) secondaryColor = new ColorARGB(255, 255, 255, 255);

        drawing.setTool(activeTool);
        drawing.setColors(primaryColor, secondaryColor);
        showSelectedColors();

        PercentRelativeLayout toolbarLayout = (PercentRelativeLayout) findViewById(R.id.toolbarLayout);

        for (int i = 0; i < toolbarLayout.getChildCount(); i++) {
            View v = toolbarLayout.getChildAt(i);
            if (v instanceof ImageButton) {
                ImageButton b = (ImageButton) v;
                toolButtons.add(b);
            }
        }

        updateDrawingView();
        showActiveTool();
        toggleRedoButtonColor();
    }

    /**
     * Opens the {@link ColorPickerDialog color picker dialog}.
     * @param v Clicked view.
     */
    public void handleColorPickerClick(View v) {
        ColorPickerDialog cpDialog = ColorPickerDialog.newInstance(primaryColor);
        cpDialog.setColorPickerListener(this);
        cpDialog.show(getFragmentManager(), "ColorPickerDialog");
    }

    /**
     * Switch the primary and secondary colors.
     * @param v Clicked view.
     */
    public void handleColorsIndicatorClick(View v) {
        ColorARGB tmp = new ColorARGB(primaryColor.getA(),
                primaryColor.getR(), primaryColor.getG(), primaryColor.getB());

        primaryColor = new ColorARGB(secondaryColor.getA(),
                secondaryColor.getR(), secondaryColor.getG(), secondaryColor.getB());

        secondaryColor = tmp;
        updateDrawingView();
    }

    /**
     * Select {@link Pencil pencil} as the active tool.
     * @param v Clicked view.
     */
    public void handlePencilClick(View v) {
        this.activeTool = pencilTool;
        updateDrawingView();
        showActiveTool();
    }

    /**
     * Select {@link Shape shape} as the active tool.
     * @param v Clicked view.
     */
    public void handleShapeClick(View v) {
        this.activeTool = shapeTool;
        updateDrawingView();
        showActiveTool();
    }

    /**
     * Select {@link PaintBucket paint bucket} as the active tool.
     * @param v Clicked view.
     */
    public void handleFillClick(View v) {
        this.activeTool = bucketTool;
        updateDrawingView();
        showActiveTool();
    }

    /**
     * Select {@link Brush brush} as the active tool.
     * @param v Clicked view.
     */
    public void handleBrushClick(View v) {
        this.activeTool = brushTool;
        updateDrawingView();
        showActiveTool();
    }

    /**
     * Select {@link Eraser eraser} as the active tool.
     * @param v Clicked view.
     */
    public void handleEraserClick(View v) {
        this.activeTool = eraserTool;
        updateDrawingView();
        showActiveTool();
    }

    /**
     * Select {@link Pipette pipette} as the active tool.
     * @param v Clicked view.
     */
    public void handlePipetteClick(View v) {
        this.activeTool = pipetteTool;
        updateDrawingView();
        showActiveTool();
    }

    /**
     * Opens the popup menu.
     * @param v Clicked view.
     */
    public void handleMenuClick(View v) {
        showOperationsMenu(v);
    }

    /**
     * Undo last stroke to drawing area.
     * @param v Clicked view.
     */
    public void handleUndoClick(View v) {
        drawing.undo();
        toggleRedoButtonColor();
    }

    /**
     * Redo last stroke to drawing area.
     * @param v Clicked view.
     */
    public void handleRedoClick(View v) {
        drawing.redo();
        toggleRedoButtonColor();
    }

    /**
     * Grey out the redo button if unable to go forward in history.
     */
    public void toggleRedoButtonColor() {
        ImageButton redo = (ImageButton) findViewById(R.id.redoButton);

        if (PixelGridState.getHistoryCursor() > 0) {
            redo.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent),
                    PorterDuff.Mode.SRC_IN);
        } else {
            redo.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * Set the selected color from the color picker dialog as primary color.
     * @param color Color received from the {@link ColorPickerDialog color picker}.
     */
    @Override
    public void onColorChanged(ColorARGB color) {
        this.primaryColor = color;
        updateDrawingView();
    }

    /**
     * Update the drawing view to ensure it uses selected tool and colors.
     */
    public void updateDrawingView() {
        if (drawing != null) {
            if (activeTool != null) {
                activeTool.setStrokeSize(selectedStrokeSize);
                PixelGridState.setActiveTool(activeTool);
                drawing.setTool(activeTool);
            }

            drawing.setColors(primaryColor, secondaryColor);
            showSelectedColors();
        }
    }

    /**
     * Sets the number of rows and columns to {@link EditorActivity#drawing drawing}
     * and {@link EditorActivity#grid grid} views.
     */
    public void setEditorDimensions() {
        grid.setNumRows(rows);
        grid.setNumColumns(cols);
        drawing.setNumRows(rows);
        drawing.setNumColumns(cols);
    }

    /**
     * Set current primary and secondary colors to indicator button.
     */
    public void showSelectedColors() {
        ImageView primary = (ImageView) findViewById(R.id.primaryColor);
        ImageView secondary = (ImageView) findViewById(R.id.secondaryColor);
        GradientDrawable bgShape = (GradientDrawable) primary.getDrawable();
        bgShape.setColor(primaryColor.toInt());
        bgShape = (GradientDrawable) secondary.getDrawable();
        bgShape.setColor(secondaryColor.toInt());
    }

    /**
     * Highlight the button of the selected tool and show setup fragment if
     * necessary.
     */
    public void showActiveTool() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        boolean showToolFrag = false;
        boolean showShapeFrag = false;

        for (int i = 0; i < toolButtons.size(); i++) {
            toolButtons.get(i).setBackgroundColor(
                    ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        int activeColor = ContextCompat.getColor(this, R.color.colorAccent);

        switch (activeTool.getType()) {
            case PEN:
                toolButtons.get(0).setBackgroundColor(activeColor);
                showToolFrag = true;
                break;
            case BRUSH:
                toolButtons.get(1).setBackgroundColor(activeColor);
                showToolFrag = true;
                break;
            case ERASE:
                toolButtons.get(2).setBackgroundColor(activeColor);
                showToolFrag = true;
                break;
            case SHAPE:
                toolButtons.get(3).setBackgroundColor(activeColor);
                showShapeFrag = true;
                break;
            case FILL:
                toolButtons.get(4).setBackgroundColor(activeColor);
                break;
            case PIP:
                toolButtons.get(5).setBackgroundColor(activeColor);
                break;
            default:
                break;
        }

        if (showToolFrag) {
            transaction.show(setupFrag);
            transaction.hide(shapeFrag);
        } else if (showShapeFrag) {
            transaction.show(shapeFrag);
            transaction.hide(setupFrag);
        } else {
            transaction.hide(setupFrag);
            transaction.hide(shapeFrag);
        }

        transaction.commit();
    }

    /**
     * Show the popup menu.
     * @param v Clicked view.
     */
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

    /**
     * Saves the drawing and exports it to Android Gallery app if chosen so.
     *
     * Creates a bitmap image from the {@link DrawingView drawing} and saves it
     * as PNG to devices internal memory. This method also exports the image
     * as JPG to Android's default Gallery app.
     *
     * @param exportToGallery whether the image is exported to the Gallery app.
     */
    public void saveImageToFile(boolean exportToGallery) {
        FileOutputStream out = null;

        try {
            /* Create filename from current time */
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String dateStr = dateFormat.format(new Date());
            String fileName = dateStr +".png";

            /* Create save folder */
            File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File root = new File(picDir, "pixpainter_saves");
            root.mkdirs();

            File f = new File(root, fileName);
            f.createNewFile();

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
            } else {
                Toast.makeText(this,
                        getResources().getString(R.string.saveToLocal),
                        Toast.LENGTH_SHORT).show();
            }

        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    getResources().getString(R.string.saveFailedToast),
                    Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Clears the drawn image from canvas.
     *
     * Prompts the user with alert before clearing the image.
     */
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

    /**
     * Handles the input received from {@link ToolSetupFragment tool setup}.
     * @param size Size of the stroke.
     */
    @Override
    public void handleToolSetupChange(int size) {
        this.selectedStrokeSize = size;
        updateDrawingView();
    }

    /**
     * Sets the color selected with {@link Pipette pipette tool} as the
     * primary color.
     *
     * @param color Selected color.
     */
    @Override
    public void handlePipetteDraw(ColorARGB color) {
        this.primaryColor = color;
        updateDrawingView();
    }

    /**
     * Handles change of the shape type received from {@link ShapeSetupFragment shape tool setup}.
     * @param type Type of the shape.
     */
    @Override
    public void handleShapeChange(ShapeType type) {
        shapeTool.setShapeType(type);
        updateDrawingView();
    }

    /**
     * Handles change of the fill type received from {@link ShapeSetupFragment shape tool setup}.
     * @param fillType Type of the shape fill.
     */
    @Override
    public void handleShapeFillChange(ShapeFillType fillType) {
        shapeTool.setShapeFillType(fillType);
        updateDrawingView();
    }

    /**
     * Handles change of width and height received from
     * {@link ShapeSetupFragment shape tool setup}.
     *
     * @param width Shape width.
     * @param height Shape height
     */
    @Override
    public void handleShapeSizeChange(int width, int height) {
        if (shapeTool != null) {
            shapeTool.setWidth(width);
            shapeTool.setHeight(height);
        }

        updateDrawingView();
    }
}
