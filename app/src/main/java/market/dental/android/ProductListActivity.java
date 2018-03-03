package market.dental.android;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.dental.adapter.ProductListAdapter;
import market.dental.adapter.ProductsRecyclerAdapter;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

public class ProductListActivity extends AppCompatActivity {

    private ProductListAdapter productListAdapter;
    private int categoryId;
    private String searchKey;
    private RequestQueue requestQueue;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get Params
        Intent intent = getIntent();
        categoryId = intent.getIntExtra(Resource.KEY_CATEGORY_ID,-1);
        searchKey = intent.getStringExtra(Resource.SHAREDPREF_SEARCH_KEY);

        // Initialization
        requestQueue = Volley.newRequestQueue(this);
        productListAdapter = new ProductListAdapter(this);

        if(searchKey!=null && searchKey.length()>0){
            this.getSearchedProducts(0);
            //Toast.makeText(this,  searchKey , Toast.LENGTH_SHORT).show();
        }else if(categoryId>0){
            this.getCategoryProducts(0);
            //Toast.makeText(this, "" +  categoryId , Toast.LENGTH_SHORT).show();
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


    public void getCategoryProducts(final int page){

        // *****************************************************************************************
        //                        AJAX - GET PRODUCTS BY CATEGORY
        // *****************************************************************************************
        isLoading = true;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_products_by_category, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {

                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONObject content = response.getJSONObject("content");

                    productListAdapter.addProductList(Product.ProductList(content.getJSONArray("data")));
                    productListAdapter.setCurrentPage(content.getInt("current_page"));
                    ListView listView = findViewById(R.id.activity_product_list_main);
                    if(listView.getAdapter()==null)
                        listView.setAdapter(productListAdapter);
                    else{
                        productListAdapter.notifyDataSetChanged();
                    }


                    // -- EVENTS --
                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            if(view.getLastVisiblePosition() == totalItemCount-1 && !isLoading){
                                getCategoryProducts(productListAdapter.getCurrentPage()+1);
                            }
                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                    isLoading = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"ProductListActivity >> JSONException >> 120");
                    isLoading = false;
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"ProductListActivity >> ERROR ON GET DATA >> 121");
                isLoading = false;
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                params.put("catId", ""+categoryId);
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
        requestQueue.add(jsonObjectRequest);
    }


    public void getSearchedProducts(final int page){
        // *****************************************************************************************
        //                        AJAX - GET PRODUCTS BY CATEGORY
        // *****************************************************************************************

        isLoading = true;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_product_by_search_key, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {

                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONObject content = response.getJSONObject("content");

                    productListAdapter.addProductList(Product.ProductList(content.getJSONArray("data")));
                    productListAdapter.setCurrentPage(content.getInt("current_page"));
                    ListView listView = findViewById(R.id.activity_product_list_main);
                    if(listView.getAdapter()==null)
                        listView.setAdapter(productListAdapter);
                    else{
                        productListAdapter.notifyDataSetChanged();
                    }


                    // -- EVENTS --
                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            if(view.getLastVisiblePosition() == totalItemCount-1 && !isLoading){
                                getSearchedProducts(productListAdapter.getCurrentPage()+1);
                            }
                        }
                    });

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

                    isLoading = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"ProductListActivity >> JSONException >> 122");
                    isLoading = false;
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"ProductListActivity >> ERROR ON GET DATA >> 123");
                isLoading = false;
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                params.put("title", searchKey);
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
        requestQueue.add(jsonObjectRequest);
    }

}
