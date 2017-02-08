package com.julia.android.worderly.ui.game.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.julia.android.worderly.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class ListViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text_word)
    TextView textWord;
    @BindView(R.id.text_score)
    TextView textScore;
    @BindView(R.id.relative_layout_item)
    RelativeLayout rl;

    ListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}