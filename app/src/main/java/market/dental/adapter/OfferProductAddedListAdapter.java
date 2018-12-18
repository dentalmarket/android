package market.dental.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import market.dental.android.R;
import market.dental.model.Product;

public class OfferProductAddedListAdapter extends ArrayAdapter {

    private Context context;
    private List<Product> addedProductList;

    public OfferProductAddedListAdapter(@NonNull Context context) {
        super(context, R.layout.adapter_item_offer_list_search);
        this.context  = context;
        this.addedProductList = new ArrayList<>();
    }

    public void addProductList(List<Product> productList){
        this.addedProductList.addAll(productList);
    }

    public void addProduct(Product product){
        this.addedProductList.add(product);
    }

    public void clearProductList(){
        this.addedProductList.clear();
    }

    @Override
    public int getCount(){
        return addedProductList.size();
    }

    @Override
    public Object getItem(int position){
        return this.addedProductList.get(position);
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewgroup){

        AddedProductListForOfferViewHolder holder;
        if(view==null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_offer_added_product_list, viewgroup, false);
            holder = new AddedProductListForOfferViewHolder();
            holder.pName = view.findViewById(R.id.offer_added_product_name);
            holder.pName.setText(addedProductList.get(position).getName());

            holder.pImageButton = (ImageButton)view.findViewById(R.id.activity_product_list_item_image);
            Picasso.with(context)
                    .load(addedProductList.get(position).getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .resize(120, 100)
                    .into(holder.pImageButton);

            holder.pRemoveButton = view.findViewById(R.id.adapter_item_remove_product);
            holder.pRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addedProductList.remove(position);
                    notifyDataSetChanged();
                }
            });

            view.setTag(holder);

        } else {
            holder = (AddedProductListForOfferViewHolder) view.getTag();
        }
        return view;
    }

    static class AddedProductListForOfferViewHolder {
        TextView pName;
        TextView pSubtitle;
        ImageButton pImageButton;
        ImageView pRemoveButton;
    }


}
