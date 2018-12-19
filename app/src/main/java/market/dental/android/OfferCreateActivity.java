package market.dental.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import market.dental.adapter.OfferProductAddedListAdapter;
import market.dental.model.Product;

public class OfferCreateActivity extends BaseActivity {

    private OfferProductAddedListAdapter addedProductListAdapter;

    private static final int PRODUCT_ADD_FOR_OFFER = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialization
        addedProductListAdapter = new OfferProductAddedListAdapter(this);

        // ACTION LISTENER
        Button openOfferAddProduct = findViewById(R.id.open_offer_add_product_activity);
        openOfferAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OfferProductAddActivity.class);
                startActivityForResult(intent , PRODUCT_ADD_FOR_OFFER);
            }
        });
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
                    ListView listView = findViewById(R.id.fragment_offer_added_product_list);
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

}
