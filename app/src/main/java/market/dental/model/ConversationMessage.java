package market.dental.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import market.dental.util.Resource;

/**
 * Created by kemalsamikaraca on 21.02.2018.
 */

public class ConversationMessage {

    private int guestUserId;
    private String guestUserName;
    private String guestUserPhotoURL;
    private List<Message> messageList;

    public ConversationMessage(JSONObject jsonObject){
        try {
            this.guestUserId = jsonObject.has("guest_id")?
                    jsonObject.getInt("guest_id") : -1 ;
            this.guestUserName = jsonObject.has("guest_name") && !jsonObject.isNull("guest_name")?
                    jsonObject.getString("guest_name"):"";
            this.guestUserPhotoURL = jsonObject.has("guest_photo")?
                    Resource.DOMAIN_NAME + "/" + jsonObject.getString("guest_photo"):"";

            this.messageList = Message.MessageList(jsonObject.getJSONArray("messages"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getGuestUserId() {
        return guestUserId;
    }

    public void setGuestUserId(int guestUserId) {
        this.guestUserId = guestUserId;
    }

    public String getGuestUserName() {
        return guestUserName;
    }

    public void setGuestUserName(String guestUserName) {
        this.guestUserName = guestUserName;
    }

    public String getGuestUserPhotoURL() {
        return guestUserPhotoURL;
    }

    public void setGuestUserPhotoURL(String guestUserPhotoURL) {
        this.guestUserPhotoURL = guestUserPhotoURL;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
