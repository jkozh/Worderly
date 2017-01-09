package com.julia.android.worderly.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.julia.android.worderly.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.messengerTextView)
    public TextView messengerTextView;
    @BindView(R.id.messengerImageView)
    public CircleImageView messengerImageView;
    @BindView(R.id.messageTextView)
    public TextView messageTextView;
    @BindView(R.id.timeTextView)
    public TextView timeTextView;

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
