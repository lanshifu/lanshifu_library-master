package library.lanshifu.com.myapplication.network_into.activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.network_into.NetworkHelper;
import library.lanshifu.com.myapplication.network_into.service.ArpService;

/**
 * Created by lanshifu on 2017/9/18.
 */

public class NetworkKillActivity extends BaseToolBarActivity {
    @Bind(R.id.actionbar_logo_text)
    TextView actionbarLogoText;
    @Bind(R.id.kill_check_box)
    CheckBox killCheckBox;
    @Bind(R.id.header_progress)
    ProgressBar headerProgress;
    @Bind(R.id.header_text)
    TextView headerText;
    @Bind(R.id.header_view)
    RelativeLayout headerView;
    private String targetIp;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_network_kill;
    }

    @Override
    protected void onViewCreate() {

        targetIp = getIntent().getStringExtra("targetIp");
        setTBTitle(Html.fromHtml("<b>" + getString(R.string.prohibit_internet)
                + "</b> - <small>" + targetIp + "</small>"));

        if (NetworkHelper.isKillRunning) {
            killCheckBox.setChecked(true);
        } else {
            killCheckBox.setChecked(false);
        }

        killCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(NetworkKillActivity.this, ArpService.class);
                if (isChecked) {
//                    if (NetworkHelper.isHijackRunning)
//                        stopService(new Intent(NetworkKillActivity.this, HijackService.class));
//                    if (NetworkHelper.isInjectRunning)
//                        stopService(new Intent(NetworkKillActivity.this, InjectService.class));
//                    if (NetworkHelper.isTcpdumpRunning)
//                        stopService(new Intent(NetworkKillActivity.this, SnifferService.class));
                    headerView.setVisibility(View.VISIBLE);
                    startService(intent);
                } else {
                    headerView.setVisibility(View.GONE);
                    stopService(intent);
                }
            }
        });
    }


}
