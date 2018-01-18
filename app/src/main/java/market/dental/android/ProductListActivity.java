package market.dental.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private ProductListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listAdapter = new ProductListAdapter(this);
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Resource.ajax_get_products_by_category, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // result objesinin kontrolü YAPILACAK
                    // result objesinin content değeri alınır
                    JSONObject content = response.getJSONObject("content");

                    listAdapter.setProductList(Product.ProductList(content.getJSONArray("products")));
                    ListView listView = findViewById(R.id.activity_product_list_main);
                    listView.setAdapter(listAdapter);

                    listView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Log.i(Result.LOG_TAG_INFO.getResultText() , "DENEME");

                                String x = ((Product) parent.getItemAtPosition(position)).getName();
                                Toast.makeText(ProductListActivity.this,x,Toast.LENGTH_LONG).show();
                            }
                        }
                    );
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
                params.put("id", "XXX");
                return params;
            }
        };
        rq.add(jsonObjectRequest);




    }
}
