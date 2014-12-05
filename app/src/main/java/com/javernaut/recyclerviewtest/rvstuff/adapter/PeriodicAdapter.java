package com.javernaut.recyclerviewtest.rvstuff.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.javernaut.recyclerviewtest.R;
import com.javernaut.recyclerviewtest.model.PeriodicItem;
import com.javernaut.recyclerviewtest.rvstuff.viewholder.PeriodicViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Entity that coordinates model and its presentation.
 */
public class PeriodicAdapter extends BaseAdapter<PeriodicItem, PeriodicViewHolder> {

    public static final int MAX_COLOR_VALUE = 256;
    public static final int TEN = 10;

    public PeriodicAdapter(Context context, List<PeriodicItem> items) {
        super(context, items);
    }

    public static PeriodicAdapter fromIntegers(Context context, List<Integer> integers) {
        List<PeriodicItem> colors = new ArrayList<PeriodicItem>(integers.size());
        for (Integer integer : integers) {
            colors.add(new PeriodicItem(integer));
        }
        return new PeriodicAdapter(context, colors);
    }

    public ArrayList<Integer> toIntegers() {
        ArrayList<Integer> result = new ArrayList<Integer>(items.size());
        for (PeriodicItem myItem : items) {
            result.add(myItem.getColor());
        }
        return result;
    }

    @Override
    public PeriodicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PeriodicViewHolder viewHolder = new PeriodicViewHolder(LAYOUT_INFLATER.inflate(R.layout.item_periodic,
                parent, false));

        viewHolder.itemView.setOnClickListener(ITEM_CLICK_LISTENER);
        viewHolder.itemView.setOnLongClickListener(ITEM_LONG_CLICK_LISTENER);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PeriodicViewHolder holder, int position) {
        holder.setColor(items.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    public void remove10() {
        if (items.size() > TEN) {
            for (int i = 0; i < TEN; i++) {
                items.remove(0);
            }
            notifyItemRangeRemoved(0, TEN);
        }
    }

    public void addSeveral() {
        Random random = new Random();
        int pos = random.nextInt(5) + 1;
        int amount = random.nextInt(5) + 1;
        for (int i = 0; i < amount; i++) {
            items.add(pos, new PeriodicItem(newRandColor()));
        }
        notifyItemRangeInserted(pos, amount);
    }

    private void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    private void toggleColor(int position) {
        PeriodicItem myItem = items.get(position);
        myItem.setColor(newRandColor());
        notifyItemChanged(position);
    }

    private static int newRandColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(MAX_COLOR_VALUE),
                random.nextInt(MAX_COLOR_VALUE),
                random.nextInt(MAX_COLOR_VALUE));
    }

    private final View.OnClickListener ITEM_CLICK_LISTENER = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleColor(getPosition(v));
        }
    };

    private final View.OnLongClickListener ITEM_LONG_CLICK_LISTENER = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            removeItem(getPosition(v));
            return true;
        }
    };

    private int getPosition(View v) {
        return ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewPosition();
    }
}
