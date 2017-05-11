package fi.tamk.jpak.pixpainter;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import fi.tamk.jpak.pixpainter.fragments.CanvasSetupFragment;
import fi.tamk.jpak.pixpainter.fragments.OnCanvasSetupChanged;
import fi.tamk.jpak.pixpainter.utils.PixelGridState;

/**
 * Setup Activity.
 *
 * The launching activity. User chooses size for {@link DrawingView drawing canvas}
 * and launches the main {@link EditorActivity editor activity} by pressing a button.
 *
 * @author Juuso Pakarinen
 * @version 15.04.2017
 */
public class SetupActivity extends AppCompatActivity implements OnCanvasSetupChanged {

    /**
     * Array for holding selected canvas dimensions.
     */
    private int[] dimensions;

    /**
     * List of clickable areas used to select canvas size.
     */
    private ArrayList<LinearLayout> setupAreas;

    /**
     * Fragment containing input fields for custom width and height values.
     */
    private CanvasSetupFragment canvasFrag;

    /**
     * Whether the custom setup fragment is visible.
     */
    private boolean isFragInView;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after
     *                            previously being shut down then this Bundle
     *                            contains the saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        canvasFrag = new CanvasSetupFragment();
        isFragInView = false;
        dimensions = new int[] {1,1};
        setupAreas = new ArrayList<>();
        setupAreas.add((LinearLayout) findViewById(R.id.smallArea));
        setupAreas.add((LinearLayout) findViewById(R.id.mediumArea));
        setupAreas.add((LinearLayout) findViewById(R.id.largeArea));
        setupAreas.add((LinearLayout) findViewById(R.id.customArea));
    }

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause().
     *
     * Starts title animation.
     */
    @Override
    protected void onResume() {
        super.onResume();
        handleSmallClick(findViewById(R.id.smallArea));

        ImageView title = (ImageView) findViewById(R.id.appTitle);
        title.setBackgroundResource(R.drawable.title_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) title.getBackground();
        frameAnimation.start();
    }

    /**
     * Starts {@link EditorActivity} with drawing canvas of selected dimensions.
     * @param v Clicked view.
     */
    public void startEditor(View v) {
        PixelGridState.clear();
        Intent i = new Intent(this, EditorActivity.class);
        int cols = 20;
        int rows = 20;
        if (dimensions[0] <= 100) cols = dimensions[0];
        if (dimensions[1] <= 100) rows = dimensions[1];
        i.putExtra("columns", cols);
        i.putExtra("rows", rows);
        startActivity(i);
    }

    /**
     * Set {@link SetupActivity#dimensions dimensions} for a 20x20 canvas.
     * @param v Clicked view.
     */
    public void handleSmallClick(View v) {
        highlightSetupArea(v);
        hideCustomSetup();
        dimensions[0] = 20;
        dimensions[1] = 20;
    }

    /**
     * Set {@link SetupActivity#dimensions dimensions} for a 40x40 canvas.
     * @param v Clicked view.
     */
    public void handleMediumClick(View v) {
        highlightSetupArea(v);
        hideCustomSetup();
        dimensions[0] = 40;
        dimensions[1] = 40;
    }

    /**
     * Set {@link SetupActivity#dimensions dimensions} for a 60x60 canvas.
     * @param v Clicked view.
     */
    public void handleLargeClick(View v) {
        highlightSetupArea(v);
        hideCustomSetup();
        dimensions[0] = 60;
        dimensions[1] = 60;
    }

    /**
     * Show the {@link CanvasSetupFragment setup fragment} for custom canvas dimensions.
     * @param v Clicked view.
     */
    public void handleCustomClick(View v) {
        highlightSetupArea(v);
        showCustomSetup();
    }

    /**
     * Set dimensions received from {@link CanvasSetupFragment canvas setup fragment}
     * @param dimensions Width and height values for canvas.
     */
    @Override
    public void handleCanvasSetupChange(int[] dimensions) {
        this.dimensions[0] = dimensions[0];
        this.dimensions[1] = dimensions[1];
    }

    /**
     * Add {@link CanvasSetupFragment setup fragment} to the view.
     */
    public void showCustomSetup() {
        if (!isFragInView && canvasFrag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.customSetupArea, canvasFrag);
            isFragInView = true;
            transaction.commit();
        }
    }

    /**
     * Remove {@link CanvasSetupFragment setup fragment} from the view.
     */
    public void hideCustomSetup() {
        if (isFragInView && canvasFrag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(canvasFrag);
            isFragInView = false;
            transaction.commit();
        }
    }

    /**
     * Highlight the background of selected canvas size option.
     * @param v Clicked view.
     */
    public void highlightSetupArea(View v) {
        removeSetupAreaHighlights();
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    /**
     * Remove all current highlights from canvas size options.
     */
    public void removeSetupAreaHighlights() {
        for (LinearLayout l : setupAreas) {
            l.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }
}
