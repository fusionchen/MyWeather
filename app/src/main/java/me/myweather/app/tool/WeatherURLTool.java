package me.myweather.app.tool;

/**
 * Created by admin on 2017/8/11.
 */

public class WeatherURLTool {
    final private static String GD_KEY = "0d59d944cff6f4b0ddc37c21567b3c03";
    public static String getCityNumberURL(String cityName) {
        String url = "http://restapi.amap.com/v3/config/district?keywords=" + cityName + "&subdistrict=0&key=" + GD_KEY;
        return url;
    }
    public static String getCityWeatherURL(String cityNumber) {
        String url = "http://restapi.amap.com/v3/weather/weatherInfo?city=" + cityNumber + "&key=" + GD_KEY + "&extensions=all";
        return url;
    }
    public static String getNowWeatherURL(String cityNumber) {
        String url = "http://restapi.amap.com/v3/weather/weatherInfo?city=" + cityNumber + "&key=" + GD_KEY;
        return url;
    }
}
