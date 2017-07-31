package library.lanshifu.com.myapplication.viewpager.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            // view.setAlpha(0);
            view.setAlpha(0);
        } else if (position <= 0)// a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
        { // [-1,0]
            // Use the default slide transition when moving to the left page
            // view.setAlpha(1);
            view.setAlpha(1);
            // view.setTranslationX(0);
            view.setTranslationX(0);
            // view.setScaleX(1);
            view.setScaleX(1);
            // view.setScaleY(1);
            view.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            // view.setAlpha(1 - position);
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            // view.setTranslationX(pageWidth * -position);
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - position);
            // view.setScaleX(scaleFactor);
            view.setScaleX(scaleFactor);
            // view.setScaleY(1);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            // view.setAlpha(0);
            view.setAlpha(0);
        }
    }
}