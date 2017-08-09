package com.bb_sz.autonotify.http;

/**
 * Created by Administrator on 2017/7/3.
 */

public class CheckSafeResponse {


    /**
     * cid : xht
     * pkg : com.xiaohuangtiao
     * path : http://www.bb-sz.com/easynote/com.xiaohuangtiao.apk
     * version : 1.2.0
     * launcher :
     */

    private String cid;
    private String pkg;
    private String name;
    private String path;
    private String version;
    private String launcher;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPkg() {
        return pkg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLauncher() {
        return launcher;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("cid=").append(cid).append("\n");
        sb.append("pkg=").append(pkg).append("\n");
        sb.append("path=").append(path).append("\n");
        sb.append("version=").append(version).append("\n");
        sb.append("launcher=").append(launcher).append("\n");
        return sb.toString();
    }
}
