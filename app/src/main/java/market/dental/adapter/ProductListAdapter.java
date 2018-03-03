package market.dental.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import market.dental.android.R;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.model.Currency;

/**
 * Created by kemalsamikaraca on 18.01.2018.
 */

public class ProductListAdapter extends ArrayAdapter {

    private Context context;
    private List<Product> productList;
    private int currentPage;

    public ProductListAdapter(@NonNull Context context) {
        super(context, R.layout.activity_product_list_main_items);
        this.context  = context;
        this.currentPage = 1;
        this.productList = new ArrayList<>();
    }

    public void setProductList(List<Product> productList){
        this.productList = productList;
    }

    public void addProductList(List<Product> productList){
        this.productList.addAll(productList);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public int getCount(){
        return productList.size();
    }

    @Override
    public Object getItem(int position){
        return this.productList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup){

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.activity_product_list_main_items,viewgroup,false);

        TextView textView = customView.findViewById(R.id.activity_product_list_item_product_name);
        textView.setText(productList.get(position).getName());

        TextView brandTextView = customView.findViewById(R.id.activity_product_list_item_product_brand);
        brandTextView.setText(productList.get(position).getBrand()!=null ? productList.get(position).getBrand().getName() : "bilinmiyor");

        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/fontawesome-webfont.ttf");
        TextView priceTextView = customView.findViewById(R.id.activity_product_list_item_product_price);
        priceTextView.setTypeface(font);
        priceTextView.setText("" +productList.get(position).getSalePrice() + " " +
                Currency.getCurrencyString( context.getResources(),productList.get(position).getCurrencyId()));

        ImageView imageView = customView.findViewById(R.id.activity_product_list_item_image);
        Picasso.with(context)
                .load(productList.get(position).getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
        return customView;
    }

}
