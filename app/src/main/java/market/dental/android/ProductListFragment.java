package market.dental.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import market.dental.adapter.ProductListAdapter;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment {

    private View view;
    private RequestQueue requestQueue;
    private ProductListAdapter productListAdapter;
    private int categoryId;
    private String searchKey;
    private boolean recentProducts = false;
    private boolean isLoading;
    private boolean isLastPage;
    private OnFragmentInteractionListener mListener;

    public ProductListFragment() {}

    public static ProductListFragment newInstance() {
        ProductListFragment fragment = new ProductListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isLastPage = false;
        isLoading = false;
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(Resource.KEY_CATEGORY_ID,-1);
            searchKey = getArguments().getString(Resource.SHAREDPREF_SEARCH_KEY);
            recentProducts = getArguments().getBoolean(Resource.KEY_GET_RECENT_PRODUCTS);
        }

        getActivity().setTitle(getString(R.string.title_category_products));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialization
        view = inflater.inflate(R.layout.activity_product_list, container, false);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        productListAdapter = new ProductListAdapter(getActivity());

        if(searchKey!=null && searchKey.length()>0){
            this.getSearchedProducts(0);
            //Toast.makeText(this,  searchKey , Toast.LENGTH_SHORT).show();
        }else if(categoryId>0){
            this.getCategoryProducts(0);
            //Toast.makeText(this, "" +  categoryId , Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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


    public void getSearchedProducts(final int page){
        // *****************************************************************************************
        //                        AJAX - GET PRODUCTS BY SEARCH KEY
        // *****************************************************************************************

        if(!isLoading && !isLastPage){
            isLoading = true;
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_get_product_by_search_key, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {

                    try {

                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){

                            JSONObject content = response.getJSONObject("content");
                            if(content.getJSONArray("data").length()>0){

                                productListAdapter.addProductList(Product.ProductList(content.getJSONArray("data")));
                                productListAdapter.setCurrentPage(content.getInt("current_page"));
                                ListView listView = view.findViewById(R.id.activity_product_list_main);
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

                            }else{
                                isLastPage = true;
                            }

                        } else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                            Resource.setDefaultAPITOKEN();
                            Intent intent = new Intent(getActivity().getApplicationContext() , LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_product_by_search_key + " >> responseString = " + responseString);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_product_by_search_key + " >> responseString = " + responseString);
                    } finally {
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


    public void getCategoryProducts(final int page){

        // *****************************************************************************************
        //                        AJAX - GET PRODUCTS BY CATEGORY
        // *****************************************************************************************
        if(!isLoading && !isLastPage){
            isLoading = true;
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_get_products_by_category, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {

                    try {

                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){

                            JSONObject content = response.getJSONObject("content");
                            if(content.getJSONArray("data").length()>0){
                                productListAdapter.addProductList(Product.ProductList(content.getJSONArray("data")));
                                productListAdapter.setCurrentPage(content.getInt("current_page"));

                                // list view size 1 arttırılır. ilk eleman product yerine resim (category_sliders)
                                // yerleştirileceğinden null olarak set edilir
                                if(productListAdapter.getCurrentPage()==1 && content.getJSONArray("category_sliders").length()>0){
                                    productListAdapter.setImages(content.getJSONArray("category_sliders"));
                                    productListAdapter.getProductList().add(0,null);
                                }

                                ListView listView = view.findViewById(R.id.activity_product_list_main);
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
                                });

                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(getString(R.string.alert_product_not_found , content.getString("category_name")));
                                builder.setPositiveButton(getString(R.string.dental_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Birşey yapmadan dialog kapatılır
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();

                                isLastPage = true;
                            }

                        } else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                            Resource.setDefaultAPITOKEN();
                            Intent intent = new Intent(getActivity().getApplicationContext() , LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),  getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_products_by_category + " >> responseString = " + responseString);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_get_products_by_category );
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
    }


    @Override
    public void onDestroy() {
        view = null;
        requestQueue = null;
        productListAdapter = null;
        searchKey = null;
        mListener=null;
        super.onDestroy();
    }

}
