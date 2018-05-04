package market.dental.android;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import market.dental.adapter.ProductDetailViewPagerAdapter;

public class FullscreenImageActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ProductDetailViewPagerAdapter productDetailViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_image);
        getSupportActionBar().hide();

        PhotoView photoView = (PhotoView)findViewById(R.id.fullscreen_activity_photo_view);

        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent!=null){

            Picasso.with(this)
                .load(callingActivityIntent.getStringExtra("url"))
                .fit()
                .centerInside()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(photoView);

/*
            try {
                viewPager = (ViewPager)findViewById(R.id.activity_product_detail_view_pager);
                productDetailViewPagerAdapter = new ProductDetailViewPagerAdapter(getApplicationContext(), new JSONArray(callingActivityIntent.getStringExtra("imagesJsonString")));
                viewPager.setAdapter(productDetailViewPagerAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
*/
        }


        TextView closeTextView = (TextView) findViewById(R.id.fullscreen_activity_close);
        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}
