package market.dental.model;

import org.json.JSONException;
import org.json.JSONObject;

import market.dental.util.Resource;

/**
 * Created by kemalsamikaraca on 20.01.2018.
 */

public class Store {

    private int id;
    private String name;
    private String phone;
    private String gsm;
    private String email;
    private String photo;

    public Store(JSONObject storeJsonObject){
        try {
            this.id = storeJsonObject.has("id")?
                    storeJsonObject.getInt("id") : -1 ;
            this.name = storeJsonObject.has("store_name")&& !storeJsonObject.isNull("store_name")?
                    storeJsonObject.getString("store_name"):"-";
            this.phone = storeJsonObject.has("phone")&& !storeJsonObject.isNull("phone") ?
                    storeJsonObject.getString("phone"):"-";
            this.gsm = storeJsonObject.has("mobile_phone")&& !storeJsonObject.isNull("mobile_phone") ?
                    storeJsonObject.getString("mobile_phone"):"-";
            this.email = storeJsonObject.has("email")?
                    storeJsonObject.getString("email"):"-";
            this.photo = storeJsonObject.has("photo")?
                    Resource.DOMAIN_NAME + "/" + storeJsonObject.getString("photo"):"";

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
