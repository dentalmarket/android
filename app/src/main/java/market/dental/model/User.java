package market.dental.model;

import org.json.JSONException;
import org.json.JSONObject;

import market.dental.util.Resource;

/**
 * Created by kemalsamikaraca on 28.01.2018.
 */

public class User {

    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String phone;
    private String mobile_phone;
    private String birthday;
    private String job;
    private String api_token;
    private String city_id;
    private String borough_id;

    public User(JSONObject userJsonObject){

        try {
            this.id = userJsonObject.has("id")?
                    userJsonObject.getInt("id") : -1 ;
            this.email = userJsonObject.has("email") && !userJsonObject.isNull("email")?
                    userJsonObject.getString("email"):"";
            this.first_name = userJsonObject.has("first_name") && !userJsonObject.isNull("first_name")?
                    userJsonObject.getString("first_name"):"";
            this.last_name = userJsonObject.has("last_name") && !userJsonObject.isNull("last_name")?
                    userJsonObject.getString("last_name"):"";
            this.phone = userJsonObject.has("phone") && !userJsonObject.isNull("phone")?
                    userJsonObject.getString("phone"):"";
            this.mobile_phone = userJsonObject.has("mobile_phone") && !userJsonObject.isNull("mobile_phone")?
                    userJsonObject.getString("mobile_phone"):"";
            this.birthday = userJsonObject.has("birthday") && !userJsonObject.isNull("birthday")?
                    userJsonObject.getString("birthday"):"";
            this.job = userJsonObject.has("job") && !userJsonObject.isNull("job")?
                    userJsonObject.getString("job"):"";
            this.api_token = userJsonObject.has("api_token") && !userJsonObject.isNull("api_token")?
                    userJsonObject.getString("api_token"):"";
            this.city_id = userJsonObject.has("city") && !userJsonObject.isNull("city")?
                    userJsonObject.getString("city"):"";
            this.borough_id = userJsonObject.has("district") && !userJsonObject.isNull("district")?
                    userJsonObject.getString("district"):"";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getBorough_id() {
        return borough_id;
    }

    public void setBorough_id(String borough_id) {
        this.borough_id = borough_id;
    }
}
