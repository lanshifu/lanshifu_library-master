package library.lanshifu.com.lsf_library.base;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.TextView;

import library.lanshifu.com.lsf_library.R;
import library.lanshifu.com.lsf_library.utils.ViewIdGenerator;

/**
 * Created by lanxiaobin on 2017/8/1.
 */

public  abstract class BaseTabActivity extends BaseToolBarActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    @Override
    protected void doAfterSetContentView() {
        super.doAfterSetContentView();
        mTabLayout = (TabLayout) findViewById(R.id.comm_tab_layout);
        mTabLayout.setTabTextColors(R.color.colorPrimary, R.color.main_color);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        mTabLayout.setSelectedTabIndicatorColor(R.color.green);
    }

    @Override
    protected int getLayoutid() {
        return R.layout.base_tab_activity;
    }




    protected ViewPager getViewPager(){
        return mViewPager;
    }

    public TabLayout getTabLayout(){
        return mTabLayout;
    }

    /**
     * 使用内置的ViewPager进行初始化, 此时不必给getContentView返回布局资源id
     *
     * @param titles
     * @param fragments
     */
    protected void setupTabLayout(String[] titles, Fragment[] fragments) {

        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(new InnerVPAdapter(getSupportFragmentManager(), titles, fragments));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public static class InnerVPAdapter extends FragmentPagerAdapter {
        private Fragment[] mFragments;
        private String[] mTitles;

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        public InnerVPAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
