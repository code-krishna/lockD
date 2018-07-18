package com.example.hp.colorlock;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String pass="";
        try{
        pass = SHA1(detailValue);}
        catch (Exception e)
        {
            Log.e("Encryption Error", "onCreate: pass not created!",e);
        }

        String st = bin(pass);

        Timer timer = new Timer();
        ColorStart.MyTimer mt = new ColorStart.MyTimer(st);

        timer.schedule(mt,500,500);
    }
    public static String bin(String hexa)
    {
        int i=0;
        String result = "";
        while (i<hexa.length())
        {
            switch (hexa.charAt(i))
            {
                case '0':
                    result+="0000";
                    //printf("0000");
                    break;
                case '1':
                    result+="0001";
                    //printf("0001");
                    break;
                case '2':
                    result+="0010";
                    //printf("0010");
                    break;
                case '3':
                    result+="0011";
                    //printf("0011");
                    break;
                case '4':
                    result+="0100";
                    //printf("0100");
                    break;
                case '5':
                    result+="0101";
                    //printf("0101");
                    break;
                case '6':
                    result+="0110";
                    //printf("0110");
                    break;
                case '7':
                    result+="0111";
                    //printf("0111");
                    break;
                case '8':
                    result+="1000";
                    //printf("1000");
                    break;
                case '9':
                    result+="1001";
                    //printf("1001");
                    break;
                case 'A':
                    result+="1010";
                    //printf("1010");
                    break;
                case 'B':
                    result+="1011";
                    //printf("1011");
                    break;
                case 'C':
                    result+="1100";
                    //printf("1100");
                    break;
                case 'D':
                    result+="1101";
                    //printf("1101");
                    break;
                case 'E':
                    result+="1110";
                    //printf("1110");
                    break;
                case 'F':
                    result+="1111";
                    //printf("1111");
                    break;
                case 'a':
                    result+="1010";
                    //printf("1010");
                    break;
                case 'b':
                    result+="1011";
                    //printf("1011");
                    break;
                case 'c':
                    result+="1100";
                    //printf("1100");
                    break;
                case 'd':
                    result+="1101";
                    //printf("1101");
                    break;
                case 'e':
                    result+="1110";
                    //printf("1110");
                    break;
                case 'f':
                    result+="1111";
                    //printf("1111");
                    break;
                default:
                    //printf("\n Invalid hexa digit %c ", hexa[i]);
                    break;
            }
            i++;
        }
        return result;
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException  {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
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
