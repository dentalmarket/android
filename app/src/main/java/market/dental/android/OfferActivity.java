package market.dental.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import market.dental.model.Product;
import market.dental.util.Result;

public class OfferActivity extends BaseActivity  {

    private static final int CREATE_NEW_OFFER = 1101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button createNewOffer = findViewById(R.id.new_offer_create_button);
        createNewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OfferCreateActivity.class);
                startActivityForResult(intent , CREATE_NEW_OFFER);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (CREATE_NEW_OFFER) : {
                if (resultCode == Activity.RESULT_OK) {

                    Log.i("DENEME" , data.getStringExtra("PRODUCT_GSON_STRING"));

                }
                break;
            }
        }
    }

}
