package com.bb_sz.autonotify.http;

import com.bb_sz.lib.http.BaseModel;
import com.bb_sz.lib.http.Http;
import com.bb_sz.lib.http.HttpEntry;
import com.bb_sz.lib.http.IHttpCallback;

/**
 * Created by Administrator on 2017/7/4.
 */

public class CheckSafeModel extends BaseModel {

    private final String url = "http://www.bb-sz.com/ad/safe/apk.php";

    private IHttpCallback callback;

    public CheckSafeModel(IHttpCallback callback) {
        this.callback = callback;
    }

    @Override
    public HttpEntry toHttpEntry() {
        HttpEntry entry = new HttpEntry();
        entry.setType(Http.GET);
        entry.setBaseUrl(url);
        entry.setCallback(callback);
        return entry;
    }
}
