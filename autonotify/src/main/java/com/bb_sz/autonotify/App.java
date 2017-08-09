package com.bb_sz.autonotify;

import android.app.Application;
import android.content.Intent;

import com.bb_sz.autonotify.service.MyCoreService;

/**
 * Created by Administrator on 2017/6/22.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyCoreService.class));
    }
}
