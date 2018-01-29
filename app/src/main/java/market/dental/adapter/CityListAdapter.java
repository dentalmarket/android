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
import market.dental.model.City;
import market.dental.model.Profession;

/**
 * Created by kemalsamikaraca on 29.01.2018.
 */

public class CityListAdapter extends ArrayAdapter {

    private Context context;
    private List<City> cityList;

    public CityListAdapter(@NonNull Context context) {
        super(context, R.layout.dm_alert_dialog_list_items);
        this.context  = context;
    }

    public CityListAdapter(@NonNull Context context , List<City> cityList) {
        super(context, R.layout.dm_alert_dialog_list_items);
        this.context  = context;
        this.cityList = cityList;
    }

    public void setCityList(List<City> cityList){
        this.cityList = cityList;
    }

    @Override
    public int getCount(){
        return cityList.size();
    }

    @Override
    public Object getItem(int position){
        return this.cityList.get(position);
    }


    @Override
    public View getView(int position, View view, ViewGroup viewgroup){

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.dm_alert_dialog_list_items,viewgroup,false);

        TextView itemNameTextView = customView.findViewById(R.id.dm_alert_dialog_list_item_name);
        itemNameTextView.setText(cityList.get(position).getName());

        return customView;
    }

}
