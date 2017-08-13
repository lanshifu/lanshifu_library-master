package library.lanshifu.com.myapplication.fragment.round;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.commwidget.Indicator;
import library.lanshifu.com.lsf_library.commwidget.autoscrolltoplayout.AutoScrollToTopLayout;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;

/**
 * Created by Administrator on 2017/8/12.
 */

public class RoundFragment extends BaseFragment {

    @Bind(R.id.home_head_pager)
    ViewPager homeHeadPager;
    @Bind(R.id.home_head_indicator)
    Indicator homeHeadIndicator;
    @Bind(R.id.menu_indicator)
    Indicator menu_indicator;
    @Bind(R.id.reclclerview)
    RecyclerView reclclerview;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.menu_viewpager)
    ViewPager menu_viewpager;
    @Bind(R.id.scrollToTopLayout)
    AutoScrollToTopLayout scrollToTopLayout;

    private int[] imgRes = new int[]{R.mipmap.banner01, R.mipmap.banner02, R.mipmap.banner03};
    private Handler mHandler = new Handler();
    private FragmentManager fragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.round_fragment;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

        homeHeadPager.setAdapter(new BannerPagerAdapter(getChildFragmentManager(), imgRes));
        homeHeadIndicator.setCount(imgRes.length);
        homeHeadPager.addOnPageChangeListener(new ViewPagerListener(homeHeadIndicator));

        List<Fragment> menuFragments = new ArrayList<>();
        menuFragments.add(new Menu1Fragment());
        menuFragments.add(new Menu2Fragment());
        fragmentManager = getFragmentManager();
        menu_viewpager.setAdapter(new MenuPagerAdapter(fragmentManager, menuFragments));

        menu_viewpager.addOnPageChangeListener(new ViewPagerListener(menu_indicator));

        antoScroll();
        initList();


    }

    private void initList() {

        final CommonAdapter<String> adapter = new CommonAdapter<String>(getActivity(), R.layout.list_item, initData(0)) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.title, s);

            }
        };

//        final HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
//
//        View headView = View.inflate(getActivity(), R.layout.menu_viewpager, null);
//        ViewPager mViewPager = headView.findViewById(R.id.menu_viewpager);
//        List<Fragment> menuFragments = new ArrayList<>();
//        menuFragments.add(new Menu1Fragment());
//        menuFragments.add(new Menu2Fragment());
//        mViewPager.setAdapter(new MenuPagerAdapter(getActivity().getSupportFragmentManager(), menuFragments));
//
//        TextView t1 = new TextView(getActivity());
//        t1.setBackgroundColor(getResources().getColor(R.color.gray));
//        t1.setText("Header 1");
//        headerAndFooterWrapper.addHeaderView(headView);

        reclclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        reclclerview.setAdapter(adapter);

        //开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadmore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                ((View) refreshLayout).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.refresh(initData(20));
                        refreshLayout.finishRefresh();
                        refreshLayout.setLoadmoreFinished(false);
                    }
                }, 2000);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshLayout) {
                ((View) refreshLayout).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(initData(10));
                        refreshLayout.finishLoadmore();
                        if (adapter.getItemCount() > 30) {
                            T.showShort("数据全部加载完毕");
                            refreshLayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
                        }
                    }
                }, 2000);
            }
        });

        //触发自动刷新
        refreshLayout.autoRefresh();
        scrollToTopLayout.bindScrollBack();
    }


    private void antoScroll() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = homeHeadPager.getCurrentItem();
                homeHeadPager.setCurrentItem(currentItem + 1, true);
                mHandler.postDelayed(this, 2000);
            }
        }, 2000);
    }


    private List<String> initData(int size) {
        List<String> list = new ArrayList<>();
        if (size <= 0) {
            return list;
        }
        for (int i = 0; i < size; i++) {
            list.add("数据" + i);
        }
        return list;

    }

}
