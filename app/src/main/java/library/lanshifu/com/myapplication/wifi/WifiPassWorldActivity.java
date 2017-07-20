package library.lanshifu.com.myapplication.wifi;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import rx.functions.Action1;

public class WifiPassWorldActivity extends BaseToolBarActivity {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private CommonAdapter<WifiInfo> adapter;
    private WifiPwManager wifiPwManager;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_wifi_pass_world;
    }

    @Override
    protected void onViewCreate() {
        setTBTitle("wifi密码查看");

        mRxManager.on("wifi", new Action1<List<WifiInfo>>() {
            @Override
            public void call(List<WifiInfo> wifiInfos) {
                refreshLayout.finishRefresh();
                if(wifiInfos ==null){
                    showShortToast("出错了，看log，可能是手机未root");
                    return;
                }
                adapter.refresh(wifiInfos);

            }
        });

        adapter = new CommonAdapter<WifiInfo>(this, R.layout.list_item, new ArrayList<WifiInfo>()) {
            @Override
            protected void convert(ViewHolder holder, WifiInfo wifiInfo, int position) {
                holder.setText(R.id.title, wifiInfo.Ssid);
                holder.setText(R.id.content, wifiInfo.Password);

            }
        };
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);

        //开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                getWiFiPw();
            }
        });


        wifiPwManager = new WifiPwManager();
        //触发自动刷新
        refreshLayout.autoRefresh();

//        getWiFiPw();
//循环 语句
        //查看密码，

    }

    private void getWiFiPw() {
        new Thread() {
            @Override
            public void run() {
                try {
                    List<WifiInfo> wifiInfos = wifiPwManager.Read();

                    mRxManager.post("wifi", wifiInfos);
                } catch (Exception e) {
                    Loge(e.toString());
                    mRxManager.post("wifi", null);
                }
            }
        }.start();
    }


}
