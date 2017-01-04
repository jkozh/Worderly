package com.julia.android.worderly.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.julia.android.worderly.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

class MessageViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.messengerTextView)
    TextView messengerTextView;
    @BindView(R.id.messengerImageView)
    CircleImageView messengerImageView;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.timeTextView)
    TextView timeTextView;

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
