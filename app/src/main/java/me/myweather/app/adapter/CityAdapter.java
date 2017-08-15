package me.myweather.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
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

    private CityCodes cityCodes = new CityCodes();
    private HashMap<String, NowWeather> nowWeatherHashMap = new HashMap<>();
    private Context context;

    public CityAdapter(Context context, HashMap<String, String> nowWeatherHashMap, CityCodes cityCodes) {
        this.context = context;
        this.nowWeatherHashMap.clear();
        for(String citycode : cityCodes) {
            this.nowWeatherHashMap.put(citycode, JsonTool.getInstance(nowWeatherHashMap.get(citycode), NowWeather.class));
        }
        this.cityCodes.addAll(cityCodes);
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
        ViewHolder viewHolder = null;
        String citycode = cityCodes.get(position);
        String cityname = CityNameCodeTool.code2name(citycode);
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_manage_city, null);
            viewHolder.ivBG = (ImageView) convertView.findViewById(R.id.bg);
            viewHolder.tvCity = (TextView) convertView.findViewById(R.id.city);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.time);
            viewHolder.tvTemperature = (TextView) convertView.findViewById(R.id.temperature);
            viewHolder.ivLocation = (ImageView) convertView.findViewById(R.id.location);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position == 0)
            viewHolder.ivLocation.setVisibility(View.VISIBLE);
        viewHolder.tvCity.setText(cityname);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        viewHolder.tvTime.setText(simpleDateFormat.format(new Date()));
        int bgID = R.drawable.launch_image;
        if(nowWeatherHashMap.get(citycode) != null && !nowWeatherHashMap.get(citycode).isNull()) {
            NowWeather.LivesBean livesBean = nowWeatherHashMap.get(citycode).getLives().get(0);
            viewHolder.tvTemperature.setText(livesBean.getTemperature() + "°");
            bgID = WeatherBackgroundFactory.getResource(livesBean.getWeather());
        }
        viewHolder.ivBG.setImageResource(bgID);
        return convertView;
    }

    class ViewHolder {
        ImageView ivBG;
        TextView tvTime;
        TextView tvCity;
        TextView tvTemperature;
        ImageView ivLocation;
    }

    public void addItem(String citycode, NowWeather nowWeather) {
        cityCodes.add(citycode);
        nowWeatherHashMap.put(citycode, nowWeather);
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        nowWeatherHashMap.remove(cityCodes.get(pos));
        cityCodes.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        // menu type count
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // current menu type
        if(position == 0)
            return 0;
        else
            return 1;
    }
}
