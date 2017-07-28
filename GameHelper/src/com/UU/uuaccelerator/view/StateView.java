package com.UU.uuaccelerator.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.UU.uuaccelerator.Utils.SystemUtils;

import java.util.HashMap;

public class StateView extends LinearLayout {

    Context context;
    ImageView img_ok;
    TextView mTextView;
    String[] text = {"内存整理...", "无关进程关闭", "安全性检测", "游戏优先级提升"};
    HashMap<String, View> views = new HashMap<String, View>();
    //	StateItemView itemView;
    LayoutParams layoutParams;

    public StateView(Context context) {
        super(context);
        // TODO Auto-generated constructor stu
        this.context = context;
        setOrientation(LinearLayout.VERTICAL);

        int pad = SystemUtils.Dp2Px(context, 10);
        setPadding(pad, 2 * pad, pad, 2 * pad);
        init();
    }


    private void init() {
        // TODO Auto-generated method stub
        layoutParams = new LayoutParams(SystemUtils.Dp2Px(context, 200), LayoutParams.WRAP_CONTENT, 1);

        for (int i = 0; i < text.length; i++) {
            StateItemView itemView = new StateItemView(context, text[i]);
            itemView.setLayoutParams(layoutParams);
            addView(itemView);
            views.put(i + "", itemView);
        }


    }


    public HashMap<String, View> getViews() {
        return views;
    }


    public void setViews(HashMap<String, View> views) {
        this.views = views;
    }


}
