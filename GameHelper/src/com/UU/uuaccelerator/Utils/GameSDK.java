package com.UU.uuaccelerator.Utils;


import android.app.Activity;
import android.content.Intent;

public class GameSDK {

    public static void goSpeedUp(Activity act) {

        int openCount = SharedUtils.getStartAppCount(act);

        if (openCount >= 0) {

            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(act, "com.UU.myaccessibility.SpeedUpActivity");
            act.startActivity(intent);
        }
        SharedUtils.saveStartAppCount(act);
    }

}
