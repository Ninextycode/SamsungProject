package com.max.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.max.figure.Figure;
import com.max.myapplication.Constants;
import com.max.myapplication.DBRecords;
import com.max.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DBRecords dbRecords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        dbRecords = new DBRecords(this);


        final EditText playerName = (EditText) findViewById(R.id.nameEditText);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Button button = (Button) findViewById(R.id.recordsButtom);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(i);
            }
        });


        rewriteBest();
        Constants.name = playerName.getText().toString();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.modes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);






        playerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Constants.name = playerName.getText().toString();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Constants.mode = pos;
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                Figure.A = width /( Constants.figureW[Constants.mode] + 2);


                rewriteBest();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        spinner.setSelection(Constants.mode);

        Button start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FieldActivity.class);
                startActivity(i);
               // finish();
            }
        });
        //  setContentView(new FieldView(this));
    }

    private void rewriteBest(){
        List<Pair<String, Long>> bests = dbRecords.selectAll(Constants.mode, 3);
        List<TextView> bestsTV = new ArrayList<>();
        bestsTV.add((TextView) findViewById(R.id.best1textView));
        bestsTV.add((TextView) findViewById(R.id.best2textView));
        bestsTV.add((TextView) findViewById(R.id.best3textView));

        for(int i =0;i<3;i++) {
            if (bests.size() > i) {
                long milliseconds = bests.get(i).second;
                bestsTV.get(i).setText(bests.get(i).first + " " +
                        milliseconds / 60000 + ":"
                        + (milliseconds % 60000) / 1000 + ":"
                        + (milliseconds % 1000) / 100);
            } else {
                bestsTV.get(i).setText("");
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        rewriteBest();
    }
}
