package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kemalsamikaraca on 28.01.2018.
 */

public class Profession {

    private int id;
    private String name;

    public Profession(JSONObject professionJsonObject){

        try {
            this.id = professionJsonObject.has("id") && !professionJsonObject.isNull("id") ?
                    professionJsonObject.getInt("id") : -1 ;
            this.name = professionJsonObject.has("name") && !professionJsonObject.isNull("name")?
                    professionJsonObject.getString("name"):"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Profession(String professionName){
        this.id = -1;
        this.name = professionName;
    }

    public static List<Profession> ProfessionList(JSONArray professionJsonArray){

        List<Profession> professionList = new ArrayList<Profession>();
        for(int i=0; i<professionJsonArray.length(); i++){
            try {
                professionList.add(new Profession((String) professionJsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return professionList;
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
