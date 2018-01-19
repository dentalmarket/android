package market.dental.android;

import android.content.Context;
import android.content.Intent;
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
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import market.dental.adapter.ProductsRecyclerAdapter;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

public class ProductDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mRecyclerLayoutManager;
    private RecyclerView.Adapter mRecyclerAdapter;
    private Context productDetailContext;
    private ImageView  pdImageView;
    private TextView pdProductName;
    private TextView storeName;

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
        Toast.makeText(this,"productId = " + productId , Toast.LENGTH_LONG).show();

        // Initialization
        RequestQueue rq = Volley.newRequestQueue(this);
        productDetailContext = this.getApplicationContext();
        pdImageView = (ImageView)findViewById(R.id.activity_product_detail_image);
        pdProductName = (TextView)findViewById(R.id.activity_product_detail_product_name);
        storeName = (TextView)findViewById(R.id.activity_product_detail_store_name);

        // *****************************************************************************************
        //                              GET PRODUCT DETAIL
        // *****************************************************************************************
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_product_detail_similar_prod_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Resource.ajax_get_product_detail_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // result objesinin kontrolü YAPILACAK
                    // result objesinin content değeri alınır
                    JSONObject content = response.getJSONObject("content");

                    productDesc = content.getString("description");
                    pdProductName.setText((String) content.getString("name"));
                    Picasso.with(productDetailContext)
                            .load((String) content.getJSONArray("images").get(0))
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(pdImageView);

                    JSONObject store = content.getJSONObject("store");
                    storeName.setText(store.getString("name"));

                    mRecyclerView.setLayoutManager(mRecyclerLayoutManager);
                    mRecyclerAdapter = new ProductsRecyclerAdapter(Product.ProductList(content.getJSONArray("similarProducts")));
                    mRecyclerView.setAdapter(mRecyclerAdapter);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(Result.LOG_TAG_INFO.getResultText(),"ERROR ON GET DATA");
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("id", "TESTID-111222333");
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
