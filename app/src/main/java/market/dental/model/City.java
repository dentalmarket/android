package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kemalsamikaraca on 29.01.2018.
 */

public class City {

    private int id;
    private String name;

    public City(JSONObject cityJsonObject){

        try {
            this.id = cityJsonObject.has("id")?
                    cityJsonObject.getInt("id") : -1 ;
            this.name = cityJsonObject.has("il_adi") && !cityJsonObject.isNull("il_adi")?
                    cityJsonObject.getString("il_adi"):"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<City> getCityList(JSONArray cityJsonArray){

        List<City> cityList = new ArrayList<City>();
        for(int i=0; i<cityJsonArray.length(); i++){
            try {
                cityList.add(new City((JSONObject) cityJsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cityList;
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
}
