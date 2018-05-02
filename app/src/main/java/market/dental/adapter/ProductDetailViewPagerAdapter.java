package market.dental.adapter;

import android.content.Context;
import android.os.Build;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import market.dental.android.R;
import market.dental.util.Resource;
import market.dental.util.Result;

public class ProductDetailViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private JSONArray images;

    public ProductDetailViewPagerAdapter(Context context){
        this.context = context;
    }

    public ProductDetailViewPagerAdapter(Context context , JSONArray images){
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

        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.product_detail_viewpager_item,container,false);
        ProductDetailViewPagerAdapter.ViewHolder viewHolder = new ProductDetailViewPagerAdapter.ViewHolder(v);

        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View itemView = inflater.inflate(R.layout.imageslider_viewpager_item , container, false);
        //ImageView image = (ImageView) itemView.findViewById(R.id.viewpage_imageView);
/*
        DisplayMetrics dis = context.getResources().getDisplayMetrics();
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);
*/

        try{

            Picasso.with(context)
                    .load( Resource.DOMAIN_NAME + "/" +((JSONObject)images.get(position)).getString("photo"))
                    .fit()
                    .centerInside()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.mImageView);

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
            ConstraintLayout mainLayout = (ConstraintLayout)object;
            Picasso.with(context).cancelRequest((ImageView)mainLayout.findViewById(R.id.viewpage_imageView));
        }catch (Exception e){
            e.printStackTrace();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Crashlytics.log(Log.ERROR , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + "destroyItem " + " >> " + object.getClass().getName() + " - " + object.getClass().getTypeName());
        }else{
            Crashlytics.log(Log.ERROR , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + "destroyItem " + " >> " + object.getClass().getName());
        }

        ((ViewPager) container).removeView((View)object);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.viewpage_imageView);
        }
    }

}
