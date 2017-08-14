package me.myweather.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import me.myweather.app.R;
import me.myweather.app.tool.JsonTool;
import me.myweather.app.been.CityCodes;
import me.myweather.app.been.NowWeather;
import me.myweather.app.been.WeatherMessage;
import me.myweather.app.fragment.MainFragment;
import me.myweather.app.tool.CityNameCodeTool;
import me.myweather.app.tool.PreferenceTool;
import me.myweather.app.tool.WeatherURLTool;
import me.myweather.app.tool.HttpTool;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ImageButton manageCityButton;
    private RotateLoading rotateLoading;
    private CircleIndicator circleIndicator;

    private HttpTool refreshTool;
    private CityCodes cityCodes = new CityCodes();
    private int currentPage = 0;
    private HashMap<String, String> weatherMessageHashMap = new HashMap<>();
    private HashMap<String, String> nowWeatherHashMap = new HashMap<>();
    private int refreshTimes = 0;
    private boolean sendFlag = false;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //my init actions
        init();

        manageCityButton = (ImageButton) findViewById(R.id.button_manage_city);
        manageCityButton.setOnClickListener((view) -> {
            Intent i = new Intent();
            i.putExtra(JsonTool.KEY_NOW_WEATHER, nowWeatherHashMap);
            i.setClass(this, ManageCityActivity.class);
            startActivity(i);
        });
        rotateLoading.setOnClickListener((view)->{
            if(rotateLoading.isStart()){
                rotateLoading.stop();
                HttpTool.disconnectAll();
                return;
            }
            sendGetWeather();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceTool.init(this);
        CityNameCodeTool.init(this);
        initCity();
        initWeather();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HttpTool.disconnectAll();
        cityCodes.saveCityCodes();
    }

    private void init() {
        rotateLoading = (RotateLoading) findViewById(R.id.loading);
    }

    private void initCity() {
        cityCodes.loadCityCodes();
    }

    private void initWeather() {
        sendGetWeather();
    }

    private void createViewPager() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        circleIndicator.setViewPager(mViewPager);
    }

    private void sendGetWeather() {
        if(sendFlag)
            return;
        if(mViewPager != null)
            currentPage = mViewPager.getCurrentItem();
        else
            currentPage = 0;
        rotateLoading.start();
        refreshTimes = 0;
        lockSendFlag();
        for(String cityCode : cityCodes) {
            sendGetWeatherMessage(cityCode);
            sendGetNowWeather(cityCode);
        }
    }

    public void sendGetWeatherMessage(String cityCode) {
        HttpTool.getInstance(new HttpTool.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    unlockSendFlag();
                    rotateLoading.stop();
                    Toast.makeText(MainActivity.this, "天气信息获取失败…", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(String response) {
                if(response == null) {
                    response = JsonTool.toJsonString(WeatherMessage.getDefaultInstance());
                }
                weatherMessageHashMap.put(cityCode, response);
                refreshTimes++;
                if(refreshTimes == cityCodes.size() * 2) {
                    refreshSuccess();
                }
            }
        }).sendGet(WeatherURLTool.getCityWeatherURL(cityCode));
    }

    public void sendGetNowWeather(String cityCode) {
        HttpTool.getInstance(new HttpTool.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    unlockSendFlag();
                    rotateLoading.stop();
                    Toast.makeText(MainActivity.this, "天气信息获取失败…", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(String response) {
                if(response == null) {
                    response = JsonTool.toJsonString(NowWeather.getDefaultInstance());
                }
                nowWeatherHashMap.put(cityCode, response);
                refreshTimes++;
                if(refreshTimes == cityCodes.size() * 2) {
                    refreshSuccess();
                }
            }
        }).sendGet(WeatherURLTool.getNowWeatherURL(cityCode));
    }

    public void refreshSuccess() {
        try {
            Thread.sleep(100);
            runOnUiThread(()->{
                createViewPager();
                setCurrentPage(currentPage);
                unlockSendFlag();
                rotateLoading.stop();
                Toast.makeText(MainActivity.this, "天气信息获取成功！", Toast.LENGTH_SHORT).show();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void lockSendFlag() {
        sendFlag = true;
    }
    
    public void unlockSendFlag() {
        sendFlag = false;
    }

    public void setCurrentPage(int pos) {
        //先强制设定跳转到指定页面
        try {
            Field field = mViewPager.getClass().getField("mCurItem");//参数mCurItem是系统自带的
            field.setAccessible(true);
            field.setInt(mViewPager,pos);
        }catch (Exception e){
            e.printStackTrace();
        }
        //然后调用下面的函数刷新数据
        mSectionsPagerAdapter.notifyDataSetChanged();
        //再调用setCurrentItem()函数设置一次
        mViewPager.setCurrentItem(pos);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            String weatherMessage = weatherMessageHashMap.get(cityCodes.get(position));
            String nowWeather = nowWeatherHashMap.get(cityCodes.get(position));
            return MainFragment.newInstance(position + 1, weatherMessage + JsonTool.SPLIT_STRING + nowWeather, cityCodes.get(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return cityCodes.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
               return null;
        }
    }
}
