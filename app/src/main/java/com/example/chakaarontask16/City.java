package com.example.chakaarontask16;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class City {
    private int id;
    private String name;
    private int parentId=-1;
    private String enName="";
    private String initialName="";
    private String weather_id="";
    private int level=0;
    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public City() {

    }
    public City(int id, String name, int parentId) {
        this.id = id;
        this.name = name;
        this.parentId=parentId;
    }

    public String getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(String weather_id) {
        this.weather_id = weather_id;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getInitialName() {
        return initialName;
    }

    public void setInitialName(String initialName) {
        this.initialName = initialName;
    }

    @Override
    public String toString() {
        return String.format("%s(%s),id=%d",name,enName,id) ;
    }
   public static List<City> parseJsonCityByArray(String s)
    {
        List<City> list=new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                com.example.chakaarontask16.City city = new com.example.chakaarontask16.City(id, name);
                list.add(city);

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return  list;
    }

    public static List<City> json2CitiesByGson(String s){
        List<City> list=null;
        try {
            JSONArray jsonArray = new JSONArray(s);
            list=new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String s1 = jsonArray.get(i).toString();
                list.add(new Gson().fromJson(s1, com.example.chakaarontask16.City.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<City> json2CitiesByGsonV2(String s){
        List<City> list=null;
        if(!TextUtils.isEmpty(s)) {
            Type type = new TypeToken<List<City>>() {}.getType();
            list = new Gson().fromJson(s, type);
        }
        return list;
    }
}

