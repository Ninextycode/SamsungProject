package com.max.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.modes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                switch (pos) {
                    case 0:
                        Constants.figureW = 3;
                        Constants.figureH = 4;
                        Constants.figureN = 3;
                        break;
                    case 1:
                        Constants.figureW = 4;
                        Constants.figureH = 5;
                        Constants.figureN = 4;
                        break;
                    case 2:
                        Constants.figureW = 5;
                        Constants.figureH = 6;
                        Constants.figureN = 5;
                        break;
                    case 3:
                        Constants.figureW = 6;
                        Constants.figureH = 7;
                        Constants.figureN = 6;
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        spinner.setSelection(0);

        Button start = (Button)findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FieldActivity.class);
                startActivity(i);
            }
        });
      //  setContentView(new FieldView(this));

    }
}
