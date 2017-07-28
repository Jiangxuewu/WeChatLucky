package com.example.TestPlugin;

import android.app.Activity;
import android.os.Bundle;

import com.UU.uuaccelerator.Utils.GameSDK;


public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        GameSDK.goSpeedUp(this);
    }
}
