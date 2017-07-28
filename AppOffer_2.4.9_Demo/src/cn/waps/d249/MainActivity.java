package cn.waps.d249;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.waps.AppConnect;
import cn.waps.AppListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickInit(View v) {
        // 初始化统计器，并通过代码设置APP_ID, APP_PID
        AppConnect.getInstance("09f277ca386ee99cb4c910e09f562112", "waps", this);
    }

    public void onClickLoadPop(View v) {
        AppConnect.getInstance(this).initPopAd(this);
    }

    public void onClickShowPop(View v) {
// 设置插屏广告无数据时的回调监听（该方法必须在showPopAd之前调用）
        AppConnect.getInstance(this).setPopAdNoDataListener(new AppListener() {

            @Override
            public void onPopNoData() {
                Log.i("debug", "插屏广告暂无可用数据");
            }

        });
        // 显示插屏广告
        AppConnect.getInstance(this).showPopAd(this);
    }

    public void onClickExit(View v) {
        AppConnect.getInstance(this).close();
    }
}
