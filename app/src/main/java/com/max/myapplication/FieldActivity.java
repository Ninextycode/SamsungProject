package com.max.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
/**
 * Created by Max on 4/30/2016.
 */
public class FieldActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_layout);
       // RelativeLayout surface = (RelativeLayout)findViewById(R.id.fieldLayout);
       // FieldView fv = new FieldView(this);
      //  surface.addView(fv);


        Button pause = (Button)findViewById(R.id.menuButton);
        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FieldActivity.this.finish();
                FieldActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(FieldActivity.this, PauseActivity.class);
                        startActivity(i);
                    }
                });
            }
        });

    }
}
