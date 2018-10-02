package market.dental.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import market.dental.adapter.ProductsRecyclerAdapter;
import market.dental.adapter.ViewPagerAdapter;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

public class MainFragment extends Fragment {

    private Timer timer;
    private View view;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mRecyclerLayoutManager;
    private RecyclerView.Adapter mRecyclerAdapter;
    private RecyclerView chosenProducts;
    private RecyclerView.Adapter chosenProductsAdapter;
    private RecyclerView.LayoutManager chosenProductsLayoutManager;
    private RecyclerView discountedProducts;
    private RecyclerView.Adapter discountedProductsAdapter;
    private RecyclerView.LayoutManager discountedProductsLayoutManager;
    private RecyclerView newestProducts;
    private RecyclerView.Adapter newestProductsAdapter;
    private RecyclerView.LayoutManager newestProductsLayoutManager;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView[] dots;
    private EditText searchText;
    private OnFragmentInteractionListener mListener;

    public MainFragment() {}

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}

        getActivity().setTitle("Anasayfa");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RequestQueue rq = Volley.newRequestQueue(getContext());
        view = inflater.inflate(R.layout.fragment_main, container, false);

        // *****************************************************************************************
        //                              BANNER IMAGES
        // *****************************************************************************************
        StringRequest bannerRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_banner_images_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {
                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){
                        onResponseBannerImages(response.getJSONArray("content"));
                    }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                        Resource.setDefaultAPITOKEN();
                        Intent intent = new Intent(getActivity().getApplicationContext() , LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                        Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , "MainFragment >> " + Resource.ajax_get_banner_images_url + " >> responseString = " + responseString);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_get_banner_images_url );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Crashlytics.log(Log.ERROR , Result.LOG_TAG_INFO.getResultText() , "MainActivity >> onErrorResponse");
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        rq.add(bannerRequest);


        // *****************************************************************************************
        //                              FEATURED PRODUCTS
        // *****************************************************************************************

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL , false);

        chosenProducts = (RecyclerView) view.findViewById(R.id.fragment_main_chosen_products_recycler);
        chosenProducts.setHasFixedSize(true);
        chosenProductsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL , false);

        discountedProducts = (RecyclerView) view.findViewById(R.id.fragment_main_discounted_products_recycler);
        discountedProducts.setHasFixedSize(true);
        discountedProductsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL , false);

        newestProducts = (RecyclerView) view.findViewById(R.id.fragment_main_newest_products_recycler);
        newestProducts.setHasFixedSize(true);
        newestProductsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL , false);

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_products_homeproduct_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {
                    
                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){
                        mRecyclerView.setLayoutManager(mRecyclerLayoutManager);
                        mRecyclerAdapter = new ProductsRecyclerAdapter(Product.ProductList(response.getJSONObject("content").getJSONObject("featured").getJSONArray("products")));
                        mRecyclerView.setAdapter(mRecyclerAdapter);

                        chosenProducts.setLayoutManager(chosenProductsLayoutManager);
                        chosenProductsAdapter = new ProductsRecyclerAdapter(Product.ProductList(response.getJSONObject("content").getJSONObject("chosen").getJSONArray("products")));
                        chosenProducts.setAdapter(chosenProductsAdapter);

                        discountedProducts.setLayoutManager(discountedProductsLayoutManager);
                        discountedProductsAdapter = new ProductsRecyclerAdapter(Product.ProductList(response.getJSONObject("content").getJSONObject("discounted").getJSONArray("products")));
                        discountedProducts.setAdapter(discountedProductsAdapter);

                        newestProducts.setLayoutManager(newestProductsLayoutManager);
                        newestProductsAdapter = new ProductsRecyclerAdapter(Product.ProductList(response.getJSONObject("content").getJSONObject("newest").getJSONArray("products")));
                        newestProducts.setAdapter(newestProductsAdapter);
                    }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                        Resource.setDefaultAPITOKEN();
                        Intent intent = new Intent(getActivity().getApplicationContext() , LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                        Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , "MainFragment >> " + Resource.ajax_get_products_homeproduct_url + " >> responseString = " + responseString);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                    Crashlytics.log(Log.ERROR , Result.LOG_TAG_INFO.getResultText() , "MainFragment >> " + Resource.ajax_get_products_homeproduct_url + " >> response error");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Crashlytics.log(Log.ERROR , Result.LOG_TAG_INFO.getResultText() , "MainActivity >> onErrorResponse");
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
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


        searchText = (EditText) view.findViewById(R.id.fragment_main_search);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // REDIRECT TO PRODUCTLIST
                    Bundle bundle = new Bundle();
                    bundle.putString(Resource.SHAREDPREF_SEARCH_KEY, searchText.getText().toString());
                    Intent intent = new Intent(getActivity(),ProductListActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    handled = true;
                }
                return handled;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        
        if(timer!=null){
            timer.cancel();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Bu TimerTask class ile image slider otomatik değiştirmesi sağlanıyor
     */
    public class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                if(viewPager!=null){
                    if(viewPager.getCurrentItem()+1==viewPagerAdapter.getCount()){
                        viewPager.setCurrentItem(0);
                    }else{
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
                }

                }
            });
        }
    }


    /**
     * ViewPagerAdapter volley response sonrası doldurulmasını sağlar
     * @param images
     */
    public void onResponseBannerImages(JSONArray images){
        viewPager = (ViewPager)view.findViewById(R.id.fragment_main_view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getActivity(),images);
        viewPager.setAdapter(viewPagerAdapter);

        LinearLayout sliderDotsPanel = (LinearLayout)view.findViewById(R.id.viewpage_dots_layout);
        dots = new ImageView[viewPagerAdapter.getCount()];
        for(int i=0; i<viewPagerAdapter.getCount(); i++){
            dots[i] = new ImageView(getActivity());

            if(i==0){
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext() , R.drawable.active_dot));
            }else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext() , R.drawable.nonactive_dot));
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
                for(int i=0; i<viewPagerAdapter.getCount(); i++){
                    if(i==position){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext() , R.drawable.active_dot));
                    }else{
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext() , R.drawable.nonactive_dot));
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask() , 2000 , 4000);
    }

    @Override
    public void onDestroy() {

        timer=null;
        view=null;
        mRecyclerView=null;
        mRecyclerLayoutManager=null;
        mRecyclerAdapter=null;
        chosenProducts=null;
        chosenProductsAdapter=null;
        chosenProductsLayoutManager=null;
        discountedProducts=null;
        discountedProductsAdapter=null;
        discountedProductsLayoutManager=null;
        newestProducts=null;
        newestProductsAdapter=null;
        newestProductsLayoutManager=null;
        viewPager=null;
        viewPagerAdapter=null;
        dots=null;
        searchText=null;
        mListener=null;

        super.onDestroy();
    }

}
