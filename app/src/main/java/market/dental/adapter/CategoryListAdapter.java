package market.dental.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import market.dental.android.R;
import market.dental.model.Category;

/**
 * @author kemalsamikaraca
 * @version 1.0.1
 * @since 5/10/2018
 */

public class CategoryListAdapter extends ArrayAdapter {

    private Context context;
    private List<Category> categoryList;

    public CategoryListAdapter(@NonNull Context context) {
        super(context, R.layout.adapter_item_category_list);
        this.context = context;
    }

    public CategoryListAdapter(@NonNull Context context , List<Category> categoryList) {
        super(context, R.layout.adapter_item_category_list);
        this.context  = context;
        this.categoryList = categoryList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
    }

    public void clearCategoryList(){
        this.categoryList.clear();
    }


    @Override
    public int getCount(){
        return categoryList.size();
    }

    @Override
    public Object getItem(int position){
        return this.categoryList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup){

        ViewHolder holder = new ViewHolder();

        holder.categoryName = view.findViewById(R.id.adapter_item_category_list_name);
        holder.categoryName.setText(categoryList.get(position).getName());

        holder.categoryImage = view.findViewById(R.id.activity_product_list_item_image);
        Picasso.with(context)
                .load(categoryList.get(position).getIcon())
                .resize(120, 100)
                .into(holder.categoryImage);

        view.setTag(holder);
        return view;
    }


    static class ViewHolder {
        TextView categoryName;
        ImageView categoryImage;
    }

}
