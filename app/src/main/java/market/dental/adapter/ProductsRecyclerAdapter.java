package market.dental.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import market.dental.android.ProductDetailActivity;
import market.dental.android.R;
import market.dental.model.Product;
import market.dental.util.Result;

/**
 * Created by kemalsamikaraca on 14.01.2018.
 */

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Product> products;

    public ProductsRecyclerAdapter(List<Product> products){
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

        try {
            Product product = products.get(position);
            holder.mTextView.setText(product.getName());
            Picasso.with(context.getApplicationContext())
                    .load(product.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.mImageView);
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public Button mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.recycler_view_product_name);
            mImageView = (ImageView)itemView.findViewById(R.id.recycler_view_product_image);
            mButton = (Button) itemView.findViewById(R.id.recycler_view_button_product_detail);

            mButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(),ProductDetailActivity.class);
                    v.getContext().startActivity(intent);
                    /*
                    FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main , fragment);
                    transaction.commit();
                    */
                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.i(Result.LOG_TAG_INFO.getResultText() , "CardView clicked event ");
                }
            });
        }
    }
}
