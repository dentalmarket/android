package market.dental.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import market.dental.android.R;
import market.dental.model.Borough;

/**
 * Created by kemalsamikaraca on 29.01.2018.
 */

public class BoroughListAdapter extends ArrayAdapter {

    private Context context;
    private List<Borough> boroughList;

    public BoroughListAdapter(@NonNull Context context) {
        super(context, R.layout.dm_alert_dialog_list_items);
        this.context  = context;
    }

    public BoroughListAdapter(@NonNull Context context , List<Borough> boroughList) {
        super(context, R.layout.dm_alert_dialog_list_items);
        this.context  = context;
        this.boroughList = boroughList;
    }

    public void setCityList(List<Borough> boroughList){
        this.boroughList = boroughList;
    }


    @Override
    public int getCount(){
        return boroughList.size();
    }

    @Override
    public Object getItem(int position){
        return this.boroughList.get(position);
    }


    @Override
    public View getView(int position, View view, ViewGroup viewgroup){

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.dm_alert_dialog_list_items,viewgroup,false);

        TextView itemNameTextView = customView.findViewById(R.id.dm_alert_dialog_list_item_name);
        itemNameTextView.setText(boroughList.get(position).getName());

        return customView;
    }

}
