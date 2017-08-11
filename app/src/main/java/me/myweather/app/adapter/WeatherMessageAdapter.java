package me.myweather.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import me.myweather.app.R;
import me.myweather.app.been.WeatherMessage;
import me.myweather.app.tool.NumberWeekTool;

/**
 * Created by admin on 2017/8/11.
 */

public class WeatherMessageAdapter extends BaseAdapter {

    WeatherMessage weatherMessage;
    List<WeatherMessage.ForecastsBean.CastsBean> weathers;
    private LayoutInflater layoutInflater;
    private  Context context;

    public WeatherMessageAdapter(Context context, WeatherMessage weatherMessage) {
        this.weatherMessage = weatherMessage;
        this.weathers = weatherMessage.getForecasts().get(0).getCasts();
        this.weathers.remove(0);
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return weathers.size();
    }

    @Override
    public Object getItem(int position) {
        return weathers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherTag weatherTag = null;
        WeatherMessage.ForecastsBean.CastsBean weather = weathers.get(position);
        if(convertView == null) {
            weatherTag = new WeatherTag();
            convertView = layoutInflater.inflate(R.layout.item_day_info, null);
            weatherTag.tvWeek = (TextView) convertView.findViewById(R.id.week);
            weatherTag.tvDayTemperature = (TextView) convertView.findViewById(R.id.day_temperature);
            weatherTag.tvNightTemperature = (TextView) convertView.findViewById(R.id.night_temperature);
            weatherTag.ivIcon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(weatherTag);
        } else {
            weatherTag = (WeatherTag) convertView.getTag();
        }
        weatherTag.tvWeek.setText(NumberWeekTool.getWeekByNumber(weather.getWeek()));
        weatherTag.tvDayTemperature.setText(weather.getDaytemp());
        weatherTag.tvNightTemperature.setText(weather.getNighttemp());
        return convertView;
    }

    final class WeatherTag {
        public TextView tvWeek;
        public ImageView ivIcon;
        public TextView tvDayTemperature;
        public TextView tvNightTemperature;
    }
}
