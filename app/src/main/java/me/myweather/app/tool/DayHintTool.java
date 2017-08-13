package me.myweather.app.tool;

/**
 * Created by guyu on 2017/8/12.
 */

public class DayHintTool {
    public static String getHint(String dayWeather, String dayWind, String dayTemp, String nightWeather, String nightWind, String nightTemp) {
        String result = "今日：白天" + dayWeather
                + "，" + dayWind + "风，最高气温" + dayTemp
                + "°。" + "夜间" + nightWeather + "，"
                + nightWind + "风，最低气温" + nightTemp + "°。";
        return result;
    }
    public static String getWindVector(String wind) {
        String result = "风向：" + wind  + "风";
        return result;
    }
    public static String getWindSpeed(String windSpeed) {
        String result = "风速：" + windSpeed + "米/秒";
        return result;
    }
    public static String getHumidity(String humidity) {
        String result = "湿度：" + humidity + "%";
        return result;
    }
}
