package market.dental.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import market.dental.android.R;
import market.dental.model.Message;

/**
 * Created by kemalsamikaraca on 21.02.2018.
 */

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private Context context;
    private List<Message> messageList;


    public MessageListAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {

        //UserMessage message = (UserMessage) mMessageList.get(position);
        //if (message.getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())) {
        if(true){
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View customView;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            customView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent,parent,false);
            return new SentMessageHolder(customView);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            customView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received,parent,false);
            return new ReceivedMessageHolder(customView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        public SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.item_message_sent_message_body);
            timeText = (TextView) itemView.findViewById(R.id.item_message_sent_message_time);
        }

        public void bind(Message message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getCreatedDate());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        public void bind(Message message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getCreatedDate());
        }
    }

}
