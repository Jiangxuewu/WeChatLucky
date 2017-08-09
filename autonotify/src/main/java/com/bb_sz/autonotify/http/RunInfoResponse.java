package com.bb_sz.autonotify.http;

/**
 * Created by Administrator on 2017/6/22.
 */

public class RunInfoResponse {


    /**
     * _id : 86900
     * _ip : 183.16.196.92
     * _date : 2017-06-22 14:07:42
     * pkg : com.pxsk.sgrz
     * phoneName : HM NOTE 1LTE 3
     * type : 1
     */

    private String _id;
    private String _ip;
    private String _date;
    private String pkg;
    private String phoneName;
    private String type;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_ip() {
        return _ip;
    }

    public void set_ip(String _ip) {
        this._ip = _ip;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
