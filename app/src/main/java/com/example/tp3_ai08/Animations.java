package com.example.tp3_ai08;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Regroupe l'ensemble des animations de l'app
 */
public class Animations {
    public static class HearthAnimation extends Animation {
        final float G = 5000f;
        final float startAlpha;
        final float targetAlpha;
        final float startX;
        final float startY;
        final float startVx;
        final float startVy;
        View view;

        public HearthAnimation(View view, float targetAlpha, float startVx, float startVy) {
            this.view = view;
            this.targetAlpha = targetAlpha;
            this.startAlpha = view.getAlpha();
            this.startX = view.getX();
            this.startY = view.getY();
            this.startVx = startVx;
            this.startVy = startVy;
        }

        @Override
        public boolean willChangeBounds() {
            return false;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float newAlpha = startAlpha + (targetAlpha - startAlpha) * interpolatedTime;
            view.setAlpha(newAlpha);

            float newX = startVx * interpolatedTime + startX;
            float newY = (G / 2) * interpolatedTime * interpolatedTime + startVy * interpolatedTime + startY;
            view.setX(newX);
            view.setY(newY);
        }
    }

    public static class AlphaAnimation extends Animation {
        final float startAlpha;
        final float targetAlpha;
        View view;

        public AlphaAnimation(View view, float targetAlpha) {
            this.view = view;
            this.targetAlpha = targetAlpha;
            startAlpha = view.getAlpha();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float newAlpha = startAlpha + (targetAlpha - startAlpha) * interpolatedTime;
            view.setAlpha(newAlpha);
        }

        @Override
        public boolean willChangeBounds() {
            return false;
        }
    }

    public static class ResizeHeightAnimation extends Animation {
        final int startHeight;
        final int targetHeight;
        View view;

        public ResizeHeightAnimation(View view, int targetHeight) {
            this.view = view;
            this.targetHeight = targetHeight;
            startHeight = view.getHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = (int) (startHeight + (targetHeight - startHeight) * interpolatedTime);
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public static class TranslateXAnimation extends Animation {
        final float startX;
        final float targetX;
        View view;

        public TranslateXAnimation(View view, float targetX) {
            this.view = view;
            this.targetX = targetX;
            startX = view.getTranslationX();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float newX = startX + (targetX - startX) * interpolatedTime;
            view.setTranslationX(newX);
        }

        @Override
        public boolean willChangeBounds() {
            return false;
        }
    }
}
