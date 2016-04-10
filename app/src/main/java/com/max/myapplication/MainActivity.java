package com.max.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.max.figure.Divider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FieldView(this));
        Divider.divide(5,5,5);
    }
}
