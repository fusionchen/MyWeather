package me.myweather.app.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;
import com.victor.loading.rotate.RotateLoading;

import java.util.HashMap;

import me.myweather.app.R;
import me.myweather.app.adapter.CityAdapter;
import me.myweather.app.been.CityCodes;
import me.myweather.app.been.NowWeather;
import me.myweather.app.tool.JsonTool;

public class ManageCityActivity extends AppCompatActivity {

    CityCodes cityCodes = new CityCodes();
    HashMap<String, String> nowWeatherHashMap = new HashMap<>();
    SwipeMenuListView listView;
    ImageButton ibAddCity;
    RotateLoading loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_city);
        cityCodes.loadCityCodes();
        nowWeatherHashMap = (HashMap<String, String>) getIntent().getSerializableExtra(JsonTool.KEY_NOW_WEATHER);
        listView = (SwipeMenuListView) findViewById(R.id.city_list);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(dp2px(30));
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setOnMenuItemClickListener((position, menu, index) -> {
            switch (index) {
                case 0:
                    break;
            }
            return false;
        });
        CityAdapter cityAdapter = new CityAdapter(this, nowWeatherHashMap, cityCodes);
        listView.setAdapter(cityAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {

        });
        loading = (RotateLoading) findViewById(R.id.loading);
        ibAddCity = (ImageButton) findViewById(R.id.add_city);
        ibAddCity.setOnClickListener((view) -> {
            loading();
        });
    }

    private int dp2px(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void loading() {
        if(loading.isStart())
            loading.stop();
        else
            loading.start();
    }
}
