package me.myweather.app.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/8/11.
 */

public class JsonTool {
    final public static String SPLIT_STRING = "<<@split>>";
    final public static String KEY_NOW_WEATHER = "now_weather";
    private static Gson gson = new GsonBuilder().create();
    public static <T> T getInstance(String jsonString, Type classOfT) {
        T instance = gson.fromJson(jsonString, classOfT);
        return instance;
    }
    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }
    public static String toJsonString(Object object, Type classOfT) {
        return gson.toJson(object, classOfT);
    }
}
