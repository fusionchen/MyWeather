package me.myweather.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import me.myweather.app.R;
import me.myweather.app.adapter.WeatherMessageAdapter;
import me.myweather.app.been.BeenFactory;
import me.myweather.app.been.NowWeather;
import me.myweather.app.been.WeatherMessage;
import me.myweather.app.tool.NumberWeekTool;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_JSON_STRING = "json_string";

    // TODO: Rename and change types of parameters
    private int selectionNumber;
    private String jsonString;

    //views
    private ListView lvWeather;
    private TextView tvCity;
    private TextView tvStatusNow;
    private TextView tvTemperatureNow;
    private TextView tvDay1Week;
    private TextView tvDay1DayTempeerature;
    private TextView tvDay1NightTemperature;


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selectionNumber 位置
     * @param jsonString 内容数据
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(int selectionNumber, String jsonString) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, selectionNumber);
        args.putString(ARG_JSON_STRING, jsonString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            jsonString = getArguments().getString(ARG_JSON_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        injectData(rootView);
        return rootView;
    }

    /**
     * 通过获取的json数据生成页面内容
     * @param rootView fragment根控件
     */
    public void injectData(View rootView) {
        if(jsonString != null) {
            String[] strings = jsonString.split(BeenFactory.SPLIT_STRING);
            if(strings.length < 2)
                return;
            WeatherMessage weatherMessage = BeenFactory.getInstance(strings[0], WeatherMessage.class);
            NowWeather nowWeather = BeenFactory.getInstance(strings[1], NowWeather.class);
            if(weatherMessage.isNull() || nowWeather.isNull())
                return;
            WeatherMessage.ForecastsBean.CastsBean day1Weather = weatherMessage.getForecasts().get(0).getCasts().get(0);
            NowWeather.LivesBean nowWeatherLive = nowWeather.getLives().get(0);
            tvCity = (TextView) rootView.findViewById(R.id.city);
            tvStatusNow = (TextView)rootView.findViewById(R.id.status_now);
            tvTemperatureNow = (TextView) rootView.findViewById(R.id.temperature_now);
            tvDay1Week = (TextView)rootView.findViewById(R.id.day1_week);
            tvDay1DayTempeerature = (TextView) rootView.findViewById(R.id.day1_day_temperature);
            tvDay1NightTemperature = (TextView) rootView.findViewById(R.id.day1_night_temperature);

            tvCity.setText(weatherMessage.getForecasts().get(0).getCity());
            tvStatusNow.setText(nowWeatherLive.getWeather());
            tvTemperatureNow.setText(nowWeatherLive.getTemperature() + "°");
            tvDay1Week.setText(NumberWeekTool.getWeekByNumber(day1Weather.getWeek()));
            tvDay1DayTempeerature.setText(day1Weather.getDaytemp());
            tvDay1NightTemperature.setText(day1Weather.getNighttemp());

            lvWeather = (ListView) rootView.findViewById(R.id.list_weather);
            lvWeather.setAdapter(new WeatherMessageAdapter(this.getContext(), weatherMessage));
        }
    }

}
