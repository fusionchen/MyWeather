package me.myweather.app.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

/**
 * Created by admin on 2017/8/14.
 */

public class PreferenceTool {
    public static Context context;
    final public static String DEF_NAME = "#def_name#6&83decf";
    public static void init(Context context) {
        PreferenceTool.context = context;
    }
    public static void save(String name, String key, String value) {
        SharedPreferences preference = context.getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void save(String key, String value) {
        save(DEF_NAME, key, value);
    }
    public static String load(String name, String key, String defValue) {
        SharedPreferences preference = context.getSharedPreferences("", Context.MODE_PRIVATE);
        return preference.getString(key, defValue);
    }
    public static String load(String key, String defValue) {
        return load(DEF_NAME, key, defValue);
    }

}
