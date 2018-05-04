package market.dental.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullscreenImageActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        PhotoView photoView = (PhotoView)findViewById(R.id.fullscreen_activity_photo_view);


        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent!=null){
            Picasso.with(this)
                .load( callingActivityIntent.getStringExtra("url"))
                .fit()
                .centerInside()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(photoView);

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}
