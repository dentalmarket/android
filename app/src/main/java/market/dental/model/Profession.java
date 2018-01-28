package market.dental.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kemalsamikaraca on 28.01.2018.
 */

public class Profession {

    private int id;
    private String name;

    public Profession(JSONObject professionJsonObject){

        try {
            this.id = professionJsonObject.has("id")?
                    professionJsonObject.getInt("id") : -1 ;
            this.name = professionJsonObject.has("name") && !professionJsonObject.isNull("name")?
                    professionJsonObject.getString("name"):"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
