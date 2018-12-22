package market.dental.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import market.dental.adapter.OfferProductAddedListAdapter;
import market.dental.dialog.OfferDialog;
import market.dental.model.OfferProduct;
import market.dental.model.User;
import market.dental.util.Resource;
import market.dental.util.Result;

public class OfferCreateActivity extends BaseActivity implements OfferDialog.OfferDialogListener {

    private OfferProductAddedListAdapter addedProductListAdapter;

    private static final int PRODUCT_ADD_FOR_OFFER = 1001;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private boolean isLastPage;
    private boolean isLoading = false;
    private int offerId = -1;
    private int offerPositionInPrevActivity = -1;
    private int offerRequestType=0;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialization
        requestQueue = Volley.newRequestQueue(this);
        addedProductListAdapter = new OfferProductAddedListAdapter(this);
        view = findViewById(android.R.id.content);
        Intent intent = getIntent();

        // TODO: Bu tasarımı sorulacak. İstenilmesi durumunda buna geçilecek
        // BUTTON ACTION LISTENER
        /*
        Button openOfferAddProduct = findViewById(R.id.open_offer_add_product_activity);
        openOfferAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OfferProductAddActivity.class);
                startActivityForResult(intent , PRODUCT_ADD_FOR_OFFER);
            }
        });
        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OfferProductAddActivity.class);
                startActivityForResult(intent , PRODUCT_ADD_FOR_OFFER);
            }
        });

        Button offerRequestMain = findViewById(R.id.offer_update_btn);
        offerRequestMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offerRequestType==1) {
                    setOfferRequest("" + offerId);
                }else if(offerRequestType==2){
                    openOfferDialog();
                }else{
                    addOfferRequest();
                }
            }
        });

        // Get bundle info if exist
        offerPositionInPrevActivity = intent.getIntExtra("offer_list_position",-1);
        if(intent.getIntExtra("offerId" , -1)!=-1){
            offerRequestType = 1;
            offerId = intent.getIntExtra("offerId" , -1);
            getOfferRequest(""+offerId);

            Switch isActive = findViewById(R.id.is_offer_active);
            isActive.setChecked(intent.getBooleanExtra("isOfferAcvite",false));

            EditText offerTitle = findViewById(R.id.fragment_offer_add_title_name);
            offerTitle.setText(intent.getStringExtra("offerName" ));

            Button offerUpdateButton = findViewById(R.id.offer_update_btn);
            offerUpdateButton.setText("Düzenle");
        }else if(intent.getIntExtra("offerRequestId" , -1)!=-1){
            offerRequestType = 2;
            offerId = intent.getIntExtra("offerRequestId" , -1);
            getOfferRequest(""+intent.getIntExtra("offerRequestId" , -1));

            // Ürün ekleme butonu kaldırılır
            EditText offerTitle = findViewById(R.id.fragment_offer_add_title_name);
            offerTitle.setText(intent.getStringExtra("offerRequestName" ));
            offerTitle.setEnabled(false);

            // Ürün ekleme butonu kaldırılır
            findViewById(R.id.open_offer_add_product_activity).setVisibility(View.GONE);
            findViewById(R.id.is_offer_active_layout).setVisibility(View.GONE);

            Button offerUpdateButton = findViewById(R.id.offer_update_btn);
            offerUpdateButton.setText("Teklif Ver");
        }

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

    @Override
    public void applyOfferDialogValues(final String desc, final int price, final int currency) {

        if(!isLoading && !isLastPage){
            isLoading = true;
            stringRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_set_offer, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {
                    try {

                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){
                            finish();
                        }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                            Resource.setDefaultAPITOKEN();
                            Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_set_offer + " >> responseString = " + responseString);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_set_offer );
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
                    params.put("request_id", ""+offerId);
                    params.put("description", ""+desc);
                    params.put("price", ""+price);
                    params.put("currency", ""+currency);
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

    private void openOfferDialog(){
        OfferDialog offerDialog = new OfferDialog();
        offerDialog.show(getSupportFragmentManager(), "offer dialog");
    }


    private void getOfferRequest(final String offerId){

        // *****************************************************************************************
        //                        GET OFFER REQUEST
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
                            }

                            if(content.has("user") && content.getJSONArray("user").length()>0){
                                User offerRequestOwner = new User((JSONObject) content.getJSONArray("user").get(0));
                                findViewById(R.id.offer_request_owner_layout).setVisibility(View.VISIBLE);
                                ((EditText)findViewById(R.id.offer_request_owner_fullname_name))
                                        .setText(offerRequestOwner.getFirst_name() + " " + offerRequestOwner.getLast_name());
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


    private void setOfferRequest(final String offerId){

        // *****************************************************************************************
        //                        SET OFFER REQUEST
        // *****************************************************************************************
        if(!isLoading && !isLastPage){
            isLoading = true;
            stringRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_set_offer_request_with_id, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {
                    try {
                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("OFFER_REQUEST_UPDATE_JSON_STR", response.getJSONObject("content").toString());
                            resultIntent.putExtra("OFFER_REQUEST_POSITION", offerPositionInPrevActivity);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                            Resource.setDefaultAPITOKEN();
                            Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_set_offer_request_with_id + " >> responseString = " + responseString);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_set_offer_request_with_id );
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
                    params.put("requestId", offerId);
                    params.put("requestName", ((EditText)findViewById(R.id.fragment_offer_add_title_name)).getText().toString());
                    params.put("isActive", ((Switch)findViewById(R.id.is_offer_active)).isChecked() ? "1" : "0");

                    int i=0;
                    for(OfferProduct offerProduct : addedProductListAdapter.getAddedProductList()){
                        params.put("productId["+i+"]", ""+offerProduct.getProductId());
                        params.put("productTitle["+i+"]", offerProduct.getProductTitle());
                        params.put("unit["+i+"]", ""+offerProduct.getUnit());
                        params.put("description["+i+"]", offerProduct.getDescription());
                        i++;
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
            stringRequest.setTag(this.getClass().getName());
            requestQueue.add(stringRequest);
        }

    }

    private void addOfferRequest(){

        // *****************************************************************************************
        //                        ADD OFFER REQUEST
        // *****************************************************************************************
        if(!isLoading && !isLastPage){
            isLoading = true;
            stringRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_set_offer_request, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {
                    try {
                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("OFFER_REQUEST_CREATE_JSON_STR", response.getJSONObject("content").toString());
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                            Resource.setDefaultAPITOKEN();
                            Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_set_offer_request + " >> responseString = " + responseString);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_set_offer_request );
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
                    params.put("requestName", ((EditText)findViewById(R.id.fragment_offer_add_title_name)).getText().toString());

                    int i=0;
                    for(OfferProduct offerProduct : addedProductListAdapter.getAddedProductList()){
                        params.put("productId["+i+"]", ""+offerProduct.getProductId());
                        params.put("productTitle["+i+"]", offerProduct.getProductTitle());
                        params.put("unit["+i+"]", ""+offerProduct.getUnit());
                        params.put("description["+i+"]", offerProduct.getDescription());
                        i++;
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
            stringRequest.setTag(this.getClass().getName());
            requestQueue.add(stringRequest);
        }

    }

}
