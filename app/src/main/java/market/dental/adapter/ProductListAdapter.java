package market.dental.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import market.dental.android.R;

/**
 * Created by kemalsamikaraca on 18.01.2018.
 */

public class ProductListAdapter extends ArrayAdapter {

    public List<String> productList;

    public ProductListAdapter(@NonNull Context context) {
        super(context, R.layout.activity_product_list_main_items);
        productList = new ArrayList<>();
        productList.add("KEmal");
        productList.add("Sami");
        productList.add("Sami");
        productList.add("Sami");
        productList.add("Sami");productList.add("Sami");
        productList.add("Sami");
        productList.add("Sami");productList.add("Sami");productList.add("Sami");
        productList.add("Sami");productList.add("Sami");productList.add("Sami");

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

        TextView textView = customView.findViewById(R.id.sssddd);
        textView.setText(productList.get(position));

        return customView;
    }

}
