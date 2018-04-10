package market.dental.android;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import market.dental.adapter.ProductListAdapter;
import market.dental.adapter.ProductsRecyclerAdapter;
import market.dental.adapter.ViewPagerAdapter;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

public class ProductListActivity extends BaseActivity {

    private ProductListAdapter productListAdapter;
    private int categoryId;
    private String searchKey;
    private RequestQueue requestQueue;
    private boolean isLoading = false;
    private View view;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view = findViewById(android.R.id.content);

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

                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){

                        JSONObject content = response.getJSONObject("content");
                        productListAdapter.addProductList(Product.ProductList(content.getJSONArray("data")));
                        productListAdapter.setCurrentPage(content.getInt("current_page"));
                        productListAdapter.setImages(content.getJSONArray("category_sliders"));
                        if(productListAdapter.getImages().length()>0){
                            // list view size 1 arttırılır. ilk eleman product yerine resim (category_sliders)
                            // yerleştirileceğinden null olarak set edilir
                            productListAdapter.getProductList().add(0,null);
                        }

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

                    }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                        Resource.setDefaultAPITOKEN();
                        Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Beklenmedik bir durum ile karşılaşıldı" , Toast.LENGTH_LONG).show();
                        Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_products_by_category + " >> responseString = " + responseString);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_get_products_by_category );
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
        //                        AJAX - GET PRODUCTS BY SEARCH KEY
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
