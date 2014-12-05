package com.javernaut.recyclerviewtest.rvstuff.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Base class for adapters.
 *
 * @param <I> type of items in data collection
 * @param <T> type of ViewHolder for parent class
 */
abstract class BaseAdapter<I, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected final LayoutInflater LAYOUT_INFLATER;
    protected final List<I> items;

    protected BaseAdapter(Context context, List<I> items) {
        LAYOUT_INFLATER = LayoutInflater.from(context);
        this.items = items;
        setHasStableIds(true);
    }

    @Override
    public final int getItemCount() {
        return items.size();
    }

    @Override
    public final long getItemId(int position) {
        return items.get(position).hashCode();
    }
}
