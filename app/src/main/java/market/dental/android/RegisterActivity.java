package market.dental.android;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

import market.dental.adapter.BoroughListAdapter;
import market.dental.adapter.CityListAdapter;
import market.dental.adapter.ProfessionListAdapter;
import market.dental.model.Borough;
import market.dental.model.Category;
import market.dental.model.City;
import market.dental.model.Profession;
import market.dental.util.Resource;
import market.dental.util.Result;

public class RegisterActivity extends AppCompatActivity {

    private List<Profession> professionList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private ProfessionListAdapter professionListAdapter = null;
    private CityListAdapter cityListAdapter = null;
    private RequestQueue requestQueue;
    private Context context;

    private AlertDialog progressDialog;
    private EditText boroughEditText;
    private EditText cityEditText;
    private EditText professionTextView;
    private boolean cityListRequestSuccess = false;
    private boolean professionListRequestSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        requestQueue = Volley.newRequestQueue(context);
        professionListAdapter = new ProfessionListAdapter(context);
        cityListAdapter = new CityListAdapter(context);

        boroughEditText = findViewById(R.id.activity_register_borough);
        cityEditText = findViewById(R.id.activity_register_city);
        professionTextView = findViewById(R.id.activity_register_job);


        AlertDialog.Builder progressDialogBuilder = new AlertDialog.Builder(this);
        progressDialogBuilder.setCancelable(false);
        progressDialogBuilder.setView(getLayoutInflater().inflate(R.layout.dialog_progressbar,null));
        progressDialog = progressDialogBuilder.create();
        progressDialog.show();

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
                    JSONObject content = response.getJSONObject("content");

                    cityList = City.getCityList(content.getJSONArray("cities"));
                    cityListAdapter.setCityList(cityList);
                    citySetOnFocusChangeListener();
                    cityListRequestSuccess = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"RegisterActivity >> JSONException >> 120");
                } finally {
                    closeProgressDialog(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"RegisterActivity >> ERROR ON GET DATA >> 121");
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
        //                          AJAX - GET PROFESSIONS
        // *****************************************************************************************
        StringRequest request = new StringRequest(Request.Method.POST,
                Resource.ajax_get_professions, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONObject content = response.getJSONObject("content");

                    professionList = Profession.ProfessionList(content.getJSONArray("professions"));
                    professionListAdapter.setProfessionList(professionList);
                    professionSetOnFocusChangeListener();
                    professionListRequestSuccess = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"RegisterActivity >> JSONException >> 122");

                } finally {
                    closeProgressDialog(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"RegisterActivity >> ERROR ON GET DATA >> 123");
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


    public void getBoroughListWithAjax(){
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
                    JSONObject content = response.getJSONObject("content");

                    List<Borough> boroughList = Borough.getBoroughList(content.getJSONArray("boroughs"));
                    BoroughListAdapter boroughListAdapter = new BoroughListAdapter(context,boroughList);
                    boroughSetOnFocusChangeListener(boroughListAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"RegisterActivity >> JSONException >> 124");
                } finally {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"RegisterActivity >> ERROR ON GET DATA >> 125");
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
    }


    public void boroughSetOnFocusChangeListener(final BoroughListAdapter boroughListAdapter){
        boroughEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus){
                if(hasFocus) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    mBuilder.setTitle("İlçe seçiniz");
                    mBuilder.setAdapter(boroughListAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boroughEditText.setText(((Borough) boroughListAdapter.getItem(which)).getName());
                        }
                    });
                    mBuilder.show();
                }
            }
        });
    }


    public void citySetOnFocusChangeListener(){
        cityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus){
                if(hasFocus) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    mBuilder.setTitle("Şehir seçiniz");
                    mBuilder.setAdapter(cityListAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cityEditText.setText( ((City) cityListAdapter.getItem(which)).getName() );
                            getBoroughListWithAjax();
                        }
                    });
                    mBuilder.show();
                }
            }
        });
    }


    public void professionSetOnFocusChangeListener(){
        professionTextView.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus){
                if(hasFocus) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    mBuilder.setTitle("Mesleğinizi seçiniz");

                    mBuilder.setAdapter(professionListAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            professionTextView.setText(((Profession) professionListAdapter.getItem(which)).getName());
                        }
                    });
                    mBuilder.show();
                }
            }
        });
    }
}
