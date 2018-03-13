package market.dental.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import market.dental.model.User;
import market.dental.util.Resource;
import market.dental.util.Result;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPref = null;
    private RequestQueue requestQueue;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialization
        context = this;
        sharedPref = getSharedPreferences(getString(R.string.sp_dental_market), Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        Resource.VALUE_API_TOKEN = sharedPref.getString(Resource.KEY_API_TOKEN , "");

        Thread myThread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(1500);

                    if(sharedPref.getString(Resource.KEY_API_TOKEN , "").length() == 0){ // API_TOKEN NULL
                        redirectLoginActivity();
                    }else if(sharedPref.getString(Resource.KEY_API_TOKEN , "").equals(Resource.STATIC_ANDROID_API_TOKEN)){ //static android API TOKEN ise
                        redirectLoginActivity();
                    }else{ // API_TOKEN ile device_token update edilmeye çalışılır
                        tryToSendDeviceTokenToServer(context);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();


    }

    protected void redirectLoginActivity(){
        Resource.setDefaultAPITOKEN();
        Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void redirectMainActivity(){
        Intent intent = new Intent(getApplicationContext() , MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void redirectConversationListActivity(Bundle bundle){
        Intent intent = new Intent(getApplicationContext() , MessageListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     *
     * Uygulama açılırken divece_token update edilmeye çalışılır. Eğer device_token update edilemez
     * ise bu durumunda login sayfasına yönlendirilir. Eğer device_token değeri update edilmiş ise
     * direk anasayfa yönlendirmesi sayılır
     *
     * @param context
     */
    protected void tryToSendDeviceTokenToServer(Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_update_device_token, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {

                try {
                    // CHECK TOKEN VALID OR NOT
                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){
                        JSONObject content = response.getJSONObject("content").getJSONObject("user");
                        User dentalUser = new User(content);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(Resource.KEY_API_TOKEN,Resource.VALUE_API_TOKEN);
                        editor.putString(getString(R.string.sp_user_json_str), content.toString());
                        editor.putInt(getString(R.string.sp_user_id), dentalUser.getId());
                        editor.commit();

                        // Eğer kullanıcı gelen mesaj bildirimine tıklıyorsa bildirimin içeriğine
                        // bakılarak gerekirse mesajlarım sayfasına yönlendirilir
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null && bundle.getString("fromId").length()>0) {
                            bundle.putString(Resource.KEY_MESSAGE_RECEIVER_ID, bundle.getString("fromId").toString());
                            redirectConversationListActivity(bundle);
                        }else{
                            redirectMainActivity();
                        }

                    // INVALID TOKEN
                    }else{
                        redirectLoginActivity();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"JSONException");
                    redirectLoginActivity();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText() , ">> Response.ErrorListener");
                Log.i(Result.LOG_TAG_INFO.getResultText() , ">> StausCode >> " + networkResponse.statusCode);
                redirectLoginActivity();
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                params.put("device_token", FirebaseInstanceId.getInstance().getToken());
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
