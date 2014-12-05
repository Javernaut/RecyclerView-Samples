package com.javernaut.recyclerviewtest.rvstuff.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.ButterKnife;

abstract class ButterKnifeViewHolder extends RecyclerView.ViewHolder {
    ButterKnifeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
