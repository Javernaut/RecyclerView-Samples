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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragAdapter extends BaseAdapter<DragItem, DragViewHolder> {

    private long dragItemId = -1;

    public DragAdapter(Context context, List<DragItem> myItems) {
        super(context, myItems);
    }

    public void setData(List<DragItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void shuffle() {
        List<DragItem> previousItems = new ArrayList<>(items);
        Collections.shuffle(items);
        for (int pos = 0; pos < items.size(); pos++) {
            notifyItemMoved(pos, items.indexOf(previousItems.get(pos)));
        }
    }

    @Override
    public DragViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        DragViewHolder myViewHolder = new DragViewHolder(LAYOUT_INFLATER.inflate(R.layout.item_drag, viewGroup, false));
        myViewHolder.itemView.setOnLongClickListener(onLongClickListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(DragViewHolder myViewHolder, int position) {
        DragItem myItem = items.get(position);

        myViewHolder.setText(myItem.getText());

        ((SwapWithShadowView) myViewHolder.itemView).setShadowMode(getItemId(position) == dragItemId);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private final View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
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
     * Swaps the element with specific id and an element on a specific position
     */
    public void swap(int childPosition) {
        int pos2 = getPosForId(dragItemId);
        if (pos2 != -1 && childPosition != pos2) {
            Collections.swap(items, childPosition, pos2);
            notifyDataSetChanged(); // works well
        }
    }

    private int getPosForId(long id) {
        for (int i = 0; i < items.size(); i++) {
            if (getItemId(i) == id) {
                return i;
            }
        }
        return -1;
    }

    public void clearDragState(RecyclerView recyclerView) {
        if (dragItemId != -1) {
            RecyclerView.ViewHolder draggedViewHolder = recyclerView.findViewHolderForItemId(dragItemId);
            if (draggedViewHolder != null) {
                ((SwapWithShadowView) draggedViewHolder.itemView).animateShadowMode(false);
            }
            dragItemId = -1;
        }
    }
}
