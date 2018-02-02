package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kemalsamikaraca on 29.01.2018.
 */

public class Borough {

    private int id;
    private String name;

    public Borough(JSONObject boroughJsonObject){

        try {
            this.id = boroughJsonObject.has("id")?
                    boroughJsonObject.getInt("id") : -1 ;
            this.name = boroughJsonObject.has("ilce_adi") && !boroughJsonObject.isNull("ilce_adi")?
                    boroughJsonObject.getString("ilce_adi"):"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Borough> getBoroughList(JSONArray boroughJsonArray){

        List<Borough> boroughList = new ArrayList<Borough>();
        for(int i=0; i<boroughJsonArray.length(); i++){
            try {
                boroughList.add(new Borough((JSONObject) boroughJsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return boroughList;
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
