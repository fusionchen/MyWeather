package me.myweather.app.factory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.myweather.app.R;

/**
 * Created by guyu on 2017/8/12.
 */

public class WeatherBackgroundFactory {

    public static int getDayResource(String name) {
        switch(name) {
            case "晴":return R.drawable.bg_day_sunny;  //
            case "多云":return R.drawable.bg_day_multicloud; //
            case "阴":return R.drawable.bg_day_overcast;  //
            case "阵雨":return R.drawable.bg_day_middlerain; //
            case "雷阵雨":return R.drawable.bg_day_thunder;    //
            case "雷阵雨并伴有冰雹":return R.drawable.bg_day_thunder;
            case "雨夹雪":return R.drawable.bg_day_smallrain;
            case "小雨":return R.drawable.bg_day_literain; //
            case "中雨":return R.drawable.bg_day_smallrain; //
            case "大雨":return R.drawable.bg_day_middlerain; //
            case "暴雨":return R.drawable.bg_day_hugerain; //
            case "大暴雨":return R.drawable.bg_day_hugerain;    //
            case "特大暴雨":return R.drawable.bg_day_hugerain;   //
            case "阵雪":return R.drawable.launch_image;
            case "小雪":return R.drawable.launch_image;
            case "中雪":return R.drawable.launch_image;
            case "大雪":return R.drawable.launch_image;
            case "暴雪":return R.drawable.launch_image;
            case "雾":return R.drawable.bg_day_fog;  //
            case "冻雨":return R.drawable.bg_day_smallrain;
            case "沙尘暴":return R.drawable.launch_image;
            case "小雨-中雨":return R.drawable.bg_day_smallrain;  //
            case "中雨-大雨":return R.drawable.bg_day_middlerain;  //
            case "大雨-暴雨":return R.drawable.bg_day_hugerain;  //
            case "暴雨-大暴雨":return R.drawable.bg_day_hugerain; //
            case "大暴雨-特大暴雨":return R.drawable.bg_day_hugerain;   //
            case "小雪-中雪":return R.drawable.launch_image;
            case "中雪-大雪":return R.drawable.launch_image;
            case "大雪-暴雪":return R.drawable.launch_image;
            case "浮尘":return R.drawable.launch_image;
            case "扬沙":return R.drawable.launch_image;
            case "强沙尘暴":return R.drawable.launch_image;
            case "飑":return R.drawable.launch_image;
            case "龙卷风":return R.drawable.launch_image;
            case "弱高吹雪":return R.drawable.launch_image;
            case "轻雾":return R.drawable.bg_day_fog; //
            case "霾":return R.drawable.bg_day_mai;  //
        }
        return R.drawable.launch_image;
    }
    
    public static int getNightResource(String name) {
        switch(name) {
            case "晴":return R.drawable.bg_night_sunny;  //
            case "多云":return R.drawable.bg_night_multicloud; //
            case "阴":return R.drawable.bg_night_overcast;  //
            case "阵雨":return R.drawable.bg_night_middlerain; //
            case "雷阵雨":return R.drawable.bg_night_thunder;    //
            case "雷阵雨并伴有冰雹":return R.drawable.bg_night_thunder;
            case "雨夹雪":return R.drawable.bg_night_smallrain;
            case "小雨":return R.drawable.bg_night_literain; //
            case "中雨":return R.drawable.bg_night_smallrain; //
            case "大雨":return R.drawable.bg_night_middlerain; //
            case "暴雨":return R.drawable.bg_night_hugerain; //
            case "大暴雨":return R.drawable.bg_night_hugerain;    //
            case "特大暴雨":return R.drawable.bg_night_hugerain;   //
            case "阵雪":return R.drawable.launch_image;
            case "小雪":return R.drawable.launch_image;
            case "中雪":return R.drawable.launch_image;
            case "大雪":return R.drawable.launch_image;
            case "暴雪":return R.drawable.launch_image;
            case "雾":return R.drawable.bg_night_fog;  //
            case "冻雨":return R.drawable.bg_night_smallrain;
            case "沙尘暴":return R.drawable.launch_image;
            case "小雨-中雨":return R.drawable.bg_night_smallrain;  //
            case "中雨-大雨":return R.drawable.bg_night_middlerain;  //
            case "大雨-暴雨":return R.drawable.bg_night_hugerain;  //
            case "暴雨-大暴雨":return R.drawable.bg_night_hugerain; //
            case "大暴雨-特大暴雨":return R.drawable.bg_night_hugerain;   //
            case "小雪-中雪":return R.drawable.launch_image;
            case "中雪-大雪":return R.drawable.launch_image;
            case "大雪-暴雪":return R.drawable.launch_image;
            case "浮尘":return R.drawable.launch_image;
            case "扬沙":return R.drawable.launch_image;
            case "强沙尘暴":return R.drawable.launch_image;
            case "飑":return R.drawable.launch_image;
            case "龙卷风":return R.drawable.launch_image;
            case "弱高吹雪":return R.drawable.launch_image;
            case "轻雾":return R.drawable.bg_night_fog; //
            case "霾":return R.drawable.bg_night_mai;  //
        }
        return R.drawable.launch_image;
    }

    public static int getResource(String name) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int hour = timestamp.getHours();
        if(hour < 7 || hour > 19)
            return getNightResource(name);
        else
            return getDayResource(name);
    }

    public static int getResource(String name, String reportTime) {
        //such as: 2017-08-11 14:00:00
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = simpleDateFormat.parse(reportTime);
            int hour = date.getHours();
            if(hour < 7 || hour > 19)
                return getNightResource(name);
            else
                return getDayResource(name);
        } catch (ParseException e) {
            return getResource(name);
        }
    }
}
