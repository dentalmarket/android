package market.dental.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import market.dental.adapter.ProductsRecyclerAdapter;
import market.dental.model.Currency;
import market.dental.model.Product;
import market.dental.model.Store;
import market.dental.util.Resource;
import market.dental.util.Result;

public class ProductDetailActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mRecyclerLayoutManager;
    private RecyclerView.Adapter mRecyclerAdapter;
    private Context productDetailContext;
    private ImageView  pdImageView;
    private TextView pdProductName;
    private TextView pdProductPrice;
    private TextView storeName;
    private TextView storePhone;
    private TextView storeGsm;
    private TextView storeEmail;
    private TextView brandName;
    private Button sendMessageBtn;

    private int productId;
    private String productDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get productId
        Intent intent = getIntent();
        productId = intent.getIntExtra(Resource.KEY_PRODUCT_ID,-1);

        // Initialization
        RequestQueue rq = Volley.newRequestQueue(this);
        productDetailContext = this.getApplicationContext();
        pdImageView = (ImageView)findViewById(R.id.activity_product_detail_image);
        pdProductName = (TextView)findViewById(R.id.activity_product_detail_product_name);
        pdProductPrice = (TextView)findViewById(R.id.activity_product_detail_price);
        storeName = (TextView)findViewById(R.id.activity_product_detail_store_name);
        storePhone = (TextView)findViewById(R.id.activity_product_detail_store_phone);
        storeGsm = (TextView)findViewById(R.id.activity_product_detail_store_gsm);
        storeEmail = (TextView)findViewById(R.id.activity_product_detail_store_email);
        brandName = (TextView)findViewById(R.id.activity_product_detail_brand_name);
        sendMessageBtn = (Button) findViewById(R.id.activity_product_detail_send_message_btn);

        // *****************************************************************************************
        //                              GET PRODUCT DETAIL
        // *****************************************************************************************
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_product_detail_similar_prod_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_product_detail_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {

                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    // result objesinin content değeri alınır
                    JSONObject response = new JSONObject(responseString);
                    JSONObject content = response.getJSONObject("content");
                    final Store store = new Store(content.getJSONObject("store"));

                    Product product = new Product(content);

                    productDesc = product.getDescription();

                    Typeface font = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
                    pdProductPrice.setTypeface(font);
                    pdProductPrice.setText(product.getSalePrice() + " "  + Currency.getCurrencyString( getResources(),product.getCurrencyId()));
                    pdProductName.setText((String) product.getName());
                    Picasso.with(productDetailContext)
                            .load(product.getImageUrl())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(pdImageView);

                    brandName.setText(product.getBrand().getName());

                    storeName.setText(store.getName());
                    storePhone.setText(store.getPhone());
                    storeGsm.setText(store.getGsm());
                    storeEmail.setText(store.getEmail());

                    mRecyclerView.setLayoutManager(mRecyclerLayoutManager);
                    mRecyclerAdapter = new ProductsRecyclerAdapter(Product.ProductList(content.getJSONArray("similarProducts")));
                    mRecyclerView.setAdapter(mRecyclerAdapter);

                    // SEND MESSAGE BUTTON EVENT
                    sendMessageBtn.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(),MessageListActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Resource.KEY_MESSAGE_RECEIVER_ID, String.valueOf(store.getId()));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"ProductListActivity >> JSONException >> 120");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"ProductListActivity >> ERROR ON GET DATA >> 121");
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                params.put("id", ""+productId);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        rq.add(jsonObjectRequest);


        Button productDescBnt = findViewById(R.id.activity_product_detail_product_desc_btn);
        productDescBnt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Resource.KEY_PRODUCT_DESC, productDesc);
                Intent intent = new Intent(view.getContext(),ProductDescriptionActivity.class);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
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
}
