
package library.lanshifu.com.myapplication.widget.cardslideview;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * description
 * <p>调整ViewPager滑动视差
 * Created by sunjian on 2017/6/22.
 */
class CardTransformer implements ViewPager.PageTransformer {

    private int mMaxTranslateOffsetX;
    private ViewPager mViewPager;

    private static final float ROT_MAX = 20.0f;
    private float mRot;

    CardTransformer(int maxOffset) {

        mMaxTranslateOffsetX = maxOffset;
    }


    @Override
    public void transformPage(View page, float position) {

        if (mViewPager == null) {
            mViewPager = (ViewPager) page.getParent();
        }

//        int leftInScreen = page.getLeft() - mViewPager.getScrollX();
//        int centerXInViewPager = leftInScreen + page.getMeasuredWidth() / 2;
//        int offsetX = centerXInViewPager - mViewPager.getMeasuredWidth() / 2;
//        float offsetRate = (float) offsetX * 0.38f / mViewPager.getMeasuredWidth();
//        float scaleFactor = 1 - Math.abs(offsetRate);
//
//        if (scaleFactor > 0) {
//            page.setScaleX(scaleFactor);
//            page.setScaleY(scaleFactor);
//            page.setTranslationX(-mMaxTranslateOffsetX * offsetRate);
//        }

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setRotation(0);
        } else if (position <= 1) // a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0
        { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0) {
                mRot = (ROT_MAX * position);
                page.setPivotX(page.getMeasuredWidth() * 0.5f);
                page.setPivotY(page.getMeasuredHeight());
                page.setRotation(mRot);
            } else {
                mRot = (ROT_MAX * position);
                page.setPivotX(page.getMeasuredWidth() * 0.5f);
                page.setPivotY(page.getMeasuredHeight());
                page.setRotation(mRot);
            }
            // Scale the page down (between MIN_SCALE and 1)
            // Fade the page relative to its size.
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setRotation(0);
        }
    }
}
