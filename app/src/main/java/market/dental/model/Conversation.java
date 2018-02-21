package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import market.dental.util.Resource;

/**
 * Created by kemalsamikaraca on 21.02.2018.
 */

public class Conversation {

    private int id;
    private int userId;
    private String nameSurname;
    private String lastMessageDate;
    private String message;
    private String userPhoto;

    public Conversation(JSONObject conversationJsonObject){
        try {
            this.id = conversationJsonObject.has("id")?
                    conversationJsonObject.getInt("id") : -1 ;
            this.userId = conversationJsonObject.has("user_id")?
                    conversationJsonObject.getInt("user_id"):-1;
            this.nameSurname = conversationJsonObject.has("name_surname") && !conversationJsonObject.isNull("name_surname")?
                    conversationJsonObject.getString("name_surname"):"";
            this.lastMessageDate = conversationJsonObject.has("last_message") && !conversationJsonObject.isNull("last_message")?
                    conversationJsonObject.getString("last_message"):"";
            this.message = conversationJsonObject.has("message") && !conversationJsonObject.isNull("message")?
                    conversationJsonObject.getString("message"):"";
            this.userPhoto = conversationJsonObject.has("user_photo") && !conversationJsonObject.isNull("user_photo")?
                    Resource.DOMAIN_NAME + "/" + conversationJsonObject.getString("user_photo"):"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static List<Conversation> ConversationList(JSONArray conversationJsonArray){

        List<Conversation> conversationList = new ArrayList<Conversation>();
        for(int i=0; i<conversationJsonArray.length(); i++){
            try {
                conversationList.add(new Conversation((JSONObject) conversationJsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return conversationList;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
