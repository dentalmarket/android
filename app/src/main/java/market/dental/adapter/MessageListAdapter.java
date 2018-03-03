package market.dental.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import market.dental.android.R;
import market.dental.model.Message;

/**
 * Created by kemalsamikaraca on 21.02.2018.
 */

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_SENT_WITH_DATE = 2;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 3;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED_WITH_DATE = 4;
    private Context context;
    private List<Message> messageList;
    private int userId;


    public MessageListAdapter(Context context, List<Message> messageList , int userId) {
        this.context = context;
        this.messageList = messageList;
        this.userId = userId;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        boolean isNewDate = false;

        // -- mesajlaşma tarihi farklı bir gün olması durumu --
        // ilk mesajın tarihi her zaman konulur
        if(position == 0){
            isNewDate = true;
        // bir önceki mesaj tarihi ile karşılakştırılır
        }else{
            try {
                Date currentMessageDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messageList.get(position).getCreatedDate());
                Date prevMessageDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messageList.get(position-1).getCreatedDate());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if(!df.format(currentMessageDate).equals(df.format(prevMessageDate))){
                    isNewDate = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(messageList.get(position).getUserId().equals(String.valueOf(userId))){
            if(isNewDate){
                return VIEW_TYPE_MESSAGE_SENT_WITH_DATE;
            }else{
                return VIEW_TYPE_MESSAGE_SENT;
            }

        } else {
            if(isNewDate){
                return VIEW_TYPE_MESSAGE_RECEIVED_WITH_DATE;
            }else{
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View customView;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            customView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(customView, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_SENT_WITH_DATE) {
                customView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent,parent,false);
                return new SentMessageHolder(customView,true);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            customView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received,parent,false);
            return new ReceivedMessageHolder(customView , false);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED_WITH_DATE) {
            customView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(customView, true);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
            case VIEW_TYPE_MESSAGE_SENT_WITH_DATE:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
            case VIEW_TYPE_MESSAGE_RECEIVED_WITH_DATE:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    public void addItem(Message newMessage){
        this.messageList.add(newMessage);
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText,newDate;
        boolean isNewDate;

        public SentMessageHolder(View itemView , boolean isNewDate) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.item_message_sent_message_body);
            timeText = (TextView) itemView.findViewById(R.id.item_message_sent_message_time);
            newDate = (TextView) itemView.findViewById(R.id.item_message_sent_newdate);
            this.isNewDate = isNewDate;
        }

        public void bind(Message message) {
            messageText.setText(message.getMessage());
            timeText.setText("");

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(message.getCreatedDate());
                DateFormat df = new SimpleDateFormat("HH:mm");
                String messageDate =  df.format(date);
                timeText.setText(messageDate);

                if(isNewDate){
                    df = new SimpleDateFormat("dd/MM/yyyy");
                    messageDate =  df.format(date);
                    newDate.setText(messageDate);
                }else{
                    newDate.setVisibility(View.GONE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText, newDate;
        ImageView profileImage;
        boolean isNewDate;

        public ReceivedMessageHolder(View itemView , boolean isNewDate) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            newDate = (TextView) itemView.findViewById(R.id.item_message_received_newdate);
            this.isNewDate = isNewDate;
        }

        public void bind(Message message) {
            messageText.setText(message.getMessage());
            timeText.setText("");
            nameText.setVisibility(View.GONE);
            profileImage.setVisibility(View.GONE);

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(message.getCreatedDate());
                DateFormat df = new SimpleDateFormat("HH:mm");
                String messageDate =  df.format(date);
                timeText.setText(messageDate);

                if(isNewDate){
                    df = new SimpleDateFormat("dd/MM/yyyy");
                    messageDate =  df.format(date);
                    newDate.setText(messageDate);
                }else{
                    newDate.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
