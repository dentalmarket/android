package market.dental.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.dental.adapter.BoroughListAdapter;
import market.dental.adapter.CityListAdapter;
import market.dental.adapter.ProfessionListAdapter;
import market.dental.model.Borough;
import market.dental.model.City;
import market.dental.model.Profession;
import market.dental.util.Resource;
import market.dental.util.Result;

public class RegisterActivity extends BaseActivity {

    private List<Profession> professionList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private ProfessionListAdapter professionListAdapter = null;
    private CityListAdapter cityListAdapter = null;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private Context context;

    private AlertDialog progressDialog;
    private TextView boroughTextView;
    private TextView cityTextView;
    private TextView professionTextView;
    private TextView registerJob;
    private EditText registerPhone;
    private EditText registerMobilePhone;
    private boolean cityListRequestSuccess = false;
    private boolean professionListRequestSuccess = false;

    private City selectedCity = null;
    private Borough selectedBorough = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        requestQueue = Volley.newRequestQueue(context);

        AlertDialog.Builder progressDialogBuilder = new AlertDialog.Builder(this);
        progressDialogBuilder.setCancelable(false);
        progressDialogBuilder.setView(getLayoutInflater().inflate(R.layout.dialog_progressbar,null));
        progressDialog = progressDialogBuilder.create();

