package market.dental.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import market.dental.util.Resource;
import market.dental.util.Result;

public class ForgotPasswordActivity extends BaseActivity {

    private RequestQueue requestQueue;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);

        AlertDialog.Builder progressDialogBuilder = new AlertDialog.Builder(this);
        progressDialogBuilder.setCancelable(false);
        progressDialogBuilder.setView(getLayoutInflater().inflate(R.layout.dialog_progressbar,null));
        progressDialog = progressDialogBuilder.create();
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


    public void forgotPasswordAction(final View view){

        progressDialog.show();
        // *****************************************************************************************
        //                          AJAX - FORGOT PASSWORD
        // *****************************************************************************************
        StringRequest request = new StringRequest(Request.Method.POST,
                Resource.ajax_forgot_password, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {

                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){
                        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        Notification notification = new Notification.Builder(view.getContext())
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(getString(R.string.forgot_password))
                                .setContentText(getString(R.string.password_refresh_link_sent_to_mail))
                                .build();

                        notificationManager.notify(0,notification);
                        finish();
                    }else{
                        Toast.makeText(view.getContext() , response.getString("content"), Toast.LENGTH_LONG ).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> 121");
                } finally {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass().getName() + " >> ERROR ON GET DATA >> 122");
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                params.put("email", ((EditText)findViewById(R.id.activity_forgot_password_email)).getText().toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(request);

    }
}
