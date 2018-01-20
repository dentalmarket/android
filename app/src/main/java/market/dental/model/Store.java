package market.dental.model;

import org.json.JSONException;
import org.json.JSONObject;

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
            this.name = storeJsonObject.has("name")?
                    storeJsonObject.getString("name"):"";
            this.phone = storeJsonObject.has("phone")?
                    storeJsonObject.getString("phone"):"";
            this.gsm = storeJsonObject.has("gsm")?
                    storeJsonObject.getString("gsm"):"";
            this.email = storeJsonObject.has("email")?
                    storeJsonObject.getString("email"):"";
            this.photo = storeJsonObject.has("photo")?
                    storeJsonObject.getString("photo"):"";

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
