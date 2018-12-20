package market.dental.android;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import market.dental.adapter.OfferListAdapter;
import market.dental.adapter.ProductListAdapter;
import market.dental.model.Offer;
import market.dental.util.Resource;
import market.dental.util.Result;

/**
 * A placeholder fragment containing a simple view.
 */
public class OfferActivityFragment extends Fragment {

    private OfferListAdapter offerListAdapter;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private boolean isLastPage;
    private boolean isLoading = false;
    private View fragmentView;
    private static final int OFFER_REQUEST_UPDATE = 1111;
    private static final int OFFER_REQUEST_CREATE = 1101;

    public OfferActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialization
        requestQueue = Volley.newRequestQueue(this.getContext());
        offerListAdapter = new OfferListAdapter(this.getContext());

        getOfferList(-1);

        fragmentView = inflater.inflate(R.layout.fragment_offer, container, false);

        Button createNewOffer = fragmentView.findViewById(R.id.new_offer_create_button);
        createNewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),OfferCreateActivity.class);
                startActivityForResult(intent , OFFER_REQUEST_CREATE);
            }
        });

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (OFFER_REQUEST_UPDATE) : {
                if (resultCode == Activity.RESULT_OK) {

                    try{
                        int position = data.getIntExtra("OFFER_REQUEST_POSITION", -1);
                        Offer updatedOffer = new Offer(data.getStringExtra("OFFER_REQUEST_UPDATE_JSON_STR"));
                        if(position!=-1){
                            Offer offerInAdapter = offerListAdapter.getOfferList().get(position);
                            offerInAdapter.setActive(updatedOffer.isActive());
                            offerInAdapter.setName(updatedOffer.getName());
                        }else{
                            // Eğer position bir yerlerde kaybolursa
                            for(Offer offerInAdapter: offerListAdapter.getOfferList()){
                                if(offerInAdapter.getId() == updatedOffer.getId()){
                                    offerInAdapter.setActive(updatedOffer.isActive());
                                    offerInAdapter.setName(updatedOffer.getName());
                                }
                            }
                        }

                        ListView listView = fragmentView.findViewById(R.id.offer_list_main);
                        if(listView.getAdapter()==null)
                            listView.setAdapter(offerListAdapter);
                        else{
                            offerListAdapter.notifyDataSetChanged();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_ERROR.getResultText(), this.getClass().getName() );
                    }

                }
                break;
            }

            case (OFFER_REQUEST_CREATE) : {
                if (resultCode == Activity.RESULT_OK) {
                    try{
                        Offer newOfferRequest = new Offer(data.getStringExtra("OFFER_REQUEST_CREATE_JSON_STR"));
                        offerListAdapter.addOffer(newOfferRequest);

                        ListView listView = fragmentView.findViewById(R.id.offer_list_main);
                        if(listView.getAdapter()==null)
                            listView.setAdapter(offerListAdapter);
                        else{
                            offerListAdapter.notifyDataSetChanged();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_ERROR.getResultText(), this.getClass().getName() );
                    }
                }
                break;
            }
        }
    }


    private void getOfferList(final int page){

        // *****************************************************************************************
        //                        GET PRODUCTS - USER HISTORY
        // *****************************************************************************************
        if(!isLoading && !isLastPage){
            isLoading = true;
            stringRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_get_my_offer_requests, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {

                    try {
                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){

                            JSONObject content = response.getJSONObject("content");
                            if(content.getJSONArray("data").length()>0){

                                offerListAdapter.addOfferList(Offer.offerList(content.getJSONArray("data")));
                                offerListAdapter.setCurrentPage(content.getInt("current_page"));

                                ListView listView = fragmentView.findViewById(R.id.offer_list_main);
                                if(listView.getAdapter()==null)
                                    listView.setAdapter(offerListAdapter);
                                else{
                                    offerListAdapter.notifyDataSetChanged();
                                }

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Offer sOffer = ((Offer) parent.getItemAtPosition(position));
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("offerId", sOffer.getId());
                                        bundle.putBoolean("isOfferAcvite", sOffer.isActive());
                                        bundle.putString("offerName" , sOffer.getName() );
                                        bundle.putInt("offer_list_position" , position );
                                        Intent intent = new Intent(view.getContext(),OfferCreateActivity.class);
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent, OFFER_REQUEST_UPDATE);
                                    }
                                });


                            } else{
                                isLastPage = false;
                            }

                        }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                            Resource.setDefaultAPITOKEN();
                            Intent intent = new Intent(getActivity().getApplicationContext() , LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_my_offer_requests + " >> responseString = " + responseString);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_get_my_offer_requests );
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
