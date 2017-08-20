package library.lanshifu.com.myapplication.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.databinding.User;
import library.lanshifu.com.myapplication.live.LiveListFragment;
import library.lanshifu.com.myapplication.fragment.round.RoundFragment;
import library.lanshifu.com.myapplication.live.RecommendFragment;
import library.lanshifu.com.myapplication.ui.DemoFragment;
import library.lanshifu.com.myapplication.widget.FragmentTabHost;

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
            MainFragment.class, RoundFragment.class,
            LiveListFragment.class, RecommendFragment.class};


    private String[] titles = new String[]{"主页", "周边", "直播", "全民"};

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
        tabHost.setup(getActivity(), getActivity().getSupportFragmentManager(), android.R.id.tabcontent);
        for (int i = 0; i < fragments.length; i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_tab, null);
            ImageView tabIcon = (ImageView) view.findViewById(R.id.item_tab_iv);
            TextView tabTitle = (TextView) view.findViewById(R.id.item_tab_tv);
            tabIcon.setImageResource(icons[i]);
            tabTitle.setText(titles[i]);
            tabHost.addTab(tabHost.newTabSpec("" + i).setIndicator(view), fragments[i], null);
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
