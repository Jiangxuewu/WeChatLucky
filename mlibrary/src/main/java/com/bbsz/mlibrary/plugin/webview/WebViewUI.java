package com.bbsz.mlibrary.plugin.webview;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.FrameLayout;

public class WebViewUI extends AppCompatActivity implements WebWidget.OnTitleListener {

    private WebWidget mWebWidget;

    private String testUrl = "http://www.bb-sz.com";

    private long clickHomeTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        mWebWidget = new WebWidget(this);
        setContentView(mWebWidget, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        testUrl = getIntent().getStringExtra("url");
        if (!URLUtil.isValidUrl(testUrl)) {
            finish();
            return;
        }
        mWebWidget.loadUrl(testUrl);

        mWebWidget.setUpdateTitleListener(this);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (null != bar) {
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
//            bar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mWebWidget.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (mWebWidget.canBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (clickHomeTime > 0 && System.currentTimeMillis() - clickHomeTime <= 300) {
                    finish();
                } else {
                    onBackPressed();
                }
                clickHomeTime = System.currentTimeMillis();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateTitle(String title) {
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (null != bar) {
            bar.setTitle(title);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWebWidget)
            mWebWidget.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mWebWidget)
            mWebWidget.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mWebWidget)
            mWebWidget.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

