package market.dental.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.dental.adapter.ConversationListAdapter;
import market.dental.model.Conversation;
import market.dental.util.Resource;
import market.dental.util.Result;

public class ConversationListActivity extends AppCompatActivity {

    private ConversationListAdapter conversationListAdapter;
    private RequestQueue requestQueue;
    private TextView placeHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialization
        requestQueue = Volley.newRequestQueue(this);
        conversationListAdapter = new ConversationListAdapter(this);
        placeHolder = (TextView)findViewById(R.id.activity_conversation_list_empty);

        // Get List
        this.getConversationList();
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
    public void getConversationList(){

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_conversation_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {

                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONObject content = response.getJSONObject("content");

                    conversationListAdapter.setConversationList(Conversation.ConversationList(content.getJSONArray("conversations")));

                    if(conversationListAdapter.getCount() > 0){
                        placeHolder.setVisibility(View.GONE);
                        ListView listView = findViewById(R.id.activity_conversation_list_main);
                        listView.setAdapter(conversationListAdapter);
                        listView.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        int conversationId = ((Conversation) parent.getItemAtPosition(position)).getId();
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Resource.KEY_CONVERSATION_ID, String.valueOf(conversationId));
                                        Intent intent = new Intent(view.getContext(),MessageListActivity.class);
                                        intent.putExtras(bundle);
                                        view.getContext().startActivity(intent);
                                    }
                                }
                        );
                    }

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
