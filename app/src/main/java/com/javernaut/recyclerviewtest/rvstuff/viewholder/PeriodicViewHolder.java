package com.javernaut.recyclerviewtest.rvstuff.viewholder;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ViewHolder for our elements.
 */
public class PeriodicViewHolder extends RecyclerView.ViewHolder {

    private final ShapeDrawable shapeDrawable;

    public PeriodicViewHolder(View itemView) {
        super(itemView);
        itemView.setBackground(shapeDrawable = new ShapeDrawable(new OvalShape()));
    }

    public void setColor(int color) {
        shapeDrawable.getPaint().setColor(color);
    }
}
