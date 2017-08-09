package com.bb_sz.wechatlucky;

/**
 * Created by Administrator on 2017/8/8.
 */

public class App {

    public App(String json) {
    }

    public int m_type;// 0->360; 1->yyb
    public String pkg;
    public String name;
    public String app_name;
    public String launcher;
    public int open;
    public String input;
    public long w_main = 10;//首页等待时长，单位秒
    public long w_search = 10;//搜索页等待时长，单位秒
    public long w_infos = 5;//app详情页等待时长，单位秒
    public long w_input = 10;//搜索等待时间，单位秒
    public long w_install = 10;//app安装时间，单位秒
    public long w_play = 30;//app打开play的时间，单位秒
    public int time_out = 5 * 60;//运行超时时长, 单位秒
    public int swipe = 3;//滚动间隔，单位秒
    public int swipe_time_out = 3 * 60;//滚动超时时间，单位秒
    public int before_start_sleep = 2;//开始运行前暂停时间，单位秒
    public int clear_app_sleep = 1;//清除所有app后等待的时间，单位秒
    public int clear_sd_sleep = 2;//清除sd卡后等待的时间，单位秒
    public int launch_market_sleep = 10;//启动市场后等待的时间， 单位秒
    public long run_blank = 250;//循环读取当前页面的间隔， 单位毫秒
    public int install_ready_time = 5;//安装等待时间， 单位秒
    public int open_ready_time = 5;//安装后启动app等待时间，单位秒
    public int qh360type;//搜索关键字后，360出来的版本类型
    public String qh360typekey;//"查看全部"
    public int qh360typeindex;//第几个"查看全部"
    public int down_times;//下载页面等待的次数
    public String local_name;//本地apk文件名称

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\n");
        sb.append("name").append(":").append((null != name ? name : "")).append("\n");
        sb.append("pkg").append(":").append((null != pkg ? pkg : "")).append("\n");
        sb.append("launcher").append(":").append((null != launcher ? launcher : "")).append("\n");
        sb.append("input").append(":").append((null != input ? input : "")).append("\n");
        sb.append("open").append(":").append(open).append("\n");
        sb.append("w_main").append(":").append(w_main).append("\n");
        sb.append("w_search").append(":").append(w_search).append("\n");
        sb.append("w_infos").append(":").append(w_infos).append("\n");
        sb.append("w_play").append(":").append(w_play).append("\n");
        sb.append("w_install").append(":").append(w_install).append("\n");
        sb.append("w_input").append(":").append(w_input).append("\n");
        sb.append("time_out").append(":").append(time_out).append("\n");
        sb.append("swipe").append(":").append(swipe).append("\n");
        sb.append("swipe_time_out").append(":").append(swipe_time_out).append("\n");
        sb.append("before_start_sleep").append(":").append(before_start_sleep).append("\n");
        sb.append("clear_app_sleep").append(":").append(clear_app_sleep).append("\n");
        sb.append("clear_sd_sleep").append(":").append(clear_sd_sleep).append("\n");
        sb.append("launch_market_sleep").append(":").append(launch_market_sleep).append("\n");
        sb.append("run_blank").append(":").append(run_blank).append("\n");
        sb.append("install_ready_time").append(":").append(install_ready_time).append("\n");
        sb.append("open_ready_time").append(":").append(open_ready_time).append("\n");
        sb.append("m_type").append(":").append(m_type).append("\n");
        sb.append("qh360type").append(":").append(qh360type).append("\n");
        sb.append("qh360typekey").append(":").append(qh360typekey).append("\n");
        sb.append("qh360typeindex").append(":").append(qh360typeindex).append("\n");
        sb.append("local_name").append(":").append(local_name).append("\n");
        sb.append("down_times").append(":").append(down_times).append("\n");
        sb.append("app_name").append(":").append(app_name).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
