package market.dental.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import market.dental.android.R;
import market.dental.model.Offer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OfferListAdapter extends ArrayAdapter {

    private int userType;
    private Context context;
    private int currentPage;
    private List<Offer> offerList;

    public OfferListAdapter(@NonNull Context context) {
        super(context, R.layout.adapter_item_offer_list);
        this.context  = context;
        this.currentPage = 1;
        this.offerList = new ArrayList<>();
        userType = 0;   // default use
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
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

        view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_offer_list, viewgroup, false);
        OfferListViewHolder holder = new OfferListViewHolder();

        holder.offerName = view.findViewById(R.id.offer_name);
        holder.offerName.setText(offerList.get(position).getName());

        holder.offerDate = view.findViewById(R.id.offer_created_at);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        holder.offerDate.setText(df.format(offerList.get(position).getCreated_at()));

        holder.offerStatus = view.findViewById(R.id.offer_status);
        holder.offerCount = view.findViewById(R.id.offer_count);
        if(userType==2){
            holder.offerStatus.setVisibility(View.GONE);
            holder.offerCount.setVisibility(View.GONE);
        }else{
            holder.offerStatus.setText(offerList.get(position).isActive() ?
                    context.getResources().getString(R.string.offer_status_active) : context.getResources().getString(R.string.offer_status_inactive));
            holder.offerCount.setText(""+offerList.get(position).getNumberOfOffers());
        }
        view.setTag(holder);

        return view;
    }

    static class OfferListViewHolder {
        TextView offerName;
        TextView offerStatus;
        TextView offerCount;
        TextView offerDate;
    }


}
