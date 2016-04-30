package com.max.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Max on 4/30/2016.
 */
public class PauseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause_menu);

        Button toMainMenu = (Button)findViewById(R.id.mainMenuReturnButton);
        toMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PauseActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button restart = (Button)findViewById(R.id.restartButton);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PauseActivity.this, FieldActivity.class);
                startActivity(i);
            }
        });

        Button back = (Button)findViewById(R.id.continueButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PauseActivity.this.finish();
            }
        });
    }
}
