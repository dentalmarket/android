package market.dental.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kemalsamikaraca on 18.01.2018.
 */

public class Brand {

    private int id;
    private String name;
    private String logo;

    public Brand(JSONObject brandJsonObject){

        try {
            this.id = brandJsonObject.getInt("id");
            this.name = brandJsonObject.getString("name");
            this.logo = brandJsonObject.getString("logo");
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
