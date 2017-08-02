package library.lanshifu.com.myapplication.viewpager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import library.lanshifu.com.lsf_library.base.BaseTabActivity;
import library.lanshifu.com.myapplication.ui.DemoFragment;
import library.lanshifu.com.myapplication.viewpager.transformer.CardTransformer;

/**
 * Created by lanxiaobin on 2017/8/1.
 */

public class TabActivity extends BaseTabActivity {


    @Override
    protected void onViewCreate() {

        setTBTitle("标题");


        getTabLayout().setTabMode(TabLayout.MODE_SCROLLABLE);
        String[] titles = new String[]{"轨迹回放", "工作日志", "任务反馈", "工单总结", "预警总结", "营销总结"};
        setupTabLayout(titles, new Fragment[]{
                //
                new DemoFragment(), //
                new DemoFragment(), //
                new DemoFragment(),
                new DemoFragment(),
                new DemoFragment(),
                new DemoFragment(),
        });

        getViewPager().setOffscreenPageLimit(1);
        getViewPager().setPageTransformer(true, new CardTransformer());
    }


}
