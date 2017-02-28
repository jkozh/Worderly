package com.julia.android.worderly.ui.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.julia.android.worderly.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MessageViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.text_messenger)
    TextView messengerTextView;
    @BindView(R.id.image_avatar_messenger)
    CircleImageView messengerImageView;
    @BindView(R.id.text_message)
    TextView messageTextView;
    @BindView(R.id.text_time)
    TextView timeTextView;


    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
