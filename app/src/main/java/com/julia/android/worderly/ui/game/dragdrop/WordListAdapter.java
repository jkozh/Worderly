package com.julia.android.worderly.ui.game.dragdrop;

import android.app.Activity;
import android.content.ClipData;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.julia.android.worderly.R;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private List<CustomList> mCustomList;
    private Listener mListener;

    public WordListAdapter(List<CustomList> customList, Listener listener) {
        this.mCustomList = customList;
        this.mListener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.row_item, parent, false);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)parent.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        view.setLayoutParams(new RecyclerView.LayoutParams((dm.widthPixels-20) / 7, (dm.widthPixels-20) / 7));
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        CustomList customList = mCustomList.get(position);
        holder.rl.setBackgroundColor(holder.itemView.getResources().getColor(customList.color));
        holder.textWord.setText(customList.letter);
        holder.textScore.setText(String.valueOf(position));
        holder.rl.setTag(position);
        holder.rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            view.startDragAndDrop(data, shadowBuilder, view, 0);
                        } else {
                            view.startDrag(data, shadowBuilder, view, 0);
                        }
                        return true;
                }
                return false;
            }
        });
        holder.rl.setOnDragListener(new DragListener(mListener));
    }

    public DragListener getDragInstance() {
        if (mListener != null) {
            return new DragListener(mListener);
        } else {
            Log.e("WordListAdapter", "Listener wasn't initialized!");
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mCustomList.size();
    }

    List<CustomList> getCustomList() {
        return mCustomList;
    }

    void updateCustomList(List<CustomList> customList) {
        this.mCustomList = customList;
    }
}

