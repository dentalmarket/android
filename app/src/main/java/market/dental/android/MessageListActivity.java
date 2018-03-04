package market.dental.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.dental.adapter.MessageListAdapter;
import market.dental.model.Message;
import market.dental.model.User;
import market.dental.util.Resource;
import market.dental.util.Result;

public class MessageListActivity extends AppCompatActivity {

    private MessageListAdapter messageListAdapter;
    private RequestQueue requestQueue;
    private Context context;
    private SharedPreferences sharedPref = null;
    private RecyclerView messageRecycler;
    private RecyclerView.LayoutManager messageRecyclerLayoutManager;
    private Button sendMessage;
    private EditText messageText;
    private String receiverId;
    private int userId=-1;
    private Message newMessage;

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter(Resource.KEY_LOCAL_BROADCAST)
        );
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Karşıdaki kullanıcı broadcast değerindeki kişi ise mesaj direk eklenir
            if(receiverId.equals(intent.getExtras().getString("fromId"))){
                newMessage = new Message(intent.getExtras().getString("fromId"),intent.getExtras().getString("notification"));
                messageListAdapter.addItem(newMessage);
                messageListAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(context, "Farklı bir kişiden mesaj geldi" , Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialization
        context = this;
        requestQueue = Volley.newRequestQueue(this);
        messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        messageRecyclerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false);
        sharedPref = getSharedPreferences(getString(R.string.sp_dental_market), Context.MODE_PRIVATE);
        sendMessage = (Button) findViewById(R.id.activity_messagelist_send_message_btn);
        messageText = (EditText)findViewById(R.id.activity_messagelist_send_message_txt);

        try {
            JSONObject userJsonObject = new JSONObject(sharedPref.getString(getString(R.string.sp_user_json_str) , ""));
            User user = new User(userJsonObject);
            userId = user.getId();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        if(intent.hasExtra(Resource.KEY_MESSAGE_RECEIVER_ID) && intent.getStringExtra(Resource.KEY_MESSAGE_RECEIVER_ID).length()>0){
            receiverId = intent.getStringExtra(Resource.KEY_MESSAGE_RECEIVER_ID);
        }

        // *****************************************************************************************
        // *****************************************************************************************
        // https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
        // *****************************************************************************************
        // *****************************************************************************************

        getMessageList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    // *********************************************************************************************
    //                        AJAX - GET CONVERSATION LIST
    // *********************************************************************************************
    public void getMessageList(){

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_message_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {

                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONObject content = response.getJSONObject("content");

                    List<Message> messageList =  Message.MessageList(content.getJSONArray("messages"));
                    Collections.reverse(messageList);
                    messageListAdapter = new MessageListAdapter(context, messageList,userId);
                    messageRecycler.setLayoutManager(new LinearLayoutManager(context));
                    messageRecycler.setAdapter(messageListAdapter);
                    // scroll to end of the message
                    if(messageList.size() > 0){
                        messageRecycler.getLayoutManager().scrollToPosition(messageList.size()-1);
                    }

                    sendMessage.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            sendMessage();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"ConversationListActivity >> JSONException >> 120");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"ConversationListActivity >> ERROR ON GET DATA >> 121");
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Intent intent = getIntent();
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                if(intent.hasExtra(Resource.KEY_MESSAGE_RECEIVER_ID) && intent.getStringExtra(Resource.KEY_MESSAGE_RECEIVER_ID).length()>0){
                    params.put("receiverId", intent.getStringExtra(Resource.KEY_MESSAGE_RECEIVER_ID));
                }else if(intent.hasExtra(Resource.KEY_CONVERSATION_ID) && intent.getStringExtra(Resource.KEY_CONVERSATION_ID ).length()>0){
                    params.put("conversationId", intent.getStringExtra(Resource.KEY_CONVERSATION_ID ));
                }
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    // *********************************************************************************************
    //                        AJAX - SEND MESSAGE
    // *********************************************************************************************
    public void sendMessage(){
        final String message = messageText.getText().toString();
        messageText.setText("");
        if(message.length()>0){

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_send_message, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {

                    try {
                        // TODO: result objesinin kontrolü YAPILACAK
                        JSONObject response = new JSONObject(responseString);

                        newMessage = new Message(String.valueOf(userId),message);
                        sendMessage.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                messageListAdapter.addItem(newMessage);
                                messageListAdapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i(Result.LOG_TAG_INFO.getResultText(),"ConversationListActivity >> JSONException >> 120");
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"ConversationListActivity >> ERROR ON GET DATA >> 121");
                }
            }){
                @Override
                protected Map<String, String> getParams()  {
                    Map<String, String> params = new HashMap<>();
                    params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                    params.put("receiver_id", receiverId);
                    params.put("message", message);
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            requestQueue.add(jsonObjectRequest);
        }
    }
}
