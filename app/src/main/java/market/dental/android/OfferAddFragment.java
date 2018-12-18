package market.dental.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import market.dental.adapter.OfferProductAddedListAdapter;
import market.dental.model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfferAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferAddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private OfferProductAddedListAdapter addedProductListAdapter;
    private View view;

    private static final int PRODUCT_ADD_FOR_OFFER = 1001;

    public OfferAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OfferAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfferAddFragment newInstance(String param1, String param2) {
        OfferAddFragment fragment = new OfferAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_offer_add, container, false);

        // Initialization
        addedProductListAdapter = new OfferProductAddedListAdapter(this.getContext());

        // ACTION LISTENER
        Button openOfferAddProduct = view.findViewById(R.id.open_offer_add_product_activity);
        openOfferAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),OfferProductAddActivity.class);
                startActivityForResult(intent , PRODUCT_ADD_FOR_OFFER);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PRODUCT_ADD_FOR_OFFER) : {
                if (resultCode == Activity.RESULT_OK) {
                    Gson gson = new Gson();
                    Product selectedProduct = gson.fromJson(data.getStringExtra("PRODUCT_GSON_STRING"), Product.class);

                    addedProductListAdapter.addProduct(selectedProduct);
                    ListView listView = view.findViewById(R.id.fragment_offer_added_product_list);
                    if (listView.getAdapter() == null){
                        listView.setAdapter(addedProductListAdapter);
                    }else{
                        addedProductListAdapter.notifyDataSetChanged();
                    }

                }
                break;
            }
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Girilen index değerine göre Adapter içerisindeki arraylist'ten product çıkarılır.
     * @param index
     */
    private void removeProductFromOffer(int index){

    }

}
