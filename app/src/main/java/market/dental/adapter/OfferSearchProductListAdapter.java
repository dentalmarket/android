package market.dental.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import market.dental.android.R;
import market.dental.model.Product;

public class OfferSearchProductListAdapter extends ArrayAdapter {

    private Context context;
    private int currentPage;
    private List<Product> productList;
    private List<Product> suggestions;

    public OfferSearchProductListAdapter(@NonNull Context context) {
        super(context, R.layout.adapter_item_offer_list_search);
        this.context  = context;
        this.currentPage = 1;
        this.productList = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }

    public void addProductList(List<Product> productList){
        this.productList.addAll(productList);
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void clearProductList(){
        this.productList.clear();
        this.currentPage = 0;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
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

        ProductListForOfferViewHolder holder;
        if(view==null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_offer_list_search, viewgroup, false);
            holder = new ProductListForOfferViewHolder();
            holder.pName = view.findViewById(R.id.offer_add_product_name);
            holder.pName.setText(productList.get(position).getName());
            holder.pSubtitle = view.findViewById(R.id.offer_add_product_subtitle);
            holder.pSubtitle.setText(productList.get(position).getSubtitle());
            view.setTag(holder);
        } else {
            holder = (ProductListForOfferViewHolder) view.getTag();
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Product)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Product product : productList) {
                    if(product.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(product);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Product> filteredList = (ArrayList<Product>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Product c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }

    };

    static class ProductListForOfferViewHolder {
        TextView pName;
        TextView pSubtitle;
    }

}
