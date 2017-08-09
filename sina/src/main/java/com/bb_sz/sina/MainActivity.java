package com.bb_sz.sina;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static String doExec(String cmd) {

        Log.i("sky_MrToSh", "doExec(), cmd is " + cmd);

        StringBuffer sb = new StringBuffer();
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line + "\n");
            }
            Log.i("sky_MrToSh", "doExec(), sb is " + sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
