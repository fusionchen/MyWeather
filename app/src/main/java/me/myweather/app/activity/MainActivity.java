package me.myweather.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import me.myweather.app.R;
import me.myweather.app.tool.JsonTool;
import me.myweather.app.been.CityCodes;
import me.myweather.app.been.NowWeather;
import me.myweather.app.been.WeatherMessage;
import me.myweather.app.fragment.MainFragment;
import me.myweather.app.tool.CityNameCodeTool;
import me.myweather.app.tool.LocationTool;
import me.myweather.app.tool.PicTool;
import me.myweather.app.tool.PreferenceTool;
import me.myweather.app.tool.WeatherURLTool;
import me.myweather.app.tool.HttpTool;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    final public static int KEY_POS = 0;

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
    private ImageButton ibImageCity;
    private RotateLoading rotateLoading;
    private CircleIndicator circleIndicator;

    private CityCodes cityCodes = new CityCodes();
    private int currentPage = 0;
    private HashMap<String, String> weatherMessageHashMap = new HashMap<>();
    private HashMap<String, String> nowWeatherHashMap = new HashMap<>();
    private int refreshTimes = 0;
    private boolean isSending = false;
    private boolean pageFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //my init actions
        init();

        ibImageCity = (ImageButton) findViewById(R.id.button_manage_city);
        ibImageCity.setOnClickListener((view) -> {
            Intent i = new Intent();
            i.putExtra(JsonTool.KEY_NOW_WEATHER, nowWeatherHashMap);
            i.setClass(this, ManageCityActivity.class);
            startActivityForResult(i, KEY_POS);
        });
        rotateLoading.setOnClickListener((view)->{
            if(rotateLoading.isStart()){
                rotateLoading.stop();
                HttpTool.disconnectAll();
                isSending = false;
                return;
            }
            initLocation();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initCity();
        refreshViewPager();
        switch (requestCode) {
            case KEY_POS:
                if(data == null)
                    return;
                int pos = data.getIntExtra(KEY_POS + "", 0);
                this.currentPage = pos;
                if(pos < cityCodes.size())
                    setCurrentPage(pos);
                pageFlag = true;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCity();
        initLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HttpTool.disconnectAll();
        cityCodes.saveCityCodes();
    }

    private void init() {
        rotateLoading = (RotateLoading) findViewById(R.id.loading);
        PreferenceTool.init(this);
        CityNameCodeTool.init(this);
        PicTool.init(this);
        initViewPager();
    }

    private void initLocation() {
        rotateLoading.start();
        LocationTool locationTool = LocationTool.getInstance(this, (citycode, cityname) -> {
            if(citycode != null) {
                cityCodes.set(0, citycode);
                cityCodes.saveCityCodes();
            } else {
                Toast.makeText(this, "定位失败" , Toast.LENGTH_SHORT).show();
            }
            sendGetWeather();
        });
        locationTool.location();
    }

    private void initCity() {
        cityCodes.loadCityCodes();
    }

    private void initViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        circleIndicator.setViewPager(mViewPager);

    }

    private void refreshViewPager() {
        if(getSupportFragmentManager().getFragments() != null)
            getSupportFragmentManager().getFragments().clear();
        mSectionsPagerAdapter.notifyDataSetChanged();
        circleIndicator.setViewPager(mViewPager);
    }

    private void sendGetWeather() {
        if(cityCodes.isEmpty()) {
            refreshViewPager();
            return;
        }
        if(isSending)
            return;
        if(mViewPager != null && pageFlag == false)
            currentPage = mViewPager.getCurrentItem();
        pageFlag = false;
        rotateLoading.start();
        refreshTimes = 0;
        lockSendFlag();
        weatherMessageHashMap.clear();
        nowWeatherHashMap.clear();
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
                    Toast.makeText(MainActivity.this, "连接超时，天气信息获取失败…", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "连接超时，天气信息获取失败…", Toast.LENGTH_SHORT).show();
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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()->{
                    mSectionsPagerAdapter.notifyDataSetChanged();
                    refreshViewPager();
                    setCurrentPage(currentPage);
                    unlockSendFlag();
                    rotateLoading.stop();
                    //Toast.makeText(MainActivity.this, "天气信息获取成功！", Toast.LENGTH_SHORT).show();
                });
            }
        }, 2000);
    }

    public void lockSendFlag() {
        isSending = true;
    }
    
    public void unlockSendFlag() {
        isSending = false;
    }

    public void setCurrentPage(int pos) {
        mViewPager.setCurrentItem(pos);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int count = 0;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).\
            String weatherMessage = weatherMessageHashMap.get(cityCodes.get(position));
            String nowWeather = nowWeatherHashMap.get(cityCodes.get(position));
            return MainFragment.newInstance(position + 1, weatherMessage + JsonTool.SPLIT_STRING + nowWeather, cityCodes.get(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
               return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            count = cityCodes.size();
            super.notifyDataSetChanged();
        }
    }
}
