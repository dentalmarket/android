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

/**
 * Created by kemalsamikaraca on 21.02.2018.
 */

public class ConversationListAdapter extends ArrayAdapter {

    private Context context;
    private List<String> conversationList;

    public ConversationListAdapter(@NonNull Context context) {
        super(context, R.layout.activity_conversation_list_items);
        this.context  = context;
    }

    public void setConversationList(List<String> conversationList){
        this.conversationList = conversationList;
    }


    @Override
    public int getCount(){
        return conversationList.size();
    }

    @Override
    public Object getItem(int position){
        return this.conversationList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup){

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.activity_conversation_list_items,viewgroup,false);

        TextView textView = customView.findViewById(R.id.activity_conversation_list_item_product_name);
        textView.setText(conversationList.get(position));

        return customView;
    }

}
