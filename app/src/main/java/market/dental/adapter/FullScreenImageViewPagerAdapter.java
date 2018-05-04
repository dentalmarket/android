package market.dental.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import market.dental.android.FullscreenImageActivity;
import market.dental.android.R;
import market.dental.util.Resource;
import market.dental.util.Result;

public class FullScreenImageViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private JSONArray images;

    public FullScreenImageViewPagerAdapter(Context context){
        this.context = context;
    }

    public FullScreenImageViewPagerAdapter(Context context , JSONArray images){
        this.context = context;
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

        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_fullscreen_photoview,container,false);
        FullScreenImageViewPagerAdapter.ViewHolder viewHolder = new FullScreenImageViewPagerAdapter.ViewHolder(v);

        try{

            viewHolder.imageUrl = Resource.DOMAIN_NAME + "/" +((JSONObject)images.get(position)).getString("photo");
            Picasso.with(context)
                    .load( viewHolder.imageUrl )
                    .fit()
                    .centerInside()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.photoView);

        }catch (Exception ex){
            ex.printStackTrace();
            Crashlytics.log(Log.ERROR , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + "instantiateItem " + " >> exception");
        }

        container.addView(viewHolder.itemView);

        return viewHolder.itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        try{
            ((ViewPager) container).removeView((View)object);
            ConstraintLayout mainLayout = (ConstraintLayout)object;
            Picasso.with(context).cancelRequest((ImageView)mainLayout.findViewById(R.id.fullscreen_activity_photo_view));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public PhotoView photoView;
        public String imageUrl;

        public ViewHolder(View itemView) {
            super(itemView);
            photoView = (PhotoView)itemView.findViewById(R.id.fullscreen_activity_photo_view);
        }

    }

}
