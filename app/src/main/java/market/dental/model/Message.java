package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import market.dental.util.Resource;

/**
 * Created by kemalsamikaraca on 21.02.2018.
 */

public class Message {

    private int id;
    private String conversationId;
    private String userId;
    private String message;
    private String createdDate;
    private String updatedDate;
    private String deletedDate;

    public Message (String userId , String message){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.createdDate = df.format(new Date());
        this.updatedDate = df.format(new Date());
        this.message = message;
        this.userId = userId;
    }

    public Message(JSONObject messageJsonObject){
        try {
            this.id = messageJsonObject.has("id")?
                    messageJsonObject.getInt("id") : -1 ;
            this.conversationId = messageJsonObject.has("conversation_id") && !messageJsonObject.isNull("conversation_id")?
                    messageJsonObject.getString("conversation_id"):"";
            this.userId = messageJsonObject.has("user_id") && !messageJsonObject.isNull("user_id")?
                    messageJsonObject.getString("user_id"):"";
            this.message = messageJsonObject.has("message") && !messageJsonObject.isNull("message")?
                    messageJsonObject.getString("message"):"";
            this.createdDate = messageJsonObject.has("created_at") && !messageJsonObject.isNull("created_at")?
                    messageJsonObject.getString("created_at"):"";
            this.updatedDate = messageJsonObject.has("updated_at") && !messageJsonObject.isNull("updated_at")?
                    messageJsonObject.getString("updated_at"):"";
            this.deletedDate = messageJsonObject.has("deleted_at") && !messageJsonObject.isNull("deleted_at")?
                    messageJsonObject.getString("deleted_at"):"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Message> MessageList(JSONArray messageJsonArray){

        List<Message> messageList = new ArrayList<Message>();
        for(int i=0; i<messageJsonArray.length(); i++){
            try {
                messageList.add(new Message((JSONObject) messageJsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return messageList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }
}
