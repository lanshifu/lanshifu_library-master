package library.lanshifu.com.myapplication.fragment.round;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.commwidget.Indicator;
import library.lanshifu.com.lsf_library.commwidget.autoscrolltoplayout.AutoScrollToTopLayout;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.NewBean;
import library.lanshifu.com.myapplication.model.WechatItem;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RetrofitHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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


    public static final String WECHAT_APPKEY = "26ce25ffcfc907a26263e2b0e3e23676";
    //每页请求的 item 数量
    public final int mPs = 21;
    public int mPageMark = 1;
    private NewListAdapter newListAdapter;

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

        final CommonAdapter<String> adapter = new CommonAdapter<String>(getActivity(), R.layout.list_item, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.title, s);

            }
        };

        newListAdapter = new NewListAdapter(getActivity(), new ArrayList<WechatItem.ResultBean.ListBean>());

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

        reclclerview.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 2));
        reclclerview.setAdapter(newListAdapter);

        //开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadmore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                refreshLayout.setLoadmoreFinished(false);
                mPageMark = 1;
                requestData();
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshLayout) {
                requestData();
            }
        });

        if (!queryDB()) {
            //触发自动刷新
            refreshLayout.autoRefresh();
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    refreshLayout.autoRefresh();
                }
            }, 1000);
        }
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


    private void requestData() {
        RetrofitHelper.getWechatApi().getWechat(WECHAT_APPKEY, mPageMark, mPs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<WechatItem>() {
                    @Override
                    public void _onNext(WechatItem wechatItem) {
                        setNewDataAddList(wechatItem);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadmore();
                    }

                    @Override
                    public void _onError(String e) {
                        T.showShort("出错了" + e);
                        L.e(e);
                        refreshLayout.finishRefresh(false);
                        refreshLayout.finishLoadmore(false);
                    }
                });
    }


    private void setNewDataAddList(WechatItem wechatItem) {
        mPageMark++;
        List<WechatItem.ResultBean.ListBean> newData = wechatItem.getResult().getList();

        if (newData != null && newData.size() > 0) {
            newData.get(0).setItemType(1);
            L.e("数据+" + newData.size());
            if (mPageMark == 2) {
                newListAdapter.refresh(newData);
                DataSupport.deleteAll(NewBean.class);
            } else {
                newListAdapter.addAll(newData);
            }
            savaToDB(newData);
        } else {
            refreshLayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
        }

    }

    private void savaToDB(List<WechatItem.ResultBean.ListBean> newData) {
        for (WechatItem.ResultBean.ListBean listBean : newData) {
            NewBean newBean = new NewBean(listBean.getId(), listBean.getTitle(), listBean.getSource(), listBean.getFirstImg(),
                    listBean.getMark(), listBean.getUrl(), listBean.getItemType(), listBean.getSpansize());
            newBean.save();
        }
    }

    private boolean queryDB() {
        List<NewBean> newBeanList = DataSupport.findAll(NewBean.class);
        if (newBeanList == null || newBeanList.size() == 0) {
            return false;
        }
        List<WechatItem.ResultBean.ListBean> listBeanList = new ArrayList<>();
        for (NewBean newBean : newBeanList) {
            WechatItem.ResultBean.ListBean listBean = new WechatItem.ResultBean.ListBean();
            listBean.setUrl(newBean.getUrl());
            listBean.setId(newBean.getId());
            listBean.setItemType(newBean.getItemType());
            listBean.setFirstImg(newBean.getFirstImg());
            listBean.setTitle(newBean.getTitle());
            listBean.setSource(newBean.getSource());
            listBean.setMark(newBean.getMark());
            listBean.setSpansize(newBean.getSpansize());
            listBeanList.add(listBean);
        }
        newListAdapter.refresh(listBeanList);
        return true;
    }



}
