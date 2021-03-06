package me.myweather.app.been;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/11.
 */

public class WeatherMessage {

    /**
     * status : 1
     * count : 1
     * info : OK
     * infocode : 10000
     * forecasts : [{"city":"沈阳市","adcode":"210100","province":"辽宁","reporttime":"2017-08-11 08:00:00","casts":[{"date":"2017-08-11","week":"5","dayweather":"多云","nightweather":"多云","daytemp":"31","nighttemp":"22","daywind":"西南","nightwind":"西南","daypower":"≤3","nightpower":"≤3"},{"date":"2017-08-12","week":"6","dayweather":"多云","nightweather":"多云","daytemp":"31","nighttemp":"22","daywind":"西南","nightwind":"西南","daypower":"≤3","nightpower":"≤3"},{"date":"2017-08-13","week":"7","dayweather":"多云","nightweather":"多云","daytemp":"31","nighttemp":"22","daywind":"西南","nightwind":"北","daypower":"≤3","nightpower":"≤3"},{"date":"2017-08-14","week":"1","dayweather":"雷阵雨","nightweather":"雷阵雨","daytemp":"27","nighttemp":"21","daywind":"北","nightwind":"北","daypower":"≤3","nightpower":"≤3"}]}]
     */

    private String status;
    private String count;
    private String info;
    private String infocode;
    private List<ForecastsBean> forecasts;

    public WeatherMessage() {
        forecasts = new ArrayList<>();
        ForecastsBean forecastsBean = new ForecastsBean();
        forecasts.add(forecastsBean);
        forecastsBean.setCity("——");
        forecastsBean.casts = new ArrayList<>();
    }

    public static WeatherMessage getDefaultInstance() {
        WeatherMessage weatherMessage = new WeatherMessage();
        return weatherMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<ForecastsBean> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<ForecastsBean> forecasts) {
        this.forecasts = forecasts;
    }

    public static class ForecastsBean {
        /**
         * city : 沈阳市
         * adcode : 210100
         * province : 辽宁
         * reporttime : 2017-08-11 08:00:00
         * casts : [{"date":"2017-08-11","week":"5","dayweather":"多云","nightweather":"多云","daytemp":"31","nighttemp":"22","daywind":"西南","nightwind":"西南","daypower":"≤3","nightpower":"≤3"},{"date":"2017-08-12","week":"6","dayweather":"多云","nightweather":"多云","daytemp":"31","nighttemp":"22","daywind":"西南","nightwind":"西南","daypower":"≤3","nightpower":"≤3"},{"date":"2017-08-13","week":"7","dayweather":"多云","nightweather":"多云","daytemp":"31","nighttemp":"22","daywind":"西南","nightwind":"北","daypower":"≤3","nightpower":"≤3"},{"date":"2017-08-14","week":"1","dayweather":"雷阵雨","nightweather":"雷阵雨","daytemp":"27","nighttemp":"21","daywind":"北","nightwind":"北","daypower":"≤3","nightpower":"≤3"}]
         */

        private String city = "——";
        private String adcode;
        private String province;
        private String reporttime;
        private List<CastsBean> casts;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getReporttime() {
            return reporttime;
        }

        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }

        public List<CastsBean> getCasts() {
            return casts;
        }

        public void setCasts(List<CastsBean> casts) {
            this.casts = casts;
        }

        public static class CastsBean {
            /**
             * date : 2017-08-11
             * week : 5
             * dayweather : 多云
             * nightweather : 多云
             * daytemp : 31
             * nighttemp : 22
             * daywind : 西南
             * nightwind : 西南
             * daypower : ≤3
             * nightpower : ≤3
             */

            private String date;
            private String week;
            private String dayweather;
            private String nightweather;
            private String daytemp;
            private String nighttemp;
            private String daywind;
            private String nightwind;
            private String daypower;
            private String nightpower;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getDayweather() {
                return dayweather;
            }

            public void setDayweather(String dayweather) {
                this.dayweather = dayweather;
            }

            public String getNightweather() {
                return nightweather;
            }

            public void setNightweather(String nightweather) {
                this.nightweather = nightweather;
            }

            public String getDaytemp() {
                return daytemp;
            }

            public void setDaytemp(String daytemp) {
                this.daytemp = daytemp;
            }

            public String getNighttemp() {
                return nighttemp;
            }

            public void setNighttemp(String nighttemp) {
                this.nighttemp = nighttemp;
            }

            public String getDaywind() {
                return daywind;
            }

            public void setDaywind(String daywind) {
                this.daywind = daywind;
            }

            public String getNightwind() {
                return nightwind;
            }

            public void setNightwind(String nightwind) {
                this.nightwind = nightwind;
            }

            public String getDaypower() {
                return daypower;
            }

            public void setDaypower(String daypower) {
                this.daypower = daypower;
            }

            public String getNightpower() {
                return nightpower;
            }

            public void setNightpower(String nightpower) {
                this.nightpower = nightpower;
            }
        }
    }

    public boolean isNull(){
        if(forecasts == null || forecasts.isEmpty())
            return true;
        else if(forecasts.get(0).casts == null || forecasts.get(0).casts.isEmpty())
            return true;
        else
            return false;
    }
}
