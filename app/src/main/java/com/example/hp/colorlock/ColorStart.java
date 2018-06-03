package com.example.hp.colorlock;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ColorStart extends AppCompatActivity {

    View mLayout;
    public String detailValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        mLayout = (View) findViewById(R.id.myLayout);
        mLayout.setBackgroundColor(Color.RED);

        Bundle extras = getIntent().getExtras();

        Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },10000);

        if(extras!=null){
             detailValue = extras.getString("Password");

            if(detailValue==null)
            {
                Toast.makeText(this,"ENTER VALID PASSWORD",Toast.LENGTH_SHORT).show();
            }
        }



        String st = bin(detailValue);

        Timer timer = new Timer();
        ColorStart.MyTimer mt = new ColorStart.MyTimer(st);

        timer.schedule(mt,500,500);
    }

    public static int hex2decimal(String ss)
    {
        String digits = "0123456789ABCDEF";
        ss = ss.toUpperCase();
        int val = 0;
        for (int i = 0; i < ss.length(); i++)
        {
            char c = ss.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
    public static String bin(String s)
    {
        int decnum, i=1, j;
        int binnum[] = new int[100];
         /* first convert the hexadecimal to decimal */

        decnum = hex2decimal(s);

        /* now convert the decimal to binary */

        while(decnum != 0)
        {
            binnum[i++] = decnum%2;
            decnum = decnum/2;
        }
        int k;
        String str = "";
        for(k = 0; k<32; k++)
        {
            str += Integer.toString(binnum[k]);
        }
        return str;
    }
    class MyTimer extends TimerTask {

        public String s;
        public int i=0;
        public int j=0;
        public MyTimer(String st)
        {
            s = st;
            //Toast.makeText(this,"ENTER VALID PASSWORD",Toast.LENGTH_SHORT).show();
        }

        public void run()
        {
            if(i>31) {return;}
            if(s.charAt(i)=='0' && s.charAt(i+1)=='0') j=0;
            else if(s.charAt(i)=='0' && s.charAt(i+1)=='1') j=1;
            else if(s.charAt(i)=='1' && s.charAt(i+1)=='0') j=2;
            else if(s.charAt(i)=='1' && s.charAt(i+1)=='1') j=3;
            //Random rand = new Random();
            runOnUiThread(new Runnable() {
                public void run() {
                   int Images[] = {
                            Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW
                    };
                    mLayout.setBackgroundColor(Images[j]);
                    i+=2;
                }

            });
        }
    }

}
