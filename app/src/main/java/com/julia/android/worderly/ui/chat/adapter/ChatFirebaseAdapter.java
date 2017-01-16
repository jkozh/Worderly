package com.julia.android.worderly.ui.chat.adapter;

import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {

    public ChatFirebaseAdapter(Class<Message> modelClass, int modelLayout,
                               Class<MessageViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(MessageViewHolder viewHolder, Message message, int position) {
        viewHolder.messengerTextView.setText(message.getName());
        viewHolder.messageTextView.setText(message.getText());
        viewHolder.timeTextView.setText(getTime(message));
        // Set striped background view for the messages
        setStripedBackground(viewHolder, position);
    }

    private void setStripedBackground(MessageViewHolder viewHolder, int position) {
        // TODO: make it work for only opponent messages
        if(position % 2 == 0) {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(
                    viewHolder.itemView.getContext(), R.color.colorChatDark));
        }
    }

    private String getTime(Message message) {
        String time = "";
        Object ts = message.getTimeStamp();
        if (ts != null) {
            if (DateUtils.isToday((long)ts)) {
                time = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(ts);
            } else {
                // TODO: Think about it below
                time = new SimpleDateFormat("EEE d, hh:mm", Locale.getDefault()).format(ts);
            }
        }
        return time;
    }
}
