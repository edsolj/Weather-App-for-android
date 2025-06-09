package utility;

import com.example.chakaarontask16.City;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    public static List<City> getCityListFromJson(String json, int parentId, int level){
        ArrayList<City> list=null;
        try {
            list=new ArrayList<>();
            JSONArray jsonArray=new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                String s1 = jsonArray.get(i).toString();
                City city = new Gson().fromJson(s1, City.class);
                String name = city.getName();
                String enName = PinyinUtils.toPinyin(name);
                String initialName = PinyinUtils.toPinyinFirstLetter(name);
                city.setParentId(parentId);
                city.setEnName(enName);
                city.setInitialName(initialName);
                city.setLevel(level);
                list.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
