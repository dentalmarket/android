package market.dental.android;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

public class ProfileActivity extends BaseActivity {

    private SharedPreferences sharedPref = null;
    private RequestQueue requestQueue;
    private Context context;
    private AlertDialog progressDialog;
    private TextView professionTextView;
    private TextView cityEditText;
    private TextView boroughEditText;
    private TextView birthdayText;
    private TextView activity_profile_header_name;
    private TextView activity_profile_header_mail;
    private TextView activity_profile_header_rate;
    private EditText activity_profile_content_name;
    private EditText activity_profile_content_lastname;
    private EditText activity_profile_phone;
    private EditText activity_profile_mobile_phone;
    private EditText activity_profile_password;
    private EditText activity_profile_password_new;
    private EditText activity_profile_password_check;
    private List<Profession> professionList = new ArrayList<>();
    private ProfessionListAdapter professionListAdapter = null;
    private List<City> cityList = new ArrayList<>();
    private List<Borough> boroughList = new ArrayList<>();
    private CityListAdapter cityListAdapter = null;
    private boolean professionListRequestSuccess = false;
    private boolean cityListRequestSuccess = false;
    private City selectedCity = null;
    private Borough selectedBorough = null;
    private String localError = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        requestQueue = Volley.newRequestQueue(context);

        activity_profile_header_name = (TextView)findViewById(R.id.activity_profile_header_name);
        activity_profile_header_mail = (TextView)findViewById(R.id.activity_profile_header_mail);
        activity_profile_header_rate = (TextView)findViewById(R.id.activity_profile_header_rate);
        activity_profile_content_name = (EditText)findViewById(R.id.activity_profile_content_name);
        activity_profile_content_lastname = (EditText)findViewById(R.id.activity_profile_content_lastname);
        activity_profile_phone = (EditText)findViewById(R.id.activity_profile_phone);
        activity_profile_mobile_phone = (EditText)findViewById(R.id.activity_profile_mobile_phone);
        birthdayText = findViewById(R.id.activity_profile_birthday);
        professionTextView = (TextView)findViewById(R.id.activity_profile_profession);
        cityEditText = (TextView)findViewById(R.id.activity_profile_city_text);
        boroughEditText = (TextView)findViewById(R.id.activity_profile_borough_text);
        Button updateProfileButton = (Button) findViewById(R.id.activity_profile_save_btn);
        activity_profile_password = (EditText)findViewById(R.id.activity_profile_password);
        activity_profile_password_new = (EditText)findViewById(R.id.activity_profile_password_new);
        activity_profile_password_check = (EditText)findViewById(R.id.activity_profile_password_check);


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

                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){
                        professionList = Profession.ProfessionList(response.getJSONArray("content"));
                        professionListAdapter = new ProfessionListAdapter(context);
                        professionListAdapter.setProfessionList(professionList);
                        professionSetOnClickListener();
                    }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                        redirectLoginActivity();
                    }else {
                        Toast.makeText(context, "Meslek listesi getirilirken beklenilmeyen bir durum ile karşılaşıldı" , Toast.LENGTH_LONG).show();
                        Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_professions + " >> responseString = " + responseString);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Meslek listesi getirilirken beklenilmeyen bir hata ile karşılaşıldı" , Toast.LENGTH_LONG).show();
                    Crashlytics.log(Log.ERROR ,Result.LOG_TAG_INFO.getResultText(),this.getClass().getName() + " >> " + Resource.ajax_get_professions + " >> Exception");
                } finally {
                    professionListRequestSuccess = true;
                    closeProgressDialog(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                professionListRequestSuccess = true;
                closeProgressDialog(progressDialog);
                error.printStackTrace();

                StringBuilder errorMessage = new StringBuilder(this.getClass().getName() + " >> " + Resource.ajax_get_professions + " >> ERROR ON GET DATA ");
                volleyOnErrorResponse(error , errorMessage);
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

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> ajax_get_city_list");
                } finally {
                    cityListRequestSuccess = true;
                    closeProgressDialog(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cityListRequestSuccess = true;
                closeProgressDialog(progressDialog);
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
                        birthdayText.setText(dayOfMonth + "." + (month+1) + "." + year);
                    }
                } , year , month, day);

                mDatePicker.show();

            }
        });


        // *****************************************************************************************
        //                          CLICK EVENTS
        // *****************************************************************************************
        updateProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ajaxUpdateProfile();
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
            updateView();
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
                        selectedCity = (City) cityListAdapter.getItem(which);
                        cityEditText.setText( selectedCity.getName() );
                        getBoroughListWithAjax(selectedCity.getId(), null);
                    }
                });
                mBuilder.show();
            }
        });
    }


    public void getBoroughListWithAjax(final int cityId , final String userBoroughId){

        // remove previous borough list
        boroughEditText.setText("");
        selectedBorough = null;

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

                    boroughList = Borough.getBoroughList(content);
                    BoroughListAdapter boroughListAdapter = new BoroughListAdapter(context,boroughList);
                    boroughSetOnClickListener(boroughListAdapter);

                    if(userBoroughId!=null){
                        boroughLoop:
                        for(Borough borough:boroughList){
                            if(String.valueOf(borough.getId()).equals(userBoroughId)){
                                selectedBorough = borough;
                                boroughEditText.setText(selectedBorough.getName());
                                break boroughLoop;
                            }
                        }
                    }

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
                        selectedBorough = (Borough) boroughListAdapter.getItem(which);
                        boroughEditText.setText(selectedBorough.getName());
                    }
                });
                mBuilder.show();
            }
        });
    }


    // *********************************************************************************************
    //                          FUNCTION - UPDATE PROFILE
    // *********************************************************************************************
    public void ajaxUpdateProfile(){

        progressDialog.show();

        StringRequest requestUpdateProfile = new StringRequest(Request.Method.POST,
                Resource.ajax_profile_update, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {

                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){
                        JSONObject userJsonObject = response.getJSONObject("content").getJSONObject("user");
                        String responseMessage = response.getJSONObject("content").getString("message");

                        setUserSession(userJsonObject);
                        updateView();

                        if(localError.length()>0) {
                            Toast.makeText(context, localError, Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(context, "Beklenmeyen Hata", Toast.LENGTH_LONG).show();
                        Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> ajaxUpdateProfile");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> ajaxUpdateProfile");
                } finally {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> ERROR ON GET DATA >> ajaxUpdateProfile");

                NetworkResponse networkResponse = error.networkResponse;
                if(networkResponse!=null){
                    Log.i(Result.LOG_TAG_INFO.getResultText() , "" + this.getClass() + " >> Login.StausCode >> " + networkResponse.statusCode);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                localError = "";
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                params.put("first_name", activity_profile_content_name.getText().toString());
                params.put("last_name", activity_profile_content_lastname.getText().toString());
                params.put("phone", activity_profile_phone.getText().toString());
                params.put("mobile_phone", activity_profile_mobile_phone.getText().toString());
                params.put("job", professionTextView.getText().toString());
                params.put("birthday", birthdayText.getText().toString());

                if(selectedCity!=null)
                    params.put("city", String.valueOf(selectedCity.getId()));

                if(selectedBorough!=null)
                    params.put("district", String.valueOf(selectedBorough.getId()));

                // Şifre Değişikliği Durumu
                if(activity_profile_password_new.getText().toString().length() > 0 &&
                        activity_profile_password_check.getText().toString().length() > 0){
                    if(activity_profile_password_new.getText().toString().equals(
                            activity_profile_password_check.getText().toString())){
                        params.put("password", activity_profile_password.getText().toString());
                        params.put("new_password", activity_profile_password_new.getText().toString());
                    }else{
                        localError = "Yeni Şifrenizi Doğru Giriniz...";
                    }
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
        requestQueue.add(requestUpdateProfile);
    }


    /**
     * sp_user_id key değerine user session değeri yazılır
     */
    protected void setUserSession(JSONObject userJsonObject){
        User dentalUser = new User(userJsonObject);
        Resource.VALUE_API_TOKEN = dentalUser.getApi_token();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.sp_user_id), dentalUser.getId());
        editor.putString(getString(R.string.sp_user_json_str), userJsonObject.toString());
        editor.commit();
    }


    /**
     * activity_profile.xml data ile doldurulur
     */
    protected void updateView(){
        sharedPref = getSharedPreferences(getString(R.string.sp_dental_market), Context.MODE_PRIVATE);
        try {
            JSONObject userJsonObject = new JSONObject(sharedPref.getString(getString(R.string.sp_user_json_str) , ""));
            User user = new User(userJsonObject);
            activity_profile_header_name.setText(user.getFirst_name() + " " + user.getLast_name());
            activity_profile_header_mail.setText(user.getEmail());
            activity_profile_header_rate.setText(" ?? / 10");
            activity_profile_content_name.setText(user.getFirst_name());
            activity_profile_content_lastname.setText(user.getLast_name());
            activity_profile_phone.setText(user.getPhone());
            activity_profile_mobile_phone.setText(user.getMobile_phone());
            professionTextView.setText(user.getJob());
            birthdayText.setText(user.getBirthday());
            activity_profile_password.setText(null);
            activity_profile_password_new.setText(null);
            activity_profile_password_check.setText(null);

            cityLoop:
            for(City city:cityList){
                if(String.valueOf(city.getId()).equals(user.getCity_id())){
                    selectedCity = city;
                    cityEditText.setText(selectedCity.getName());
                    getBoroughListWithAjax(selectedCity.getId() , user.getBorough_id());
                    break cityLoop;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(Result.LOG_TAG_INFO.getResultText(),"" + this.getClass() + " >> JSONException >> updateView");
        }
    }
}
