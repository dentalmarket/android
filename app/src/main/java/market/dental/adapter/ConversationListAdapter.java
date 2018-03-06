package market.dental.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import market.dental.android.R;
import market.dental.model.Conversation;

/**
 * Created by kemalsamikaraca on 21.02.2018.
 */

public class ConversationListAdapter extends ArrayAdapter {

    private Context context;
    private List<Conversation> conversationList;

    public ConversationListAdapter(@NonNull Context context) {
        super(context, R.layout.activity_conversation_list_items);
        this.context  = context;
    }

    public void setConversationList(List<Conversation> conversationList){
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

        TextView textView = customView.findViewById(R.id.activity_conversation_list_item_name_surname);
        textView.setText(conversationList.get(position).getNameSurname());

        TextView cenversationMessage = customView.findViewById(R.id.activity_conversation_list_item_message);
        cenversationMessage.setText(conversationList.get(position).getMessage());

        TextView cenversationDate = customView.findViewById(R.id.activity_conversation_list_item_date);
        cenversationDate.setText(conversationList.get(position).getLastMessageDate().substring(0,conversationList.get(position).getLastMessageDate().indexOf(" ")));

        if(conversationList.get(position).getUserPhoto().length()>0){
            ImageView imageView = customView.findViewById(R.id.activity_conversation_list_item_image);
            Picasso.with(context)
                    .load(conversationList.get(position).getUserPhoto())
                    .placeholder(R.drawable.ic_person_24dp)
                    .error(R.drawable.ic_person_24dp)
                    .into(imageView);
        }
        return customView;
    }

}
