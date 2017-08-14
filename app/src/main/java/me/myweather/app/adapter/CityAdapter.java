package me.myweather.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.myweather.app.R;
import me.myweather.app.been.CityCodes;
import me.myweather.app.been.NowWeather;
import me.myweather.app.factory.WeatherBackgroundFactory;
import me.myweather.app.tool.CityNameCodeTool;
import me.myweather.app.tool.JsonTool;

/**
 * Created by admin on 2017/8/14.
 */

public class CityAdapter extends BaseAdapter {

    private CityCodes cityCodes;
    private HashMap<String, NowWeather> nowWeatherHashMap = new HashMap<>();
    private Context context;

    public CityAdapter(Context context, HashMap<String, String> nowWeatherHashMap, CityCodes cityCodes) {
        this.context = context;
        this.nowWeatherHashMap.clear();
        for(String citycode : cityCodes) {
            this.nowWeatherHashMap.put(citycode, JsonTool.getInstance(nowWeatherHashMap.get(citycode), NowWeather.class));
        }
        this.cityCodes = cityCodes;
    }

    @Override
    public int getCount() {
        return cityCodes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tag tag = null;
        String citycode = cityCodes.get(position);
        String cityname = CityNameCodeTool.code2name(citycode);

        if(convertView == null) {
            tag = new Tag();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_manage_city, null);
            tag.ivBG = (ImageView) convertView.findViewById(R.id.bg);
            tag.tvCity = (TextView) convertView.findViewById(R.id.city);
            tag.tvTime = (TextView) convertView.findViewById(R.id.time);
            tag.tvTemperature = (TextView) convertView.findViewById(R.id.temperature);
            convertView.setTag(tag);
        } else {
            tag = (Tag) convertView.getTag();
        }
        tag.tvCity.setText(cityname);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        tag.tvTime.setText(simpleDateFormat.format(new Date()));
        int bgID = R.drawable.launch_image;
        if(nowWeatherHashMap.get(citycode) != null) {
            NowWeather.LivesBean livesBean = nowWeatherHashMap.get(citycode).getLives().get(0);
            tag.tvTemperature.setText(livesBean.getTemperature() + "°");
            bgID = WeatherBackgroundFactory.getResource(livesBean.getWeather());
        }
        tag.ivBG.setImageResource(bgID);
        return convertView;
    }

    class Tag {
        ImageView ivBG;
        TextView tvTime;
        TextView tvCity;
        TextView tvTemperature;
    }
}
