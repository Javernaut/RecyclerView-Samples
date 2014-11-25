package com.javernaut.recyclerviewtest;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

/**
 * Animates elements changing and adding as horizontal flipping.
 * I consider a bug in {@link RecyclerView}
 * because newHolder in {@link ItemAnimator#animateChange(ViewHolder, ViewHolder)} is always null.
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
            mChangeAnimations.add(changeInfo.newHolder);
            final ViewPropertyAnimatorCompat newViewAnimation = ViewCompat.animate(newView);
            newViewAnimation.translationX(0).translationY(0).setDuration(getChangeDuration()).
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

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        ViewCompat.setScaleX(view, 0);
        ViewCompat.setAlpha(view, 1);
        mAddAnimations.add(holder);
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        animation.scaleX(1).setDuration(getAddDuration()).
                setListener(new VpaListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchAddStarting(holder);
                    }
                    @Override
                    public void onAnimationCancel(View view) {
                        ViewCompat.setAlpha(view, 1);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        animation.setListener(null);
                        ViewCompat.setScaleX(view, 1);
                        dispatchAddFinished(holder);
                        mAddAnimations.remove(holder);
                        dispatchFinishedWhenDone();
                    }
                }).start();
    }
}
