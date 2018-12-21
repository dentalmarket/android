package market.dental.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import market.dental.android.R;
import market.dental.model.Currency;
import market.dental.model.Offer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by kemalsamikaraca on 12.12.2018.
 */

public class OfferListAdapter extends ArrayAdapter {

    private Context context;
    private int currentPage;
    private List<Offer> offerList;

    public OfferListAdapter(@NonNull Context context) {
        super(context, R.layout.adapter_item_offer_list);
        this.context  = context;
        this.currentPage = 1;
        this.offerList = new ArrayList<>();
    }

    public List<Offer> getOfferList(){
        return this.offerList;
    }
    public void addOfferList(List<Offer> offerList){
        this.offerList.addAll(offerList);
    }
    public void addOffer(Offer offer){
        this.offerList.add(offer);
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public void clearOfferList(){
        this.offerList.clear();
        this.currentPage = 0;
    }

    @Override
    public int getCount(){
        return offerList.size();
    }

    @Override
    public Object getItem(int position){
        return this.offerList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup){

        OfferListViewHolder holder;
        if(view==null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_offer_list, viewgroup, false);
            holder = new OfferListViewHolder();

            holder.offerName = view.findViewById(R.id.offer_name);
            holder.offerName.setText(offerList.get(position).getName());

            holder.offerStatus = view.findViewById(R.id.offer_status);
            holder.offerStatus.setText(offerList.get(position).isActive() ?
                    context.getResources().getString(R.string.offer_status_active) : context.getResources().getString(R.string.offer_status_inactive));

            holder.offerCount = view.findViewById(R.id.offer_count);
            holder.offerCount.setText(""+offerList.get(position).getNumberOfOffers());

            holder.offerDate = view.findViewById(R.id.offer_created_at);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            holder.offerDate.setText(df.format(offerList.get(position).getCreated_at()));

            view.setTag(holder);

        } else {
            holder = (OfferListViewHolder) view.getTag();
        }

        return view;
    }

    static class OfferListViewHolder {
        TextView offerName;
        TextView offerStatus;
        TextView offerCount;
        TextView offerDate;
    }


}
