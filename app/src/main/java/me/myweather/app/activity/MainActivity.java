package me.myweather.app.activity;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.HashMap;

import me.myweather.app.R;
import me.myweather.app.been.BeenFactory;
import me.myweather.app.been.CityCodes;
import me.myweather.app.been.NowWeather;
import me.myweather.app.been.WeatherMessage;
import me.myweather.app.fragment.MainFragment;
import me.myweather.app.tool.WeatherURLTool;
import me.myweather.app.tool.HttpTool;
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
    private Button manageCityButton;
    private RotateLoading rotateLoading;
    private HttpTool refreshTool;
    private CityCodes cityCodes = new CityCodes();
    private HashMap<String, String> weatherMessageHashMap = new HashMap<>();
    private HashMap<String, String> nowWeatherHashMap = new HashMap<>();
    private int refreshTimes = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //my init actions
        init();


        manageCityButton = (Button) findViewById(R.id.button_manage_city);
        manageCityButton.setOnClickListener((view)->{
            cityCodes.add("150100");
            sendGetWeather();
        });
    }

    private void init() {
        rotateLoading = (RotateLoading) findViewById(R.id.loading);
        initCity();
        initWeather();
    }

    private void initCity() {
        cityCodes.setCityCodes("210100", "110000");
    }

    private void initWeather() {
        sendGetWeather();
    }

    private void createViewPager() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void sendGetWeather() {
        rotateLoading.start();
        refreshTimes = 0;
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
                    rotateLoading.stop();
                    Toast.makeText(MainActivity.this, "天气信息获取失败…", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(String response) {
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
                    rotateLoading.stop();
                    Toast.makeText(MainActivity.this, "天气信息获取失败…", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(String response) {
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
            Thread.sleep(2000);
            runOnUiThread(()->{
                createViewPager();
                rotateLoading.stop();
                Toast.makeText(MainActivity.this, "天气信息获取成功！", Toast.LENGTH_SHORT).show();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            return MainFragment.newInstance(position + 1, weatherMessage + BeenFactory.SPLIT_STRING + nowWeather);
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
