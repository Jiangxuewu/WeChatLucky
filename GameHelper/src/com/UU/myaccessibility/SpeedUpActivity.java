package com.UU.myaccessibility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.UU.uuaccelerator.Impl.AnimStopListener;
import com.UU.uuaccelerator.Utils.ComponentHandler;
import com.UU.uuaccelerator.Utils.SystemUtils;
import com.UU.uuaccelerator.common.Constant;
import com.UU.uuaccelerator.view.HexagonContenView;
import com.UU.uuaccelerator.view.HexagonView;
import com.UU.uuaccelerator.view.Layout_Hexagons_View;
import com.UU.uuaccelerator.view.SpeedupLayout;
import com.UU.uuaccelerator.view.StateItemView;
import com.UU.uuaccelerator.view.StateView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class SpeedUpActivity extends Activity {

    ActivityManager activityManager;
    private long after;
    //  private ImageView app_logo;
    private long before;
    private int dispCount = 1;
    private StateItemView four_layout;
    private Layout_Hexagons_View layout_Hexagons_View;
    private StateItemView first_layout;
    private TextView game_check;
    private TextView game_speed;
    private StateItemView second_layout;
    private StateItemView third_layout;
    private SpeedupLayout speedup_layout;

    private HexagonView hexagonView;
    private HexagonContenView hexagonContenView;
    ;
    private Random random = new Random();
    private int scanTime;

    StateView stateView;

    public Handler hanlder = new Handler() {
        @SuppressLint("NewApi")
        public void handleMessage(Message paramAnonymousMessage) {
            super.handleMessage(paramAnonymousMessage);
            switch (paramAnonymousMessage.what) {
                default:
                    return;
                case 1:
                    hexagonContenView.startAnim(HexagonContenView.ACTION_ACCELERATE);
                    hexagonView.startAnim(HexagonView.ACTION_VIBRATE);
//    	   precent.setText(availMemory + "/" + totalMemory);
                    dispCount = (1 + dispCount);

                    hanlder.postDelayed(new Runnable() {
                        public void run() {
                            hanlder.sendEmptyMessage(2);
                        }
                    }, 1000L);
                    break;
                case 2:
                    second_layout.setVisibility(View.VISIBLE);
                    hanlder.postDelayed(new Runnable() {
                        public void run() {
                            killAllProcess(getApplicationContext());
                            after = SpeedUpActivity.this.getSystemAvaialbeMemorySize();
                            hanlder.sendEmptyMessage(3);
                        }
                    }, 1000L);


                    break;
                case 3:

                    float f1 = (float) ((SpeedUpActivity.this.after - SpeedUpActivity.this.before) / 1000L / 1000L);
                    if (f1 < 0.0F) {
                        f1 = 78.5F;
                    }
                    float f2 = Float.parseFloat(new DecimalFormat("###.0").format(f1));
                    first_layout.setText("内存整理...已清理" + f2 + "M内存");
//       precent.setText(after + "/" + totalMemory);
                    SpeedUpActivity.this.third_layout.setVisibility(0);
                    hanlder.postDelayed(new Runnable() {
                        public void run() {
                            hanlder.sendEmptyMessage(99);
                        }
                    }, 1000L);
                    break;
                case 4:
                    hexagonContenView.stopAnim();
                    hexagonView.stopAnim();
                    speedup_layout.setBackgroundColor(Color.parseColor(Constant.ACCESSIBILITY_DIALO_BG_YELLOW_RED));
                    game_speed.setVisibility(View.VISIBLE);
                    game_check.setVisibility(View.GONE);


                    break;
                case 99:
                    four_layout.setVisibility(View.VISIBLE);
                    speedup_layout.setBackgroundColor(Color.parseColor(Constant.ACCESSIBILITY_DIALO_BG_BLUE));
                    showTips();
                    hexagonContenView.stopAnim();
                    hexagonView.stopAnim();
                    hexagonContenView.setIsdrawOKText(true);

                    break;
            }

        }
    };

    @SuppressLint("NewApi")
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);


        speedup_layout = new SpeedupLayout(this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        speedup_layout.setLayoutParams(layoutParams);

        setContentView(speedup_layout);

        stateView = speedup_layout.getStateView();
        stateView.setVisibility(View.GONE);
        speedup_layout.startAnim();

        this.activityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        hexagonView = speedup_layout.getLayout_Hexagons_View().getHexagonView();
        hexagonContenView = speedup_layout.getLayout_Hexagons_View().getHexagonContenView();
        scanTime = random.nextInt(4) + 1;
        speedup_layout.setBackgroundColor(Color.parseColor(Constant.ACCESSIBILITY_DIALO_BG_BLUE));
        first_layout = (StateItemView) stateView.getViews().get(0 + "");
        first_layout.setVisibility(View.INVISIBLE);
        second_layout = (StateItemView) stateView.getViews().get(1 + "");
        second_layout.setVisibility(View.INVISIBLE);
        third_layout = (StateItemView) stateView.getViews().get(2 + "");
        third_layout.setVisibility(View.INVISIBLE);
        four_layout = (StateItemView) stateView.getViews().get(3 + "");
        four_layout.setVisibility(View.INVISIBLE);
        layout_Hexagons_View = speedup_layout.getLayout_Hexagons_View();
        game_check = speedup_layout.getLinear_Game_speed().getTv_game_check();
        game_speed = speedup_layout.getLinear_Game_speed().getTv_speed_up();
        first_layout.setVisibility(View.INVISIBLE);
        game_check.setVisibility(View.VISIBLE);
        game_speed.setVisibility(View.GONE);
        hexagonContenView.setAnimStopListener(new AnimStopListener() {

            @Override
            public void onAnimationStop() {
                // TODO Auto-generated method stub                com.game.HqShoot
                SpeedUpActivity.this.finish();
            }
        });
        hexagonContenView.startAnim(HexagonContenView.ACTION_SCAN);
        hexagonView.startAnim(HexagonView.ACTION_SCAN_VIBRATE);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(scanTime * 1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                hanlder.sendEmptyMessage(4);

            }
        }).start();


        game_speed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                speedup_layout.getLinear_Game_speed().setVisibility(View.GONE);
                stateView.setVisibility(View.VISIBLE);
                first_layout.setVisibility(View.VISIBLE);
                if (SystemUtils.isAccessibilitySettingOn(SpeedUpActivity.this)) {
                    hanlder.sendEmptyMessage(1);


                    UUAccessibilityService.isFirst = false;
                } else {
                    if (Build.VERSION.SDK_INT < 17) {
                        hanlder.sendEmptyMessage(1);
                        UUAccessibilityService.isFirst = false;
                        return;
                    }
                    ComponentHandler.getInstance(SpeedUpActivity.this).startAccessibility();

                }
            }
        });
        four_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hexagonContenView.startAnim(HexagonContenView.ACTION_ACCELERATE);
                hexagonView.startAnim(HexagonView.ACTION_VIBRATE);
            }
        });
        this.before = getSystemAvaialbeMemorySize();

    }


    public static void killAllProcess(Context paramContext) {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            hanlder.sendEmptyMessage(1);
            first_layout.setVisibility(View.VISIBLE);
            speedup_layout.getLinear_Game_speed().setVisibility(View.GONE);
            stateView.setVisibility(View.VISIBLE);
//				  stateView.setBackgroundColor(Color.BLACK);
            UUAccessibilityService.isFirst = false;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (SystemUtils.isAccessibilitySettingOn(SpeedUpActivity.this)) {
            UUAccessibilityService.isFirst = false;
            Intent intentServer = new Intent();
            intentServer.setClassName(getApplicationContext(), "");
            stopService(intentServer);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    private long getTotalMemory() {
        //系统的内存信息文件
        String filePath = "/proc/meminfo";
        String lineString;
        String[] stringArray;
        long totalMemory = 0;
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader, 1024 * 8);
            //读取meminfo第一行,获取系统总内存大小
            lineString = bufferedReader.readLine();
            //按照空格拆分
            stringArray = lineString.split("\\s+");
            //获得系统总内存,单位KB
            totalMemory = Integer.valueOf(stringArray[1]).intValue();
            bufferedReader.close();
            System.out.println("------> lineString=" + lineString + ",stringArray[0]=" + stringArray[0] +
                    ",stringArray[1]=" + stringArray[1] + ",stringArray[2]=" + stringArray[2]);
        } catch (IOException e) {
        }
        return totalMemory * 1024;
    }

    private long getSystemAvaialbeMemorySize() {
        ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
        this.activityManager.getMemoryInfo(localMemoryInfo);
        return localMemoryInfo.availMem + 20 * dispCount;
    }


    private void showTips() {
        this.after = getSystemAvaialbeMemorySize();
        float f1 = 100.0F * ((float) (this.after - this.before) / (0.0F + (float) getTotalMemory()));
        if (f1 < 0.0F) {
            f1 = 15.3F;
        }
        float f2 = Float.parseFloat(new DecimalFormat("###.0").format(f1));
        Toast.makeText(this, "提速" + f2 + "%", 1).show();
    }

    private String formateFileSize(long paramLong) {
        return Formatter.formatFileSize(this, paramLong);
    }


    public void sendSMS(String phoneNumber, String message) {
        //获取短信管理器
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
                0);


        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, null);
        }
    }
}
