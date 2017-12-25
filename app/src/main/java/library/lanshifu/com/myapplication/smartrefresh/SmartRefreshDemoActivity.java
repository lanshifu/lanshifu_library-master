package library.lanshifu.com.myapplication.smartrefresh;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

//import com.scwang.smartrefresh.header.CircleHeader;
//import com.scwang.smartrefresh.header.DeliveryHeader;
//import com.scwang.smartrefresh.header.MaterialHeader;
//import com.scwang.smartrefresh.header.WaterDropHeader;
//import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.commwidget.autoscrolltoplayout.AutoScrollToTopLayout;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;

public class SmartRefreshDemoActivity extends BaseToolBarActivity {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private CommonAdapter<String> adapter;

    @Bind(R.id.scrollToTopLayout)
    AutoScrollToTopLayout scrollToTopLayout;


    @Override
    protected int getLayoutid() {
        return R.layout.activity_smart_refresh_demo;
    }

    @Override
    protected void onViewCreate() {

        initToolBar();

        setTBTitle("刷新控件");

        adapter = new CommonAdapter<String>(this, R.layout.list_item, initData(0)) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.title, s);

            }
        };
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);

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

    private void initToolBar() {
        addTBMore(new String[]{"样式1", "样式2", "样式3", "样式4", "样式5"});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String title = item.getTitle().toString();
//        if ("样式1".equals(title)) {
//            refreshLayout.setRefreshHeader(new MaterialHeader(this));
//            refreshLayout.setRefreshFooter(new BallPulseFooter(this));
//        } else if ("样式2".equals(title)) {
//            refreshLayout.setRefreshHeader(new WaterDropHeader(this));
//            refreshLayout.setRefreshFooter(new BallPulseFooter(this));
//        } else if ("样式3".equals(title)) {
//            refreshLayout.setRefreshHeader(new CircleHeader(this));
//        } else if ("样式4".equals(title)) {
//            refreshLayout.setRefreshHeader(new DeliveryHeader(this));
//        } else if ("样式5".equals(title)) {
//            refreshLayout.setRefreshHeader(new WaveSwipeHeader(this));
//        }

        return super.onOptionsItemSelected(item);
    }
}
