package fi.tamk.jpak.pixpainter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int cols = 1;
        int rows = 1;

        if (intent != null) {
            cols = intent.getExtras().getInt("columns");
            rows = intent.getExtras().getInt("rows");
        }

        PixelGridView pixelGrid = new PixelGridView(this, null);
        pixelGrid.setNumColumns(cols);
        pixelGrid.setNumRows(rows);
        setContentView(pixelGrid);
    }
}
