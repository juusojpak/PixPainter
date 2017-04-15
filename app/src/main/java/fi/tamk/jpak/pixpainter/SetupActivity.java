package fi.tamk.jpak.pixpainter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Juuso Pakarinen on 15/04/2017.
 */
public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    public void startEditor(View v) {
        EditText colEdit = (EditText) findViewById(R.id.colEdit);
        EditText rowEdit = (EditText) findViewById(R.id.rowEdit);
        int cols = 1;
        int rows = 1;

        try {
            cols = Integer.parseInt(colEdit.getText().toString());
            rows = Integer.parseInt(rowEdit.getText().toString());
            if (cols > 70) cols = 70;
            if (rows > 100) rows = 100;

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(this, EditorActivity.class);
        i.putExtra("columns", cols);
        i.putExtra("rows", rows);
        startActivity(i);
    }
}
