package com.javernaut.recyclerviewtest;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.FloatMath;
import android.view.View;

/**
 * Layouts elements as a periodic function sin or cos.
 * Handles both horizontal and vertical orientations.
 */
public class PeriodicLayoutManager extends LinearLayoutManager {
    private static final float FLOAT_PI = (float) Math.PI;

    private PeriodicFunc periodicFunc;

    private OrientationHelper oppositeOrientationHelper;

    public PeriodicLayoutManager(Context context) {
        super(context);
        periodicFunc = SIN;
    }

    public void setPeriodicFunc(PeriodicFunc periodicFunc) {
        this.periodicFunc = periodicFunc;
        requestLayout();
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        oppositeOrientationHelper = OrientationHelper.createOrientationHelper(this,
                orientation == OrientationHelper.HORIZONTAL ? OrientationHelper.VERTICAL : OrientationHelper.HORIZONTAL);
    }
    @Override
    public void layoutDecorated(View child, int left, int top, int right, int bottom) {
        int pos = getPosition(child);

        int itemHeight = getItemHeightByOrientation(left, top, right, bottom);
        float centerY = itemHeight * (pos + 0.5f);

        // the opposite direction size is a half of periodic function period
        float piValue = oppositeOrientationHelper.getTotalSpace() * 0.5f;

        int itemWidth = getItemWidthByOrientation(left, top, right, bottom);
        int adjustedCoordinate = (int) (piValue + (piValue * periodicFunc.f(centerY / piValue * FLOAT_PI)));

        // as we counts top or left position, we can layout items outside the parent. we shouts avoid this
        float multiplier = oppositeOrientationHelper.getTotalSpace() / (oppositeOrientationHelper.getTotalSpace() + (float) itemHeight);
        if (getOrientation() == HORIZONTAL) {
            top = adjustedCoordinate;
            top *= multiplier;
            bottom = top + itemWidth;
        } else {
            left = adjustedCoordinate;
            left *= multiplier;
            right = left + itemWidth;
        }
        super.layoutDecorated(child, left, top, right, bottom);
    }

    private int getItemHeightByOrientation(int left, int top, int right, int bottom) {
        return getOrientation() == HORIZONTAL ? bottom - top : right - left;
    }

    private int getItemWidthByOrientation(int left, int top, int right, int bottom) {
        return getOrientation() == HORIZONTAL ? right - left : bottom - top;
    }

    public static final PeriodicFunc SIN = new PeriodicFunc() {
        @Override
        public float f(float x) {
            return FloatMath.sin(x);
        }
    };

    public static final PeriodicFunc COS = new PeriodicFunc() {
        @Override
        public float f(float x) {
            return FloatMath.cos(x);
        }
    };

    public interface PeriodicFunc {
        float f(float x);
    }
}
