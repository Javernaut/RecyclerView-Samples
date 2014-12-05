package com.javernaut.recyclerviewtest.stuff;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ViewHolder for our elements.
 */
public class MyItemViewHolder extends RecyclerView.ViewHolder {

    private ShapeDrawable shapeDrawable;

    public MyItemViewHolder(View itemView) {
        super(itemView);
        itemView.setBackground(shapeDrawable = new ShapeDrawable(new OvalShape()));
    }

    public void setColor(int color) {
        shapeDrawable.getPaint().setColor(color);
    }
}
