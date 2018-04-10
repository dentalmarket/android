package market.dental.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import market.dental.android.R;
import market.dental.util.Resource;
import market.dental.util.Result;

/**
 * Created by kemalsamikaraca on 14.01.2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private JSONArray images;

    public ViewPagerAdapter(Activity activity){
        this.activity = activity;
    }

    public ViewPagerAdapter(Activity activity , JSONArray images){
        this.activity = activity;
        this.images = images;
    }

    public void setImages(JSONArray images){
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.imageslider_viewpager_item , container, false);
        ImageView image = (ImageView) itemView.findViewById(R.id.viewpage_imageView);

        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);

        try{
            Picasso.with(activity.getApplicationContext())
                    .load( Resource.DOMAIN_NAME + "/" +((JSONObject)images.get(position)).getString("image"))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                    .into(image);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        ((ViewPager) container).removeView((View)object);
    }

}
