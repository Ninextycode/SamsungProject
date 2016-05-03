package com.max.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.max.myapplication.Constants;
import com.max.myapplication.DBRecords;
import com.max.myapplication.R;

import java.util.List;

/**
 * Created by Max on 5/3/2016.
 */
public class RecordsActivity extends AppCompatActivity {
    DBRecords dbRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records_layout);
        dbRecords = new DBRecords(this);

        setAdapterFromDB();
        Button back = (Button)findViewById(R.id.backRecords);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button clear = (Button)findViewById(R.id.clearRecords);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRecords.deleteAll(Constants.mode);
                setAdapterFromDB();
            }
        });
    }

    private void setAdapterFromDB(){
        List<Pair<String, Long>> bests = dbRecords.selectAll(Constants.mode, 50);
        String[] myArr = new String[bests.size()];


        for(int i = 0; i < bests.size(); i++){
            long milliseconds = bests.get(i).second;
            myArr[i] = bests.get(i).first + " " +
                    milliseconds / 60000 + ":"
                    + (milliseconds % 60000) / 1000 + ":"
                    + (milliseconds % 1000) / 100;
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myArr);
        ListView recordsList = (ListView)findViewById(R.id.recordsListView);
        recordsList.setAdapter(monthAdapter);

    }
}


