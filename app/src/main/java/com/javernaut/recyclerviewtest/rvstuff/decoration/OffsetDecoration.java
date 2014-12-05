package com.javernaut.recyclerviewtest.rvstuff.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.javernaut.recyclerviewtest.R;

/**
 * Just adds 15
 */
public class OffsetDecoration extends RecyclerView.ItemDecoration {
    private final int offset;

    public OffsetDecoration(Context context) {
        this.offset = context.getResources().getDimensionPixelSize(R.dimen.drag_item_offset);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.offset(offset, offset);
    }
}