        // KULLANICI SADECE MAIL ADRESİ İLE KAYIT OLMASI İSTENDİ
/*
        professionListAdapter = new ProfessionListAdapter(context);
        cityListAdapter = new CityListAdapter(context);
        boroughTextView = findViewById(R.id.activity_register_borough);
        cityTextView = findViewById(R.id.activity_register_city);
        professionTextView = findViewById(R.id.activity_register_job);
        registerJob = (TextView)findViewById(R.id.activity_register_job);
        registerPhone = (EditText)findViewById(R.id.activity_register_phone);
        registerMobilePhone = (EditText)findViewById(R.id.activity_register_mobile_phone);

        progressDialog.show();

        // *****************************************************************************************
        //                          AJAX - GET CITY
        // *****************************************************************************************
        stringRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_city_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONArray content = response.getJSONArray("content");

                    cityList = City.getCityList(content);
                    cityListAdapter.setCityList(cityList);
                    citySetOnClickListener();
                    cityListRequestSuccess = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> 120");
                } finally {
                    closeProgressDialog(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> ERROR ON GET DATA >> 121");
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
        stringRequest.setTag(this.getClass().getName());
        requestQueue.add(requestCityList);


        // *****************************************************************************************
        //                          AJAX - GET PROFESSIONS
        // *****************************************************************************************
        stringRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_professions, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONArray content = response.getJSONArray("content");

                    professionList = Profession.ProfessionList(content);
                    professionListAdapter.setProfessionList(professionList);
                    professionSetOnClickListener();
                    professionListRequestSuccess = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> 122");

                } finally {
                    closeProgressDialog(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> ERROR ON GET DATA >> 123");
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
        stringRequest.setTag(this.getClass().getName());
        requestQueue.add(request);
*/

    }

    @Override
    public void onStop() {
        if (requestQueue != null) {
            requestQueue.cancelAll(this.getClass().getName());
        }
        super.onStop();
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

    public void closeProgressDialog(AlertDialog progressDialog){
        if(professionListRequestSuccess && cityListRequestSuccess){
            progressDialog.dismiss();
        }
    }


    public void getBoroughListWithAjax(final int cityId){

        // remove previous borough list
        boroughTextView.setText("");
        selectedBorough = null;

        progressDialog.show();
        // *****************************************************************************************
        //                          AJAX - GET PROFESSIONS
        // *****************************************************************************************
        stringRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_borough_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONArray content = response.getJSONArray("content");

                    List<Borough> boroughList = Borough.getBoroughList(content);
                    BoroughListAdapter boroughListAdapter = new BoroughListAdapter(context,boroughList);
                    boroughSetOnClickListener(boroughListAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> 124");
                } finally {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> ERROR ON GET DATA >> 125");
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                params.put("city", String.valueOf(cityId));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        stringRequest.setTag(this.getClass().getName());
        requestQueue.add(stringRequest);
    }


    public void boroughSetOnClickListener(final BoroughListAdapter boroughListAdapter){
        boroughTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
                mBuilder.setTitle("İlçe seçiniz");
                mBuilder.setAdapter(boroughListAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedBorough = (Borough) boroughListAdapter.getItem(which);
                        boroughTextView.setText(selectedBorough.getName());
                    }
                });
                mBuilder.show();
            }
        });
    }


    public void citySetOnClickListener(){
        cityTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
                mBuilder.setTitle(getString(R.string.choose_city));
                mBuilder.setAdapter(cityListAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCity = (City) cityListAdapter.getItem(which);
                        cityTextView.setText(selectedCity.getName());
                        getBoroughListWithAjax(selectedCity.getId());
                    }
                });
                mBuilder.show();
            }
        });
    }


    public void professionSetOnClickListener(){
        professionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
                mBuilder.setTitle(getString(R.string.choose_profession));
                mBuilder.setAdapter(professionListAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        professionTextView.setText(((Profession) professionListAdapter.getItem(which)).getName());
                    }
                });
                mBuilder.show();
            }
        });
    }

    /**
     *
     * activity_register_sign_up_button click event
     *
     * @param view
     */
    public void registerNewUser(final View view){

        final String password = ((TextInputEditText)findViewById(R.id.activity_register_password)).getText().toString();
        String passwordCheck = ((TextInputEditText)findViewById(R.id.activity_register_password_check)).getText().toString();
        if(password.length() < 6) {
            ((TextInputEditText) findViewById(R.id.activity_register_password)).setError("Şifreniz en az 6 karakterden oluşmalıdır");
        }else if(!password.equals(passwordCheck)){
            ((TextInputEditText) findViewById(R.id.activity_register_password_check)).setError("Şifreleriniz uyuşmamaktadır");
        }else{
            progressDialog.show();
            // *****************************************************************************************
            //                          AJAX - REGISTER NEW USER
            // *****************************************************************************************
            stringRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_register, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {
                    try {

                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){
                            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                            Notification notification = new Notification.Builder(view.getContext())
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle(getString(R.string.new_registration))
                                    .setContentText(getString(R.string.registration_process_success))
                                    .build();

                            notificationManager.notify(0,notification);
                            finish();
                        }else {
                            Toast.makeText(view.getContext() , response.getString("content"), Toast.LENGTH_LONG ).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass().getName() + " >> Exception >> " + Resource.ajax_register);
                    } finally {
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),this.getClass().getSimpleName() + " >> ERROR ON GET DATA >> " + Resource.ajax_register);
                    Toast.makeText(view.getContext() , getString(R.string.unexpected_network_error), Toast.LENGTH_LONG ).show();
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams()  {
                    Map<String, String> params = new HashMap<>();
                    params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                    params.put("type", "1");
                    params.put("email", ((EditText)findViewById(R.id.activity_register_email)).getText().toString());
                    params.put("password", password);
                    //String cityId = String.valueOf(selectedCity==null ? -1 : selectedCity.getId());
                    //String boroughId = String.valueOf(selectedBorough==null ? -1 : selectedBorough.getId());
                    //params.put("first_name", ((EditText)findViewById(R.id.activity_register_name)).getText().toString());
                    //params.put("last_name", ((EditText)findViewById(R.id.activity_register_lastname)).getText().toString());
                    //params.put("job", (registerJob).getText().toString());
                    //params.put("city", cityId);
                    //params.put("district", boroughId);
                    //params.put("phone", (registerPhone).getText().toString());
                    //params.put("mobile_phone", (registerMobilePhone).getText().toString());
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            stringRequest.setTag(this.getClass().getName());
            requestQueue.add(stringRequest);
        }
    }
}
