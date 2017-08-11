package me.myweather.app.been;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by admin on 2017/8/11.
 */

public class BeenFactory {
    final public static String SPLIT_STRING = "<<@split>>";
    private static Gson gson = new GsonBuilder().create();
    public static <T> T getInstance(String jsonString, Class<T> classOfT) {
        T instance = gson.fromJson(jsonString, classOfT);
        return instance;
    }
}
