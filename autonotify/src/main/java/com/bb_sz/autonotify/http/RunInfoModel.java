package com.bb_sz.autonotify.http;


import com.bb_sz.lib.http.BaseModel;
import com.bb_sz.lib.http.Http;
import com.bb_sz.lib.http.HttpEntry;
import com.bb_sz.lib.http.IHttpCallback;

/**
 * Created by Administrator on 2017/6/22.
 */

public class RunInfoModel extends BaseModel {
    private final String url = "http://www.bb-sz.com/SL/";

    private String phoneName;
    private IHttpCallback callback;

    public RunInfoModel(String phoneName, IHttpCallback callback) {
        this.callback = callback;
        this.phoneName = phoneName;
    }

    @Override
    public HttpEntry toHttpEntry() {
        HttpEntry entry = new HttpEntry();
        entry.setType(Http.GET);
        entry.setBaseUrl(url + "get_run_info.php?phoneName=" + phoneName);
        entry.setCallback(callback);
        return entry;
    }
}
