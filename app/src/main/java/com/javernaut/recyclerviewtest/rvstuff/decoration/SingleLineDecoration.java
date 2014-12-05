package com.javernaut.recyclerviewtest.rvstuff.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.javernaut.recyclerviewtest.R;
import com.javernaut.recyclerviewtest.rvstuff.animator.PeriodicItemAnimator;

import java.util.*;

/**
 * Draws a line through centers of all child of a RecyclerView.
 */
public class SingleLineDecoration extends RecyclerView.ItemDecoration {
    private boolean drawOver;
    private final Lines lines;

    public SingleLineDecoration(Context context) {
        lines = new Lines(createPaint(context.getResources().getDimensionPixelSize(R.dimen.single_line_width)));
        drawOver = false;
    }

    /**
     * @param drawOver if true the line will be drawn over children. If false - behind.
     */
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
            if (!isItemAnimatorRunning(parent.getItemAnimator())) {
                lines.ensureSize(childCount);
                lines.reset();

                List<View> children = childrenSortedByPosition(parent);

                View prevChild = children.get(0);

                for (int i = 1; i < childCount; i++) {
                    final View newChild = children.get(i);
                    lines.addLine(prevChild, newChild);
                    prevChild = newChild;
                }
                lines.draw(c);
            }
        }
    }

    private boolean isItemAnimatorRunning(RecyclerView.ItemAnimator itemAnimator) {
        if (itemAnimator instanceof PeriodicItemAnimator) {
            return itemAnimator.isRunning() && !((PeriodicItemAnimator) itemAnimator).onlyChangeAnimationsAreRunning();
        } else {
            return itemAnimator.isRunning();
        }
    }

    // Yes, allocating and sorting while drawing. But it isn't harmful.
    private static List<View> childrenSortedByPosition(RecyclerView parent) {
        List<View> result = new ArrayList<>(parent.getChildCount());
        for (int pos = 0; pos < parent.getChildCount(); pos++) {
            result.add(parent.getChildAt(pos));
        }
        Collections.sort(result, POSITION_COMPARATOR);
        return result;
    }

    private static final Comparator<View> POSITION_COMPARATOR = new Comparator<View>() {
        @Override
        public int compare(View lhs, View rhs) {
            return getPosition(rhs) - getPosition(lhs);
        }

        private int getPosition(View view) {
            return ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewPosition();
        }
    };

    private static Paint createPaint(int width) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);
        return paint;
    }

    private static final class Lines {
        private static final int POINTS_PER_LINE = 4;
        private float[] points;
        private Paint paint;
        private int offset;

        private Lines(Paint paint) {
            this.paint = paint;
        }

        public void ensureSize(int childCount) {
            if (childCount > 1 && (points == null || points.length < sizeForChildCount(childCount))) {
                points = new float[sizeForChildCount(childCount)];
            }
        }

        public void reset() {
            offset = 0;
            Arrays.fill(points, 0);
        }

        public void addLine(View src, View dest) {
            addCoordinate(centerXOfView(src));
            addCoordinate(centerYOfView(src));
            addCoordinate(centerXOfView(dest));
            addCoordinate(centerYOfView(dest));
        }

        private void addCoordinate(float coordinate) {
            points[offset++] = coordinate;
        }

        public void draw(Canvas canvas) {
            canvas.drawLines(points, 0, offset, paint);
        }

        private static int sizeForChildCount(int childCount) {
            return (childCount - 1) * POINTS_PER_LINE;
        }

        private static int centerXOfView(View view) {
            return view.getLeft() + view.getWidth() / 2;
        }

        private static int centerYOfView(View view) {
            return view.getTop() + view.getHeight() / 2;
        }
    }
}
