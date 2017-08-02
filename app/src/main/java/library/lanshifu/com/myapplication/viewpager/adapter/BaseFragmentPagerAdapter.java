package library.lanshifu.com.myapplication.viewpager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by lanxiaobin on 2017/8/1.
 */

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<PagerTab> pagerTags;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<PagerTab> pagerTags) {
        super(fm);
        this.pagerTags = pagerTags;
    }


    @Override
    public Fragment getItem(int position) {
        return pagerTags.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return pagerTags.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTags.get(position).getTitle();
    }
}
