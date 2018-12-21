package market.dental.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import market.dental.adapter.OfferListAdapter;
import market.dental.model.Offer;
import market.dental.util.Resource;
import market.dental.util.Result;

public class OfferRequestListActivity extends AppCompatActivity {

    private OfferListAdapter offerListAdapter;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private boolean isLastPage;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_request_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialization
        requestQueue = Volley.newRequestQueue(this);
        offerListAdapter = new OfferListAdapter(this);
        offerListAdapter.setUserType(2);

        // Set UI
        findViewById(R.id.offer_status).setVisibility(View.GONE);
        findViewById(R.id.offer_count).setVisibility(View.GONE);
        findViewById(R.id.new_offer_create_button).setVisibility(View.GONE);

        // Get Offer Request List
        getOfferRequestList(-1);
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
    //                        GET OFFER REQUEST LIST
    // *********************************************************************************************
    private void getOfferRequestList(final int page){

        if(!isLoading && !isLastPage){
            isLoading = true;
            stringRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_get_offer_request_list, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {

                    try {
                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){

                            JSONObject content = response.getJSONObject("content");
                            if(content.getJSONArray("data").length()>0){

                                offerListAdapter.addOfferList(Offer.offerList(content.getJSONArray("data")));
                                offerListAdapter.setCurrentPage(content.getInt("current_page"));

                                ListView listView = findViewById(R.id.offer_list_main);
                                if(listView.getAdapter()==null)
                                    listView.setAdapter(offerListAdapter);
                                else{
                                    offerListAdapter.notifyDataSetChanged();
                                }

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Offer sOffer = ((Offer) parent.getItemAtPosition(position));
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("offerRequestId", sOffer.getId());
                                        bundle.putString("offerRequestName" , sOffer.getName() );
                                        bundle.putInt("offer_list_position" , position );
                                        Intent intent = new Intent(view.getContext(),OfferCreateActivity.class);
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent, 0000);
                                    }
                                });


                            } else{
                                isLastPage = false;
                            }

                        }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                            Resource.setDefaultAPITOKEN();
                            Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_offer_request_list + " >> responseString = " + responseString);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_get_offer_request_list );
                    } finally {
                        isLoading = false;
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Crashlytics.log(Log.ERROR , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + "onErrorResponse");
                    isLoading = false;
                }
            }){
                @Override
                protected Map<String, String> getParams()  {
                    Map<String, String> params = new HashMap<>();
                    params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                    params.put("page", String.valueOf(page));
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
