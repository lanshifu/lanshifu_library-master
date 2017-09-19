package library.lanshifu.com.myapplication.network_into.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.HostBean;

/**
 * Created by lanshifu on 2017/9/17.
 */

public class NetworkFunctionActivity extends BaseToolBarActivity {

    private HostBean hostBean;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_fucntionlist;
    }

    @Override
    protected void onViewCreate() {

        hostBean = (HostBean) getIntent().getSerializableExtra("host");

        setTBTitle("功能列表-目标："+ hostBean.getIp());

    }



    @OnClick({R.id.mitm_select_sniffer, R.id.mitm_select_hijack, R.id.mitm_select_inject, R.id.mitm_select_kill, R.id.mitm_select_monitor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mitm_select_sniffer:
                showShortToast("嗅探");
                break;
            case R.id.mitm_select_hijack:
                break;
            case R.id.mitm_select_inject:
                break;
            case R.id.mitm_select_kill:
                Intent intent = new Intent(this,NetworkKillActivity.class);
                intent.putExtra("targetIp",hostBean.getIp());
                startActivity(intent);

                break;


            case R.id.mitm_select_monitor:
                break;
        }
    }
}
