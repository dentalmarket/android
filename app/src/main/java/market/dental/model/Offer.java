package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Offer {

    private int id;
    private String name;
    private int numberOfOffers;
    private boolean isActive;
    private int userId;
    private Date created_at;

    public Offer(JSONObject projectJsonObject){
        try {
            this.id = projectJsonObject.has("id")?
                    projectJsonObject.getInt("id") : -1 ;
            this.name = projectJsonObject.has("name") && !projectJsonObject.isNull("name")?
                    projectJsonObject.getString("name"):"";
            this.numberOfOffers = projectJsonObject.has("number_of_offers")?
                    projectJsonObject.getInt("number_of_offers") : 0 ;
            this.isActive = projectJsonObject.has("is_active")?
                    projectJsonObject.getInt("is_active")==1 : false ;
            this.userId = projectJsonObject.has("user_id")?
                    projectJsonObject.getInt("user_id") : -1 ;

            if(projectJsonObject.has("created_at")){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                this.created_at = formatter.parse(projectJsonObject.getString("created_at"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Offer(String jsonString) throws JSONException {
        this(new JSONObject(jsonString) );
    }

    public static List<Offer> offerList(JSONArray projectJsonArray){
        List<Offer> offerList = new ArrayList<Offer>();
        for(int i=0; i<projectJsonArray.length(); i++){
            try {
                offerList.add(new Offer((JSONObject) projectJsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return offerList;
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

    public int getNumberOfOffers() {
        return numberOfOffers;
    }

    public void setNumberOfOffers(int numberOfOffers) {
        this.numberOfOffers = numberOfOffers;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
