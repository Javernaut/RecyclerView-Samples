package com.javernaut.recyclerviewtest.android.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.javernaut.recyclerviewtest.R;

/**
 * Layout that replaces its child with a solid shadow.
 */
public final class SwapWithShadowView extends FrameLayout {

    private static final int FADE_DURATION = 200;
    private View shadow;

    public SwapWithShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwapWithShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        shadow = new View(getContext());
        shadow.setBackgroundColor(getResources().getColor(R.color.accent));
        ViewCompat.setAlpha(shadow, 0);
        addView(shadow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void animateShadowMode(boolean shadowMode) {
        animateAlpha(shadow, shadowMode ? 1 : 0);
        animateAlpha(getContentView(), shadowMode ? 0 : 1);
    }

    private static void animateAlpha(View view, int newAlphaValue) {
        ViewCompat.animate(view).alpha(newAlphaValue).setDuration(FADE_DURATION).start();
    }

    public void setShadowMode(boolean shadowMode) {
        ViewCompat.setAlpha(shadow, shadowMode ? 1 : 0);
        ViewCompat.setAlpha(getContentView(), shadowMode ? 0 : 1);
    }

    private View getContentView() {
        if (getChildCount() != 2) {
            throw new IllegalStateException("SwapWithShadowView should have exactly one child view in XML");
        }
        return getChildAt(1);
    }
}
