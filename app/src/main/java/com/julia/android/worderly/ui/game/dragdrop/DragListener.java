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
//            case DragEvent.ACTION_DRAG_STARTED:
//                break;
//            case DragEvent.ACTION_DRAG_ENTERED:
//                View viewSource = (View) dragEvent.getLocalState();
//                viewSource.setBackgroundColor(RggbChannelVector.BLUE);
//
//                return true;
//            case DragEvent.ACTION_DRAG_EXITED:
//                break;
            case DragEvent.ACTION_DROP:
                isDropped = true;
                int positionTarget = -1;

                View viewSource = (View) dragEvent.getLocalState();
                int viewId = v.getId();
                int itemViewId = R.id.relative_layout_item;
                int recyclerViewTopId = R.id.recycler_view_top;
                int recyclerViewBottomId = R.id.recycler_view_bottom;
                int frameBottomId = R.id.frame_bottom;
                int frameTopId = R.id.frame_top;
                int imageHolderId = R.id.image_holder;

                if (viewId == itemViewId
                        || viewId == recyclerViewTopId
                        || viewId == recyclerViewBottomId
                        || viewId == frameTopId
                        || viewId == frameBottomId
                        || viewId == imageHolderId) {
                    RecyclerView target;

                    if (viewId == recyclerViewTopId || viewId == frameTopId || viewId == imageHolderId) {
                        target = (RecyclerView) v.getRootView().findViewById(recyclerViewTopId);
                    } else if (viewId == recyclerViewBottomId || viewId == frameBottomId) {
                        target = (RecyclerView) v.getRootView().findViewById(recyclerViewBottomId);
                    } else {
                        target = (RecyclerView) v.getParent();
                        positionTarget = (int) v.getTag();
                    }

                    if (viewSource != null) {
                        RecyclerView source = (RecyclerView) viewSource.getParent();

                        WordListAdapter adapterSource = (WordListAdapter) source.getAdapter();
                        int positionSource = (int) viewSource.getTag();
                        int sourceId = source.getId();

                        CustomList customList = adapterSource.getCustomList().get(positionSource);
                        List<CustomList> customListSource = adapterSource.getCustomList();

                        customListSource.remove(positionSource);
                        adapterSource.updateCustomList(customListSource);
                        adapterSource.notifyDataSetChanged();

                        WordListAdapter adapterTarget = (WordListAdapter) target.getAdapter();
                        List<CustomList> customListTarget = adapterTarget.getCustomList();
                        if (positionTarget >= 0) {
                            customListTarget.add(positionTarget, customList);
                        } else {
                            customListTarget.add(customList);
                        }
                        adapterTarget.updateCustomList(customListTarget);
                        adapterTarget.notifyDataSetChanged();
                        //v.setVisibility(View.VISIBLE);

                        if (sourceId == recyclerViewTopId && adapterSource.getItemCount() < 1) {
                            mListener.setEmptyListTop(true);
                        }
                        if (v.getId() == R.id.image_holder) {
                            mListener.setEmptyListTop(false);
                        }
                    }
//                    if (sourceId == recyclerViewBottomId && adapterSource.getItemCount() < 1) {
//                        mListener.setEmptyListBottom(true);
//                    }
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
