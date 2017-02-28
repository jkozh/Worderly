package com.julia.android.worderly.ui.game.dragdrop;

import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;

import com.julia.android.worderly.R;

import java.util.List;


class DragListener implements View.OnDragListener {

    private boolean isDropped = false;
    private Listener mListener;


    DragListener(Listener listener) {
        mListener = listener;
    }


    @Override
    public boolean onDrag(View v, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DROP:
                isDropped = true;
                int positionTarget = -1;

                View viewSource = (View) dragEvent.getLocalState();
                int viewId = v.getId();
                final int itemViewId = R.id.relative_layout_item;
                final int recyclerView1Id = R.id.recycler_view_top;
                final int recyclerView2Id = R.id.recycler_view_bottom;
                final int frame1Id = R.id.frame_top;
                final int frame2Id = R.id.frame_bottom;
                final int imageHolderId = R.id.image_holder;

                if (viewId == itemViewId
                        || viewId == recyclerView1Id
                        || viewId == recyclerView2Id
                        || viewId == frame1Id
                        || viewId == frame2Id
                        || viewId == imageHolderId) {

                    RecyclerView target;

                    switch (viewId) {
                        case recyclerView1Id:
                        case frame1Id:
                        case imageHolderId:
                            target = (RecyclerView) v.getRootView().findViewById(recyclerView1Id);
                            break;
                        case recyclerView2Id:
                        case frame2Id:
                            target = (RecyclerView) v.getRootView().findViewById(recyclerView2Id);
                            break;
                        default:
                            target = (RecyclerView) v.getParent();
                            positionTarget = (int) v.getTag();
                    }

                    if (viewSource != null) {
                        RecyclerView source = (RecyclerView) viewSource.getParent();

                        TilesListAdapter adapterSource = (TilesListAdapter) source.getAdapter();
                        int positionSource = (int) viewSource.getTag();
                        int sourceId = source.getId();

                        TilesList tilesList = adapterSource.getCustomList().get(positionSource);
                        List<TilesList> tilesListSource = adapterSource.getCustomList();

                        tilesListSource.remove(positionSource);
                        adapterSource.updateCustomList(tilesListSource);
                        adapterSource.notifyDataSetChanged();

                        TilesListAdapter adapterTarget = (TilesListAdapter) target.getAdapter();
                        List<TilesList> tilesListTarget = adapterTarget.getCustomList();
                        if (positionTarget >= 0) {
                            tilesListTarget.add(positionTarget, tilesList);
                        } else {
                            tilesListTarget.add(tilesList);
                        }
                        adapterTarget.updateCustomList(tilesListTarget);
                        adapterTarget.notifyDataSetChanged();
                        //v.setVisibility(View.VISIBLE);

                        if (sourceId == recyclerView1Id && adapterSource.getItemCount() < 1) {
                            mListener.setEmptyListTop(true);
                        }
                        if (v.getId() == R.id.image_holder) {
                            mListener.setEmptyListTop(false);
                        }
                    }

                }
                return true;
        }

        if (!isDropped) {
            if (dragEvent.getLocalState() != null) {
                ((View) dragEvent.getLocalState()).setVisibility(View.VISIBLE);
            }
        }

        return true;
    }
}
