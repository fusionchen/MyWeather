package me.myweather.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.myweather.app.R;
import me.myweather.app.adapter.CityAdapter;
import me.myweather.app.been.CityCodes;
import me.myweather.app.been.NowWeather;
import me.myweather.app.tool.CityNameCodeTool;
import me.myweather.app.tool.HttpTool;
import me.myweather.app.tool.JsonTool;
import me.myweather.app.tool.WeatherURLTool;
import okhttp3.Call;
import xyz.sahildave.widget.SearchViewLayout;

public class ManageCityActivity extends AppCompatActivity {

    CityCodes cityCodes = new CityCodes();
    CityAdapter cityAdapter;
    HashMap<String, String> nowWeatherHashMap = new HashMap<>();
    ArrayList<String> citynames = new ArrayList<>();
    SwipeMenuListView listView;
    ImageButton ibAddCity;
    RotateLoading loading;
    MaterialSearchBar searchBar;
    View searchLayout;
    View returnView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_city);
        //init data
        cityCodes.loadCityCodes();
        nowWeatherHashMap = (HashMap<String, String>) getIntent().getSerializableExtra(JsonTool.KEY_NOW_WEATHER);
        //init search view
        searchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                citynames = CityNameCodeTool.findCity(s.toString());
                searchBar.updateLastSuggestions(citynames);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int i, View view) {
                returnView.callOnClick();
                addCity(citynames.get(i));
            }

            @Override
            public void OnItemDeleteListener(int i, View view) {

            }
        });

        searchLayout = findViewById(R.id.search_layout);
        returnView = findViewById(R.id.return_view);
        returnView.setOnClickListener((view) -> {
            searchBar.disableSearch();
            searchLayout.setVisibility(View.GONE);
        });

        //init list view
        listView = (SwipeMenuListView) findViewById(R.id.city_list);
        SwipeMenuCreator creator = menu -> {
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
            deleteItem.setTitleSize(dp2px(20));
            // add to menu
            menu.addMenuItem(deleteItem);
        };
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setOnMenuItemClickListener((position, menu, index) -> {
            switch (index) {
                case 0:
                    deleteCity(position);
                    break;
            }
            return false;
        });
        cityAdapter = new CityAdapter(this, nowWeatherHashMap, cityCodes);
        listView.setAdapter(cityAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent();
            i.putExtra(MainActivity.KEY_POS + "", position);
            setResult(MainActivity.KEY_POS, i);
            finish();
        });
        loading = (RotateLoading) findViewById(R.id.loading);
        ibAddCity = (ImageButton) findViewById(R.id.add_city);
        ibAddCity.setOnClickListener((view) -> {
            searchLayout.setVisibility(View.VISIBLE);
        });
    }

    private int dp2px(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void addCity(String cityname) {
        String citycode = CityNameCodeTool.name2code(cityname);
        if(cityCodes.add(citycode) == false)
            return;
        loading.start();
        cityCodes.saveCityCodes();
        final NowWeather[] nowWeather = {NowWeather.getDefaultInstance()};
        HttpTool.getInstance(new HttpTool.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(()->{
                    loading.stop();
                    cityAdapter.addItem(citycode, nowWeather[0]);
                });
            }

            @Override
            public void onResponse(String response) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(()->{
                    loading.stop();
                    nowWeather[0] = JsonTool.getInstance(response, NowWeather.class);
                    cityAdapter.addItem(citycode, nowWeather[0]);
                });
            }
        }).sendGet(WeatherURLTool.getNowWeatherURL(citycode));
    }

    private void deleteCity(int pos) {
        if(pos >= cityCodes.size())
            return;
        cityCodes.remove(pos);
        cityCodes.saveCityCodes();
        cityAdapter.removeItem(pos);
    }
}
