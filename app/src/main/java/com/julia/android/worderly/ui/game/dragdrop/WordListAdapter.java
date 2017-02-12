package com.julia.android.worderly.ui.game.dragdrop;

import android.app.Activity;
import android.content.ClipData;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.julia.android.worderly.R;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<ListViewHolder>
        implements View.OnTouchListener {

    private static final String TAG = WordListAdapter.class.getSimpleName();
    private List<TilesList> mTilesList;
    private Listener mListener;
    private int colorTile;

    public WordListAdapter(List<TilesList> tilesList, Listener listener) {
        this.mTilesList = tilesList;
        this.mListener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.row_item, parent, false);
        if (parent.getId() == R.id.recycler_view_top) {
            colorTile = R.color.blue_grey_100;
        }
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) parent.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int tileSize = (dm.widthPixels - 20) / 7;
        view.setLayoutParams(new RecyclerView.LayoutParams(tileSize, tileSize));
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        TilesList tilesList = mTilesList.get(position);
        if (colorTile != R.color.blue_grey_100) {
            colorTile = tilesList.color;
        }
        holder.rl.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(), colorTile));
        holder.textTile.setText(String.valueOf(tilesList.letter));
        holder.textScore.setText(String.valueOf(tilesList.value));
        holder.rl.setTag(position);
        holder.rl.setOnTouchListener(this);
        holder.rl.setOnDragListener(new DragListener(mListener));
    }

    @Override
    public int getItemCount() {
        return mTilesList.size();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(data, shadowBuilder, v, 0);
                } else {
                    v.startDrag(data, shadowBuilder, v, 0);
                }
                return true;
        }
        return false;
    }

    public DragListener getDragInstance() {
        if (mListener != null) {
            return new DragListener(mListener);
        } else {
            Log.e(TAG, "Listener wasn't initialized!");
            return null;
        }
    }

    List<TilesList> getCustomList() {
        return mTilesList;
    }

    void updateCustomList(List<TilesList> tilesList) {
        this.mTilesList = tilesList;
    }

}

