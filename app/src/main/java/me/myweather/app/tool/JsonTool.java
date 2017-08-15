package me.myweather.app.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import me.myweather.app.been.NowWeather;
import me.myweather.app.been.WeatherMessage;

/**
 * Created by admin on 2017/8/11.
 */

public class JsonTool {
    final public static String SPLIT_STRING = "<<@split>>";
    final public static String KEY_NOW_WEATHER = "now_weather";
    private static Gson gson = new GsonBuilder().create();
    public static <T> T getInstance(String jsonString, Type classOfT) {
        try {
            T instance = gson.fromJson(jsonString, classOfT);
            return instance;
        } catch (Exception e) {
            if(classOfT == NowWeather.class)
                return (T) NowWeather.getDefaultInstance();
            else if(classOfT == WeatherMessage.class)
                return (T) WeatherMessage.getDefaultInstance();
            else
                return null;
        }
    }
    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }
    public static String toJsonString(Object object, Type classOfT) {
        return gson.toJson(object, classOfT);
    }
}
