package market.dental.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.dental.adapter.OfferProductAddedListAdapter;
import market.dental.model.Category;
import market.dental.model.OfferProduct;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

public class OfferCreateActivity extends BaseActivity {

    private OfferProductAddedListAdapter addedProductListAdapter;

    private static final int PRODUCT_ADD_FOR_OFFER = 1001;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private boolean isLastPage;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialization
        requestQueue = Volley.newRequestQueue(this);
        addedProductListAdapter = new OfferProductAddedListAdapter(this);
        Intent intent = getIntent();

        // Get bundle info if exist
        int offerId = intent.getIntExtra("offerId" , -1);
        if(offerId!=-1){

            getOfferRequest(""+offerId);

            Switch isActive = findViewById(R.id.is_offer_active);
            isActive.setChecked(intent.getBooleanExtra("isOfferAcvite",false));

            EditText offerTitle = findViewById(R.id.fragment_offer_add_title_name);
            offerTitle.setText(intent.getStringExtra("offerName" ));

            Button offerUpdateButton = findViewById(R.id.offer_update_btn);
            offerUpdateButton.setText("Düzenle");

        }

        // ACTION LISTENER
        Button openOfferAddProduct = findViewById(R.id.open_offer_add_product_activity);
        openOfferAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OfferProductAddActivity.class);
                startActivityForResult(intent , PRODUCT_ADD_FOR_OFFER);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PRODUCT_ADD_FOR_OFFER) : {
                if (resultCode == Activity.RESULT_OK) {
                    Gson gson = new Gson();
                    OfferProduct selectedOfferProduct = gson.fromJson(data.getStringExtra("OFFERPRODUCT_GSON_STRING"), OfferProduct.class);

                    addedProductListAdapter.addProduct(selectedOfferProduct);
                    ListView listView = findViewById(R.id.fragment_offer_added_product_list);
                    if (listView.getAdapter() == null){
                        listView.setAdapter(addedProductListAdapter);
                    }else{
                        addedProductListAdapter.notifyDataSetChanged();
                    }

                }
                break;
            }
        }
    }


    private void getOfferRequest(final String offerId){

        // *****************************************************************************************
        //                        GET PRODUCTS LIST
        // *****************************************************************************************
        if(!isLoading && !isLastPage){
            isLoading = true;
            stringRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_get_offer_request, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {

                    try {
                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){
                            JSONObject content = response.getJSONObject("content");
                            if(content.has("products")) {
                                addedProductListAdapter.clearList();
                                addedProductListAdapter.addProductList(OfferProduct.offerProductList(content.getJSONArray("products")));
                                ListView listView = findViewById(R.id.fragment_offer_added_product_list);
                                if (listView.getAdapter() == null){
                                    listView.setAdapter(addedProductListAdapter);
                                }else{
                                    addedProductListAdapter.notifyDataSetChanged();
                                }
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
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_offer_request + " >> responseString = " + responseString);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_get_productsautocomplete );
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
                    params.put("id", offerId);
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
