package com.javernaut.recyclerviewtest;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Animates elements changing as horizontal flipping.
 */
public class MyItemAnimator extends AbstractItemAnimator {
    public MyItemAnimator() {
        setSupportsChangeAnimations(true);
    }

    @Override
    protected void animateChangeImpl(final AbstractItemAnimator.ChangeInfo changeInfo) {
        final RecyclerView.ViewHolder holder = changeInfo.oldHolder;
        final View view = holder.itemView;
        final RecyclerView.ViewHolder newHolder = changeInfo.newHolder;
        final View newView = newHolder != null ? newHolder.itemView : null;
        mChangeAnimations.add(changeInfo.oldHolder);

        final ViewPropertyAnimatorCompat oldViewAnim = ViewCompat.animate(view).setDuration(
                getChangeDuration());
        oldViewAnim.translationX(changeInfo.toX - changeInfo.fromX);
        oldViewAnim.translationY(changeInfo.toY - changeInfo.fromY);
        oldViewAnim.scaleX(0).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationStart(View view) {
                dispatchChangeStarting(changeInfo.oldHolder, true);
            }
            @Override
            public void onAnimationEnd(View view) {
                oldViewAnim.setListener(null);
                ViewCompat.setScaleX(view, 1);
                ViewCompat.setTranslationX(view, 0);
                ViewCompat.setTranslationY(view, 0);
                dispatchChangeFinished(changeInfo.oldHolder, true);
                mChangeAnimations.remove(changeInfo.oldHolder);
                dispatchFinishedWhenDone();
            }
        }).start();
        if (newView != null) {
            ViewCompat.setScaleX(newView, 0);
            ViewCompat.setAlpha(newView, 1);
            mChangeAnimations.add(changeInfo.newHolder);
            final ViewPropertyAnimatorCompat newViewAnimation = ViewCompat.animate(newView);
            newViewAnimation.translationX(0).translationY(0).setStartDelay(getChangeDuration()).setDuration
                    (getChangeDuration()).
                    scaleX(1).setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationStart(View view) {
                    dispatchChangeStarting(changeInfo.newHolder, false);
                }
                @Override
                public void onAnimationEnd(View view) {
                    newViewAnimation.setListener(null);
                    ViewCompat.setScaleX(newView, 1);
                    ViewCompat.setTranslationX(newView, 0);
                    ViewCompat.setTranslationY(newView, 0);
                    dispatchChangeFinished(changeInfo.newHolder, false);
                    mChangeAnimations.remove(changeInfo.newHolder);
                    dispatchFinishedWhenDone();
                }
            }).start();
        }
    }
}
