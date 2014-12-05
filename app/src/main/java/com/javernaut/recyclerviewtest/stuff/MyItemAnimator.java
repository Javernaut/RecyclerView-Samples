package com.javernaut.recyclerviewtest.stuff;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;

/**
 * Animates elements changing as horizontal flipping.
 */
public class MyItemAnimator extends AbstractItemAnimator {
    public MyItemAnimator() {
        setSupportsChangeAnimations(true);
    }

    @Override
    protected void animateChangeImpl(final ChangeInfo changeInfo) {
        animateChangeDisappearing(changeInfo);
        animateChangeAppearing(changeInfo);
    }

    private void animateChangeDisappearing(final ChangeInfo changeInfo) {
        final View view = changeInfo.oldHolder.itemView;
        mChangeAnimations.add(changeInfo.oldHolder);

        final ViewPropertyAnimatorCompat oldViewAnim = ViewCompat.animate(view).setDuration(
                getChangeDuration());
        oldViewAnim.translationX(changeInfo.toX - changeInfo.fromX);
        oldViewAnim.translationY(changeInfo.toY - changeInfo.fromY);
        oldViewAnim.scaleX(0).scaleY(0).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationStart(View view) {
                dispatchChangeStarting(changeInfo.oldHolder, true);
            }

            @Override
            public void onAnimationEnd(View view) {
                oldViewAnim.setListener(null);
                ViewCompat.setScaleX(view, 1);
                ViewCompat.setScaleY(view, 1);
                ViewCompat.setTranslationX(view, 0);
                ViewCompat.setTranslationY(view, 0);
                dispatchChangeFinished(changeInfo.oldHolder, true);
                mChangeAnimations.remove(changeInfo.oldHolder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }

    private void animateChangeAppearing(final ChangeInfo changeInfo) {
        final View newView = changeInfo.newHolder != null ? changeInfo.newHolder.itemView : null;
        if (newView != null) {
            ViewCompat.setAlpha(newView, 0);
            mChangeAnimations.add(changeInfo.newHolder);
            final ViewPropertyAnimatorCompat newViewAnimation = ViewCompat.animate(newView);
            newViewAnimation.translationX(0).translationY(0).setDuration(getChangeDuration())
                    .alpha(1).setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationStart(View view) {
                    dispatchChangeStarting(changeInfo.newHolder, false);
                }

                @Override
                public void onAnimationEnd(View view) {
                    newViewAnimation.setListener(null);
                    ViewCompat.setAlpha(newView, 1);
                    ViewCompat.setTranslationX(newView, 0);
                    ViewCompat.setTranslationY(newView, 0);
                    dispatchChangeFinished(changeInfo.newHolder, false);
                    mChangeAnimations.remove(changeInfo.newHolder);
                    dispatchFinishedWhenDone();
                }
            }).start();
        }
    }

    public final boolean onlyChangeAnimationsAreRunning() {
        return (!mPendingChanges.isEmpty() || !mChangeAnimations.isEmpty() || !mChangesList.isEmpty()) &&
                mPendingAdditions.isEmpty() &&
                mPendingMoves.isEmpty() &&
                mPendingRemovals.isEmpty() &&
                mMoveAnimations.isEmpty() &&
                mRemoveAnimations.isEmpty() &&
                mAddAnimations.isEmpty() &&
                mMovesList.isEmpty() &&
                mAdditionsList.isEmpty();
    }
}
