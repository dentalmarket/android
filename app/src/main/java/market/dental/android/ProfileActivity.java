package market.dental.android;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.dental.adapter.BoroughListAdapter;
import market.dental.adapter.CityListAdapter;
import market.dental.adapter.ProfessionListAdapter;
import market.dental.model.Borough;
import market.dental.model.City;
import market.dental.model.Profession;
import market.dental.model.User;
import market.dental.util.Resource;
import market.dental.util.Result;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences sharedPref = null;
    private RequestQueue requestQueue;
    private Context context;
    private AlertDialog progressDialog;
    private TextView professionTextView;
    private TextView cityEditText;
    private TextView boroughEditText;

    private List<Profession> professionList = new ArrayList<>();
    private ProfessionListAdapter professionListAdapter = null;
    private List<City> cityList = new ArrayList<>();
    private CityListAdapter cityListAdapter = null;
    private boolean professionListRequestSuccess = false;
    private boolean cityListRequestSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        requestQueue = Volley.newRequestQueue(context);

        TextView activity_profile_header_name = (TextView)findViewById(R.id.activity_profile_header_name);
        TextView activity_profile_header_mail = (TextView)findViewById(R.id.activity_profile_header_mail);
        TextView activity_profile_header_rate = (TextView)findViewById(R.id.activity_profile_header_rate);
        EditText activity_profile_content_name = (EditText)findViewById(R.id.activity_profile_content_name);
        EditText activity_profile_content_lastname = (EditText)findViewById(R.id.activity_profile_content_lastname);
        professionTextView = (TextView)findViewById(R.id.activity_profile_profession);
        cityEditText = (TextView)findViewById(R.id.activity_profile_city_text);
        boroughEditText = (TextView)findViewById(R.id.activity_profile_borough_text);


        // INITIALIZATION
        sharedPref = getSharedPreferences(getString(R.string.sp_dental_market), Context.MODE_PRIVATE);
        try {
            JSONObject userJsonObject = new JSONObject(sharedPref.getString(getString(R.string.sp_user_json_str) , ""));
            User user = new User(userJsonObject);
            activity_profile_header_name.setText(user.getFirst_name() + " " + user.getLast_name());
            activity_profile_header_mail.setText(user.getEmail());
            activity_profile_header_rate.setText(" ?? / 10");
            activity_profile_content_name.setText(user.getFirst_name());
            activity_profile_content_lastname.setText(user.getLast_name());
            professionTextView.setText(user.getJob());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AlertDialog.Builder progressDialogBuilder = new AlertDialog.Builder(this);
        progressDialogBuilder.setCancelable(false);
        progressDialogBuilder.setView(getLayoutInflater().inflate(R.layout.dialog_progressbar,null));
        progressDialog = progressDialogBuilder.create();
        progressDialog.show();


        // *****************************************************************************************
        //                          AJAX - GET PROFESSIONS
        // *****************************************************************************************
        StringRequest request = new StringRequest(Request.Method.POST,
                Resource.ajax_get_professions, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONArray content = response.getJSONArray("content");

                    professionList = Profession.ProfessionList(content);
                    professionListAdapter = new ProfessionListAdapter(context);
                    professionListAdapter.setProfessionList(professionList);
                    professionSetOnClickListener();
                    professionListRequestSuccess = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> ajax_get_professions");

                } finally {
                    closeProgressDialog(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> ERROR ON GET DATA >> ajax_get_professions");
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
        requestQueue.add(request);


        // *****************************************************************************************
        //                          AJAX - GET CITY
        // *****************************************************************************************
        StringRequest requestCityList = new StringRequest(Request.Method.POST,
                Resource.ajax_get_city_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONArray content = response.getJSONArray("content");

                    cityList = City.getCityList(content);
                    cityListAdapter = new CityListAdapter(context);
                    cityListAdapter.setCityList(cityList);
                    citySetOnClickListener();
                    cityListRequestSuccess = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> ajax_get_city_list");
                } finally {
                    closeProgressDialog(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> ERROR ON GET DATA >> ajax_get_city_list");
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
        requestQueue.add(requestCityList);


        // *****************************************************************************************
        //                          AJAX - DATEPICKER
        // *****************************************************************************************
        TextView birthdayText = findViewById(R.id.activity_profile_birthday);
        birthdayText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(ProfileActivity.this , R.style.DialogTheme , new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.i(Result.LOG_TAG_INFO.getResultText() , "DENEME datepicker");
                    }
                } , year , month, day);

                mDatePicker.show();
            }
        });
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

    public void professionSetOnClickListener(){
        professionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivity.this);
                mBuilder.setTitle("Mesleğinizi seçiniz");

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

    public void citySetOnClickListener(){
        cityEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivity.this);
                mBuilder.setTitle("Şehir seçiniz");
                mBuilder.setAdapter(cityListAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cityEditText.setText( ((City) cityListAdapter.getItem(which)).getName() );
                        getBoroughListWithAjax(((City) cityListAdapter.getItem(which)).getId());
                    }
                });
                mBuilder.show();
            }
        });
    }


    public void getBoroughListWithAjax(final int cityId){
        progressDialog.show();
        // *****************************************************************************************
        //                          AJAX - GET PROFESSIONS
        // *****************************************************************************************
        StringRequest request = new StringRequest(Request.Method.POST,
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
        requestQueue.add(request);
    }


    public void boroughSetOnClickListener(final BoroughListAdapter boroughListAdapter){
        boroughEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivity.this);
                mBuilder.setTitle("İlçe seçiniz");
                mBuilder.setAdapter(boroughListAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boroughEditText.setText(((Borough) boroughListAdapter.getItem(which)).getName());
                    }
                });
                mBuilder.show();
            }
        });
    }

}
