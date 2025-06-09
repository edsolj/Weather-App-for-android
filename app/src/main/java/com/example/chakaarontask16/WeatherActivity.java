package com.example.chakaarontask16;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import data.AirQualityData;
import data.DailyForecast;
import data.WeatherForecast;
import data.WeatherNow;
import utility.WeatherApiUtil;

import static db.CityDatabase.KEY_WEATHER_ID;

//t.em/etradingregistration
public class WeatherActivity extends AppCompatActivity {
    TextView tv_city, tv_update_time, tv_temp, tv_weather_info;
    ImageView iv_cond;
    String weather_id = "CN101210701";
    LinearLayout forecastLayout;
    TextView tv_aqi,tv_pm25;
    SwipeRefreshLayout swipeRefreshLayout;
    AtomicInteger requestCount=new AtomicInteger(0);
    public static final int CITY_REQ_CODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        loadWeatherId();
       // updateData();
        tv_aqi=findViewById(R.id.aqi_text);
        tv_pm25=findViewById(R.id.pm25_text);
        forecastLayout= (LinearLayout) findViewById(R.id.forecast_layout);
        tv_city = findViewById(R.id.title_city_tv);
        tv_update_time = findViewById(R.id.title_pub_time_tv);
        tv_temp = findViewById(R.id.now_temp_tv);
        tv_weather_info = findViewById(R.id.now_cond_tv);
        iv_cond=findViewById(R.id.now_cond_iv);
        //Button bt=findViewById(R.id.bt_update);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        tv_city.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(WeatherActivity.this,SelectCityActivity.class);
                startActivityForResult(i,CITY_REQ_CODE);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });
        updateData();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CITY_REQ_CODE&&resultCode== Activity.RESULT_OK){
            weather_id = SelectCityActivity.getWeatherIdByIntent(data);
            updateData();
            saveWeatherId();
        }
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CITY_REQ_CODE&&resultCode== Activity.RESULT_OK){

            saveWeatherId();
        }
    }*/
    private void loadWeatherId() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        weather_id=sharedPreferences.getString(KEY_WEATHER_ID,"CN101210701");
    }
    private void saveWeatherId() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_WEATHER_ID,weather_id);
        editor.apply();// async save;
// editor.commit();// block save;
    }

    private void updateData() {
        swipeRefreshLayout.setRefreshing(true);

        requestCount.set(0);
        updateWeatherNow();
        updateWeatherForecast();
        updateWeatherAqi();
        swipeRefreshLayout.setRefreshing(false);
    }
    private void updateRefreshState() {
        if (requestCount.incrementAndGet() == 3) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    private void updateWeatherNow() {
        WeatherApiUtil.getWeatherNow(this, weather_id, new WeatherApiUtil.OnWeatherNowFinished() {
            @Override
            public void onFinished(WeatherNow data) {
                updateRefreshState();
                if(data!=null){
                    tv_city.setText(data.basic.location);
                    tv_update_time.setText(data.update.loc);
                    tv_temp.setText(data.now.tmp);
                    tv_weather_info.setText(data.now.cond_txt);
                    updateWeatherIcon(data.now.cond_code,iv_cond); }}
        });
    }
    private void updateWeatherIcon(String cond_code, ImageView iv_cond)
    {
        String url =
                String.format("https://cdn.heweather.com/cond_icon/%s.png",cond_code);
        Glide.with(this).load(Uri.parse(url)).into(iv_cond);
    }
    private void updateWeatherAqi(){
        updateRefreshState();
        WeatherApiUtil.getAirQualityData(this, weather_id, new WeatherApiUtil.OnAirQualityFinished() {
            @Override
            public void onFinished(AirQualityData data) {
                swipeRefreshLayout.setRefreshing(false);
                if(data!=null&&data.status.equalsIgnoreCase("ok")){
                    tv_aqi.setText(data.airNowCity.aqi);
                    tv_pm25.setText(data.airNowCity.pm25);
                }else{
                    tv_aqi.setText("--");
                    tv_pm25.setText("--");
                }
            }
        });
    }
    private void updateWeatherForecast() {
        WeatherApiUtil.getWeatherForecast(this, weather_id, new WeatherApiUtil.OnWeatherForecastFinished() {
            @Override
            public void onFinished(WeatherForecast data) {
                updateRefreshState();
                if(data!=null){
                    forecastLayout.removeAllViews();
                    List<DailyForecast> forecastList = data.dailyForecastList;
                    for (int i = 0; i < forecastList.size(); i++) {
                        DailyForecast f = forecastList.get(i);
                        View v = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item, null, false);
                        TextView item_date_text=v.findViewById(R.id.item_date_text);
                        TextView item_max_text=v.findViewById(R.id.item_max_text);
                        TextView item_min_text=v.findViewById(R.id.item_min_text);
                        ImageView item_iv_day_con=v.findViewById(R.id.item_iv_day_con);
                        ImageView item_iv_night_con=v.findViewById(R.id.item_iv_night_con);
                        item_date_text.setText(f.date);
                        item_max_text.setText(f.tmp_max+"℃");
                        item_min_text.setText(f.tmp_min+"℃");
                        updateWeatherIcon(f.cond_code_d,item_iv_day_con);
                        updateWeatherIcon(f.cond_code_n,item_iv_night_con);
                        forecastLayout.addView(v); }
                }
            }
        });
    }




}
