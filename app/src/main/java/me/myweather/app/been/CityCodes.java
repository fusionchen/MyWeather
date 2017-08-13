package me.myweather.app.been;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by admin on 2017/8/11.
 */

public class CityCodes extends ArrayList<String> {
    private ArrayList<String> cityCodes;
    public CityCodes(){
        super();
    }
    public CityCodes(ArrayList<String> cityCodes) {
        setCityCodes(cityCodes);
    }
    public void setCityCodes(ArrayList<String> cityCodes) {
        this.cityCodes = cityCodes;
        this.clear();
        this.addAll(this.cityCodes);
    }
    public void setCityCodes(String jsonString) {
        this.cityCodes = new GsonBuilder().create().fromJson(jsonString, new TypeToken<ArrayList<String>>(){}.getType());
        setCityCodes(this.cityCodes);
    }
    public void setCityCodes(String...cityCodes) {
        this.cityCodes = new ArrayList<>();
        for(String string : cityCodes) {
            this.add(string);
        }
        this.clear();
        this.addAll(this.cityCodes);
    }
    public String getCityCodesString() {
        return new GsonBuilder().create().toJson(cityCodes, new TypeToken<ArrayList<String>>(){}.getType());
    }
    public boolean add(String element) {
        for(String str : this) {
            if(str.equals(element))
                return false;
        }
        super.add(element);
        return true;
    }
}
