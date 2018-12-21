package market.dental.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Map;

import market.dental.adapter.OfferSearchProductListAdapter;
import market.dental.model.OfferProduct;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

public class OfferProductAddActivity extends AppCompatActivity {

    private OfferSearchProductListAdapter productAutoCompListAdapter;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private boolean isLastPage;
    private boolean isLoading = false;
    private AutoCompleteTextView acTextView;
    private Product selectedProduct=null;
    private String prevSearchKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_product_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialization
        requestQueue = Volley.newRequestQueue(this);
        productAutoCompListAdapter = new OfferSearchProductListAdapter(this);

        // Set AutoCompleteTextView
        acTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_product_for_offer);
        acTextView.setThreshold(1);
        acTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevSearchKey = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getProductsAutoCompleteList(-1);
            }
        });

        // Action Listener
        Button b = findViewById(R.id.offer_add_product_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                if(selectedProduct!=null){
                    Gson gson = new Gson();
                    OfferProduct selectedOfferProduct = new OfferProduct();
                    selectedOfferProduct.setImageUrl(selectedProduct.getImageUrl());
                    selectedOfferProduct.setProductTitle(selectedProduct.getName());
                    selectedOfferProduct.setDescription(((EditText)findViewById(R.id.selected_product_desc_for_offer)).getText().toString());
                    selectedOfferProduct.setUnit(Integer.parseInt(((EditText)findViewById(R.id.selected_product_count_for_offer)).getText().toString()));
                    resultIntent.putExtra("OFFERPRODUCT_GSON_STRING", gson.toJson(selectedOfferProduct));
                    setResult(Activity.RESULT_OK, resultIntent);
                }
                finish();
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

    private void getProductsAutoCompleteList(final int page){

        // *****************************************************************************************
        //                        GET PRODUCTS LIST
        // *****************************************************************************************
        if(!isLoading && !isLastPage){
            isLoading = true;
            stringRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_get_productsautocomplete, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {

                    try {

                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){

                            if(response.getJSONArray("content").length()>0){

                                productAutoCompListAdapter.clearProductList();
                                productAutoCompListAdapter.addProductList(Product.ProductList(response.getJSONArray("content")));
                                acTextView.setAdapter(productAutoCompListAdapter);
                                acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                                        acTextView.setText("");
                                        // Ürün bilgileri eklenir
                                        selectedProduct = (Product)parent.getItemAtPosition(position);
                                        ((TextView)findViewById(R.id.selected_product_name_for_offer)).setText(selectedProduct.getName());
                                        ((TextView)findViewById(R.id.selected_product_subtitle_for_offer)).setText(selectedProduct.getSubtitle());

                                        Picasso.with(getApplicationContext())
                                                .load(selectedProduct.getImageUrl())
                                                .placeholder(R.mipmap.ic_launcher)
                                                .error(R.mipmap.ic_launcher)
                                                .resize(120, 100)
                                                .into((ImageView) findViewById(R.id.selected_product_image_for_offer));

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
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_productsautocomplete + " >> responseString = " + responseString);
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
                    params.put("page", String.valueOf(page));
                    params.put("word", acTextView.getText().toString());
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
