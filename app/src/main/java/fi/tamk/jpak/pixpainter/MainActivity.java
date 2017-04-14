package fi.tamk.jpak.pixpainter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Juuso Pakarinen on 15/04/2017.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PixelGridView pixelGrid = new PixelGridView(this, null);
        pixelGrid.setNumColumns(4);
        pixelGrid.setNumRows(6);

        setContentView(pixelGrid);
        //setContentView(R.layout.activity_main);
    }
}
