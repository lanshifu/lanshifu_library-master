package library.lanshifu.com.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.myapplication.MainFragment;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.databinding.User;
import library.lanshifu.com.myapplication.ui.DemoFragment;

/**
 * Created by Administrator on 2017/8/8.
 */

public class HomeFragment extends BaseFragment {


    @Bind(android.R.id.tabcontent)
    FrameLayout tabcontent;
    @Bind(android.R.id.tabs)
    TabWidget tabs;
    @Bind(R.id.main_tabHost)
    FragmentTabHost tabHost;

    private Class[] fragments = new Class[]{
        MainFragment.class,DemoFragment.class,
            DemoFragment.class,DemoFragment.class};


    private String[] titles = new String[]{"主页", "周边", "我的", "更多"};

    private int[] icons = new int[]{
            R.drawable.tab_home_selector, R.drawable.tab_around_selector,
            R.drawable.tab_me_selector, R.drawable.tab_more_selector};


    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {
        tabHost.setup(getActivity(),getActivity().getSupportFragmentManager(),android.R.id.tabcontent);
        for (int i = 0; i < fragments.length; i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_tab,null);
            ImageView tabIcon = (ImageView) view.findViewById(R.id.item_tab_iv);
            TextView tabTitle = (TextView) view.findViewById(R.id.item_tab_tv);
            tabIcon.setImageResource(icons[i]);
            tabTitle.setText(titles[i]);
            tabHost.addTab(tabHost.newTabSpec(""+i).setIndicator(view),fragments[i],null);
        }

    }

    private void setViewWithIntentData() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            User user = (User) intent.getSerializableExtra("extra");
            if (user != null) {
                tabHost.setCurrentTab(2);
            }
        }
    }

}
