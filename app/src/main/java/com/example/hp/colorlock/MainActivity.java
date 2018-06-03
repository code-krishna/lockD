package com.example.hp.colorlock;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    public static final int DETAIL_REQUEST = 1;
    Button mButton = null;
    TextInputEditText mText = null;
    public String st;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.myButton);
        mText = (TextInputEditText) findViewById(R.id.myText);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st = mText.getText().toString();
                i = new Intent(view.getContext(),ColorStart.class);
                i.putExtra("Password",st);
                Intent j = new Intent(view.getContext(),RedStart.class);
                startActivity(j);
                Handler handler  = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResult(i,DETAIL_REQUEST);

                    }
                },2000);
            }
        });
    }

}
