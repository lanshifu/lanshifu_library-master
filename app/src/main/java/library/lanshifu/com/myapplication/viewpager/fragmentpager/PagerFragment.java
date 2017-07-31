package library.lanshifu.com.myapplication.viewpager.fragmentpager;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import library.lanshifu.com.lsf_library.base.BasePagerFragment;
import library.lanshifu.com.lsf_library.base.DemoFragment;

/**
 * Created by lanxiaobin on 2017/7/28.
 */

public class PagerFragment extends BasePagerFragment {
    @Override
    protected List<Fragment> initFragmentList() {
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DemoFragment demoFragment = new DemoFragment();
            demoFragment.setTitle("这个0.0"+i);
            list.add(demoFragment);
        }
        return list;
    }

    @Override
    protected List<String> initTitleList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("这个0.0"+i);
        }
        return list;
    }




}
