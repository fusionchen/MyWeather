package me.myweather.app.tool;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by guyu on 2017/8/15.
 */

public class LocationTool {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    CitycodeListener citycodeListener;

    private LocationTool() {
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        mLocationOption.setHttpTimeOut(10000);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    }

    public static LocationTool getInstance(Context context, CitycodeListener citycodeListener) {
        LocationTool locationTool = new LocationTool();
        locationTool.mLocationClient = new AMapLocationClient(context);
        locationTool.citycodeListener = citycodeListener;
        locationTool.mLocationClient.setLocationListener(aMapLocation -> {
            if(aMapLocation.getErrorCode() == 0) {
                String citycode = aMapLocation.getAdCode();
                String cityname = aMapLocation.getCity();
                CityNameCodeTool.putCity(cityname, citycode);
                citycodeListener.onResult(citycode, cityname);
            } else {
                citycodeListener.onResult(null, null);
            }
        });
        locationTool.mLocationClient.setLocationOption(locationTool.mLocationOption);
        return locationTool;
    }

    public static interface CitycodeListener {
        public void onResult(String citycode, String cityname);
    }

    public void location() {
        mLocationClient.startLocation();
    }

    public void stopLoaction() {
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    public void destory() {
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }
}
