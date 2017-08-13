package library.lanshifu.com.myapplication.fragment.round;

import android.support.v4.view.ViewPager;

import library.lanshifu.com.lsf_library.commwidget.Indicator;

/**
 * Created by asus on 2016/9/1.
 */
public class ViewPagerListener implements ViewPager.OnPageChangeListener {

    private Indicator mIndicator;

    public ViewPagerListener(Indicator indicator) {
        mIndicator = indicator;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.setOffset(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
