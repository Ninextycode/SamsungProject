package com.max.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.max.myapplication.R;

/**
 * Created by Max on 5/16/2016.
 */
public class FailActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fail_layout);

        Button toMainMenu = (Button) findViewById(R.id.mainMenuButtonFail);
        toMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button restart = (Button) findViewById(R.id.restartButtonFail);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FailActivity.this, FieldActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
