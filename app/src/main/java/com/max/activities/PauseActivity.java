package com.max.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.max.myapplication.R;

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
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                i.putExtra("toMainMenu", true);
                finish();
            }
        });

        Button restart = (Button)findViewById(R.id.restartButton);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("restart", true);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        Button back = (Button)findViewById(R.id.continueButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("back", true);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
