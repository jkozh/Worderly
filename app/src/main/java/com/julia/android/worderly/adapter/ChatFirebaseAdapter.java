package com.julia.android.worderly.adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.julia.android.worderly.model.Message;

public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {

    public ChatFirebaseAdapter(Class<Message> modelClass, int modelLayout,
                               Class<MessageViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(MessageViewHolder viewHolder, Message message, int position) {
        viewHolder.messengerTextView.setText(message.getName());
        viewHolder.messageTextView.setText(message.getText());
        viewHolder.timeTextView.setText("123");
    }
}
