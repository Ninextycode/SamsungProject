package com.max.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.max.myapplication.FieldView;
import com.max.myapplication.R;
import com.max.observeSubscribe.Observer;
import com.max.observeSubscribe.Subject;

/**
 * Created by Max on 4/30/2016.
 */
public class FieldActivity extends AppCompatActivity implements Observer<String>{

    long start, end, startpause , endpause, offset = 0;
    boolean isPause = false;
    Object mutex = new Object();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_layout);

        FieldView fv = (FieldView)findViewById(R.id.fieldView);

        fv.register(this);

        final Button pause = (Button)findViewById(R.id.menuButton);
        final Bundle savedInstanceStateFinal = savedInstanceState;
                pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {









                  FieldActivity.this.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Dialog dialog = (new MenuDialog()).onCreateDialog(savedInstanceStateFinal);
                          dialog.show();
                        }
                   });
                //Intent i = new Intent(FieldActivity.this, PauseActivity.class);
                //startActivityForResult(i, MENU_RESULT_CODE);
            }
        });
        startpause = System.currentTimeMillis();
        start = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    end = System.currentTimeMillis();
                    long milliseconds = end - start - offset;

                    FieldActivity.this.setTime(
                                       milliseconds / 60000 + ":"
                                    + (milliseconds % 60000) / 1000 + ":"
                                    + (milliseconds % 1000) / 100);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();
    }

    private  int MENU_RESULT_CODE = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MENU_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("restart", false)) {
                    Intent i = new Intent(FieldActivity.this, FieldActivity.class);
                    startActivity(i);
                    finish();
                } else if (data.getBooleanExtra("back", false)) {
                    //just returned
                } else if (data.getBooleanExtra("toMainMenu", false)) {
                    Intent i = new Intent(FieldActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    throw new IllegalStateException("Wrong information from menue");
                }
            }
        }
    }

    @Override
    public void update(String s){
        if("matched".equals(s)){
            Intent i = new Intent(this, SucessActivity.class);
            i.putExtra("time",  end - start - offset);
            startActivity(i);
            finish();
        }
    }

    Subject sub = null;
    @Override
    public void setSubject(Subject<String> sub){
        this.sub = sub;
    }

    @Override
    public void onPause(){
        super.onPause();
        startpause = System.currentTimeMillis();
    }

    @Override
    public void onResume(){
        super.onResume();
        offset = offset+System.currentTimeMillis()-startpause;
    }

    private void setTime(String s){
        final String s2 = s;
        FieldActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tw = (TextView)
                        findViewById(R.id.timer);

                tw.setText(s2);
            }
        });
    }

    public static class MenuDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.pause)
                    .setItems(R.array.pauseMenu, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                        }
                    });
            return builder.create();
        }
    }
}
