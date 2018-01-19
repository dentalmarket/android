package market.dental.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import market.dental.adapter.ProductListAdapter;
import market.dental.adapter.ProductsRecyclerAdapter;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

public class ProductListActivity extends AppCompatActivity {

    private ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get categoryId
        Intent intent = getIntent();
        int categoryId = intent.getIntExtra(Resource.KEY_CATEGORY_ID,-1);
        Toast.makeText(this,"categoryId = " + categoryId , Toast.LENGTH_LONG).show();

        // Initialization
        productListAdapter = new ProductListAdapter(this);

        // *****************************************************************************************
        //                        AJAX - GET PRODUCTS BY CATEGORY
        // *****************************************************************************************
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Resource.ajax_get_products_by_category, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // result objesinin kontrolü YAPILACAK
                    // result objesinin content değeri alınır
                    JSONObject content = response.getJSONObject("content");

                    productListAdapter.setProductList(Product.ProductList(content.getJSONArray("products")));
                    ListView listView = findViewById(R.id.activity_product_list_main);
                    listView.setAdapter(productListAdapter);

                    listView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int productId = ((Product) parent.getItemAtPosition(position)).getId();
                                Bundle bundle = new Bundle();
                                bundle.putInt(Resource.KEY_PRODUCT_ID, productId);
                                Intent intent = new Intent(view.getContext(),ProductDetailActivity.class);
                                intent.putExtras(bundle);
                                view.getContext().startActivity(intent);
                            }
                        }
                    );
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
                params.put("id", "TESTID-111222333");
                return params;
            }
        };
        rq.add(jsonObjectRequest);

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
