package me.myweather.app.been;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import me.myweather.app.tool.PreferenceTool;

/**
 * Created by admin on 2017/8/11.
 */

public class CityCodes extends ArrayList<String> {

    final public static String KEY_CITY_CODES = "city_codes";

    public CityCodes(){
        super();
    }
    public CityCodes(ArrayList<String> cityCodes) {
        setCityCodes(cityCodes);
    }
    public void setCityCodes(ArrayList<String> cityCodes) {
        this.clear();
        for(String string : cityCodes) {
            this.add(string);
        }
    }
    public void setCityCodesByJson(String jsonString) {
        CityCodes cityCodes = new GsonBuilder().create().fromJson(jsonString, getClass());
        setCityCodes(cityCodes);
    }
    public void setCityCodes(String...cityCodes) {
        this.clear();
        for(String string : cityCodes) {
            this.add(string);
        }
    }
    public String getCityCodesString() {
        return new GsonBuilder().create().toJson(this, getClass());
    }
    public boolean add(String element) {
        for(String str : this) {
            if(str.equals(element))
                return false;
        }
        super.add(element);
        return true;
    }
    public int loadCityCodes() {
        int nowSize = size();
        String jCityCodes = PreferenceTool.load(KEY_CITY_CODES, null);
        if(jCityCodes != null)
            this.setCityCodesByJson(jCityCodes);
        if(this.isEmpty())
            this.add("");
        return size() - nowSize;
    }
    public void saveCityCodes() {
        PreferenceTool.save(KEY_CITY_CODES, getCityCodesString());
    }
}
