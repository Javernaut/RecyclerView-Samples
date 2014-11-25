package com.javernaut.recyclerviewtest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SingleLineDecorator extends RecyclerView.ItemDecoration {
    private Paint paint;
    private Path path;
    private boolean drawOver;

    public SingleLineDecorator(Context context) {
        Resources resources = context.getResources();
        paint = createPaint(resources.getDimensionPixelSize(R.dimen.single_line_width));
        path = new Path();
        drawOver = false;
    }

    public void setDrawOver(boolean drawOver) {
        this.drawOver = drawOver;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!drawOver) {
            drawLine(c, parent);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (drawOver) {
            drawLine(c, parent);
        }
    }

    private void drawLine(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        if (childCount > 1) {
            path.reset();

            List<View> children = childrenSortedByPosition(parent);

            final View firstChild = children.get(0);
            path.moveTo(centerXOfView(firstChild), centerYOfView(firstChild));

            for (int i = 1; i < childCount; i++) {
                final View child = children.get(i);
                path.lineTo(centerXOfView(child), centerYOfView(child));
            }

            c.drawPath(path, paint);
        }
    }

    private static Paint createPaint(int width) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(width);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        return paint;
    }

    private static List<View> childrenSortedByPosition(RecyclerView parent) {
        List<View> result = new ArrayList<View>(parent.getChildCount());
        for (int pos = 0; pos < parent.getChildCount(); pos++) {
            result.add(parent.getChildAt(pos));
        }
        Collections.sort(result, POSITION_COMPARATOR);
        return result;
    }

    private static final Comparator<View> POSITION_COMPARATOR = new Comparator<View>() {
        @Override
        public int compare(View lhs, View rhs) {
            RecyclerView.LayoutParams lParams = (RecyclerView.LayoutParams) lhs.getLayoutParams();
            RecyclerView.LayoutParams rParams = (RecyclerView.LayoutParams) rhs.getLayoutParams();
            return rParams.getViewPosition() - lParams.getViewPosition();
        }
    };

    private static int centerXOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    private static int centerYOfView(View view) {
        return view.getTop() + view.getHeight() / 2;
    }
}
