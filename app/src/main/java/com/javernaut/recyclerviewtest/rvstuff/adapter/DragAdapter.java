package com.javernaut.recyclerviewtest.rvstuff.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.javernaut.recyclerviewtest.R;
import com.javernaut.recyclerviewtest.android.view.SwapWithShadowView;
import com.javernaut.recyclerviewtest.model.DragItem;
import com.javernaut.recyclerviewtest.rvstuff.viewholder.DragViewHolder;

import java.util.Collections;
import java.util.List;

public class DragAdapter extends BaseAdapter<DragItem, DragViewHolder> {

    private static final int NO_VALUE = -1;

    private long dragItemId = NO_VALUE;

    public DragAdapter(Context context, List<DragItem> myItems) {
        super(context, myItems);
    }

    public void setData(List<DragItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void shuffle() {
        Collections.shuffle(items);
        notifyDataSetChanged();
    }

    @Override
    public DragViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        DragViewHolder myViewHolder = new DragViewHolder(LAYOUT_INFLATER.inflate(R.layout.item_drag, viewGroup, false));
        myViewHolder.itemView.setOnLongClickListener(ON_LONG_CLICK_LISTENER);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(DragViewHolder myViewHolder, int position) {
        DragItem myItem = items.get(position);

        myViewHolder.setText(myItem.getText());

        ((SwapWithShadowView) myViewHolder.itemView).setShadowMode(getItemId(position) == dragItemId);
    }

    private final View.OnLongClickListener ON_LONG_CLICK_LISTENER = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(v);
            long childId = childViewHolder.getItemId();
            ClipData clipData = ClipData.newPlainText(null, String.valueOf(childId));
            recyclerView.startDrag(clipData, new View.DragShadowBuilder(v), null, 0);
            ((SwapWithShadowView) v).animateShadowMode(true);

            dragItemId = childId;

            return true;
        }
    };

    /**
     * Swaps currently dragged element with an element on a specific position
     */
    public void swapWithDragged(int childPosition) {
        if (dragItemId != NO_VALUE) {
            int pos2 = getPositionForItemId(dragItemId);
            if (pos2 != NO_VALUE && childPosition != pos2) {
                Collections.swap(items, childPosition, pos2);
                notifyDataSetChanged(); // works well
            }
        }
    }

    private int getPositionForItemId(long id) {
        for (int pos = 0; pos < items.size(); pos++) {
            if (getItemId(pos) == id) {
                return pos;
            }
        }
        return NO_VALUE;
    }

    public void clearDragState(RecyclerView recyclerView) {
        if (dragItemId != NO_VALUE) {
            RecyclerView.ViewHolder draggedViewHolder = recyclerView.findViewHolderForItemId(dragItemId);
            if (draggedViewHolder != null) {
                ((SwapWithShadowView) draggedViewHolder.itemView).animateShadowMode(false);
            }
            dragItemId = NO_VALUE;
        }
    }
}
