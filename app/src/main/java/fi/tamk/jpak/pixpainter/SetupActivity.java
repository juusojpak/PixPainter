package fi.tamk.jpak.pixpainter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import fi.tamk.jpak.pixpainter.fragments.CanvasSetupFragment;
import fi.tamk.jpak.pixpainter.fragments.OnCanvasSetupChanged;
import fi.tamk.jpak.pixpainter.utils.PixelGridState;

/**
 * Created by Juuso Pakarinen on 15/04/2017.
 */
public class SetupActivity extends AppCompatActivity implements OnCanvasSetupChanged {

    private int[] dimensions;
    private ArrayList<LinearLayout> setupAreas;
    private CanvasSetupFragment canvasFrag;
    private boolean isFragInView;

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

    @Override
    protected void onResume() {
        super.onResume();
        handleSmallClick(findViewById(R.id.smallArea));
    }

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

    public void handleSmallClick(View v) {
        highlightSetupArea(v);
        hideCustomSetup();
        dimensions[0] = 20;
        dimensions[1] = 20;
    }

    public void handleMediumClick(View v) {
        highlightSetupArea(v);
        hideCustomSetup();
        dimensions[0] = 40;
        dimensions[1] = 40;
    }

    public void handleLargeClick(View v) {
        highlightSetupArea(v);
        hideCustomSetup();
        dimensions[0] = 60;
        dimensions[1] = 60;
    }

    public void handleCustomClick(View v) {
        highlightSetupArea(v);
        showCustomSetup();
    }

    @Override
    public void handleCanvasSetupChange(int[] dimensions) {
        this.dimensions[0] = dimensions[0];
        this.dimensions[1] = dimensions[1];
    }

    public void showCustomSetup() {
        if (!isFragInView && canvasFrag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.customSetupArea, canvasFrag);
            isFragInView = true;
            transaction.commit();
        }
    }

    public void hideCustomSetup() {
        if (isFragInView && canvasFrag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(canvasFrag);
            isFragInView = false;
            transaction.commit();
        }
    }

    public void highlightSetupArea(View v) {
        removeSetupAreaHighlights();
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    public void removeSetupAreaHighlights() {
        for (LinearLayout l : setupAreas) {
            l.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }
}
