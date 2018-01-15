package market.dental.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import market.dental.android.R;

/**
 * Created by kemalsamikaraca on 14.01.2018.
 */

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {

    private Context context;

    ArrayList<String> products;
    public ProductsRecyclerAdapter(ArrayList<String> products){
        this.products = products;
    }

    @Override
    public ProductsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ProductsRecyclerAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(products.get(position));

        try{
            Picasso.with(context.getApplicationContext())
                    .load("http://dental.market/assets/images/products/25_79.png")
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.mImageView);
        }catch (Exception ex){
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.recycler_view_product_name);
            mImageView = (ImageView)itemView.findViewById(R.id.recycler_view_product_image);
        }
    }
}
