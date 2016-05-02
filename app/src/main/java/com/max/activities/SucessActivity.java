package com.max.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.max.myapplication.R;

/**
 * Created by Max on 5/2/2016.
 */
public class SucessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sucess);

        TextView time = (TextView)findViewById(R.id.finihsTimeSucess);
        long milliseconds = getIntent().getLongExtra("time", 0);
        time.setText("Your time is " +
                milliseconds / 60000 + ":"
                + (milliseconds % 60000) / 1000 + ":"
                + (milliseconds % 1000) / 100);
        //TODO new record


        Button toMainMenu = (Button)findViewById(R.id.mainMenuButtonSuccess);
        toMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SucessActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button restart = (Button)findViewById(R.id.restartButtonSuccess);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SucessActivity.this, FieldActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
