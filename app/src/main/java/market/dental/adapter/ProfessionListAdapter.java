package market.dental.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import market.dental.android.R;
import market.dental.model.Profession;

/**
 * Created by kemalsamikaraca on 28.01.2018.
 */

public class ProfessionListAdapter extends ArrayAdapter {


    private Context context;
    private List<Profession> professionList;

    public ProfessionListAdapter(@NonNull Context context) {
        super(context, R.layout.alert_dialog_profession_list_items);
        this.context  = context;
    }

    public ProfessionListAdapter(@NonNull Context context , List<Profession> professionList) {
        super(context, R.layout.alert_dialog_profession_list_items);
        this.context  = context;
        this.professionList = professionList;
    }

    public void setProfessionList(List<Profession> productList){
        this.professionList = productList;
    }

    @Override
    public int getCount(){
        return professionList.size();
    }

    @Override
    public Object getItem(int position){
        return this.professionList.get(position);
    }


    @Override
    public View getView(int position, View view, ViewGroup viewgroup){

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.alert_dialog_profession_list_items,viewgroup,false);

        TextView textView = customView.findViewById(R.id.alert_dialog_profession_list_item_name);
        textView.setText(professionList.get(position).getName());

        return customView;
    }

}
