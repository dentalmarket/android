package market.dental.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import market.dental.adapter.ProductDetailViewPagerAdapter;
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
    private TextView pdProductName;
    private TextView pdProductPrice;
    private TextView storeName;
    private TextView storePhone;
    private TextView storeGsm;
    private TextView storeEmail;
    private TextView brandName;
    private Button sendMessageBtn;
    private AlertDialog progressDialog;
    private ViewPager viewPager;
    private ProductDetailViewPagerAdapter productDetailViewPagerAdapter;
    private ImageView[] dots;
    private Timer timer;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

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
        requestQueue = Volley.newRequestQueue(this);
        productDetailContext = this.getApplicationContext();
        pdProductName = (TextView)findViewById(R.id.activity_product_detail_product_name);
        pdProductPrice = (TextView)findViewById(R.id.activity_product_detail_price);
        storeName = (TextView)findViewById(R.id.activity_product_detail_store_name);
        storePhone = (TextView)findViewById(R.id.activity_product_detail_store_phone);
        storeGsm = (TextView)findViewById(R.id.activity_product_detail_store_gsm);
        storeEmail = (TextView)findViewById(R.id.activity_product_detail_store_email);
        brandName = (TextView)findViewById(R.id.activity_product_detail_brand_name);
        sendMessageBtn = (Button) findViewById(R.id.activity_product_detail_send_message_btn);

        AlertDialog.Builder progressDialogBuilder = new AlertDialog.Builder(this);
        progressDialogBuilder.setCancelable(false);
        progressDialogBuilder.setView(getLayoutInflater().inflate(R.layout.dialog_progressbar,null));
        progressDialog = progressDialogBuilder.create();
        progressDialog.show();

        // *****************************************************************************************
        //                              GET PRODUCT DETAIL
        // *****************************************************************************************
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_product_detail_similar_prod_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false);
        stringRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_product_detail_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {

                try {

                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){
                        JSONObject content = response.getJSONObject("content");
                        final Store store = new Store(content.getJSONObject("store"));

                        Product product = new Product(content);

                        productDesc = product.getDescription();

                        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
                        pdProductPrice.setTypeface(font);
                        pdProductPrice.setText(product.getSalePrice() + " "  + Currency.getCurrencyString( getResources(),product.getCurrencyId()));
                        pdProductName.setText((String) product.getName());

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


                        // BURADA JSONARRAY ile resimler yollanır
                        JSONArray productImageList = content.getJSONArray("images");
                        JSONObject firstProductImage = new JSONObject();
                        firstProductImage.put("photo" , content.getString("image"));
                        firstProductImage.put("thumb" , content.getString("thumb"));
                        productImageList.put(0,firstProductImage);

                        viewPager = (ViewPager)findViewById(R.id.activity_product_detail_view_pager);
                        productDetailViewPagerAdapter = new ProductDetailViewPagerAdapter(getApplicationContext(),productImageList);
                        viewPager.setAdapter(productDetailViewPagerAdapter);


                        LinearLayout sliderDotsPanel = (LinearLayout)findViewById(R.id.activity_product_detail_view_pager_dots);
                        dots = new ImageView[productDetailViewPagerAdapter.getCount()];
                        for(int i=0; i<productDetailViewPagerAdapter.getCount(); i++){
                            dots[i] = new ImageView(getApplicationContext());

                            if(i==0){
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext() , R.drawable.active_dot));
                            }else{
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext() , R.drawable.nonactive_dot));
                            }

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(8,0,8,0);
                            sliderDotsPanel.addView(dots[i],params);
                        }

                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
                            @Override
                            public void onPageSelected(int position) {
                                for(int i=0; i<productDetailViewPagerAdapter.getCount(); i++){
                                    if(i==position){
                                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext() , R.drawable.active_dot));
                                    }else{
                                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext() , R.drawable.nonactive_dot));
                                    }
                                }
                            }
                            @Override
                            public void onPageScrollStateChanged(int state) {}
                        });

                        timer = new Timer();
                        timer.scheduleAtFixedRate(new ProductDetailActivity.MyTimerTask() , 2000 , 4000);

                    } else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                        redirectLoginActivity();
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"ProductDetail >> Exception");
                } finally {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"ProductDetail >> ERROR ON GET DATA >> 121");
                redirectLoginActivity();
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
        stringRequest.setTag(this.getClass().getName());
        requestQueue.add(stringRequest);


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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){

        if (requestQueue != null) {
            requestQueue.cancelAll(this.getClass().getName());
        }

        if(timer!=null){
            timer.cancel();
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.right_menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://dental.market/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if(viewPager!=null)
            viewPager.setAdapter(null);

        if(mRecyclerView!=null)
            mRecyclerView.setAdapter(null);

        productDetailViewPagerAdapter = null;
        if(mRecyclerLayoutManager!=null){
            mRecyclerLayoutManager.removeAllViews();
            mRecyclerLayoutManager=null;
        }

        mRecyclerAdapter = null;
        productDetailContext = null;
        pdProductName=null;
        pdProductPrice=null;
        storeName=null;
        storePhone=null;
        storeGsm=null;
        storeEmail=null;
        brandName=null;
        sendMessageBtn=null;
        progressDialog = null;
        productDesc=null;
        super.onDestroy();
    }

    /**
     * Bu TimerTask class ile image slider otomatik değiştirmesi sağlanıyor
     */
    public class MyTimerTask extends java.util.TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager!=null && productDetailViewPagerAdapter!=null){
                        if(viewPager.getCurrentItem()+1==productDetailViewPagerAdapter.getCount()){
                            viewPager.setCurrentItem(0);
                        }else{
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    }

                }
            });
        }
    }
}
