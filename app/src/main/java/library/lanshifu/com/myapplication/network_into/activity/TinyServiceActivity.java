package library.lanshifu.com.myapplication.network_into.activity;

import android.content.Intent;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.network_into.NetworkHelper;
import library.lanshifu.com.myapplication.network_into.service.HttpService;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.FileUtils;

/**
 * Created by lanshifu on 2017/9/17.
 */

public class TinyServiceActivity extends BaseToolBarActivity {
    @Bind(R.id.actionbar_logo)
    ImageView actionbarLogo;
    @Bind(R.id.actionbar_logo_text)
    TextView actionbarLogoText;
    @Bind(R.id.http_server_check_box)
    CheckBox httpServerCheckBox;
    @Bind(R.id.header_progress)
    ProgressBar headerProgress;
    @Bind(R.id.header_text)
    TextView headerText;
    @Bind(R.id.http_server_head)
    LinearLayout httpServerHead;
    @Bind(R.id.http_server_log)
    TextView httpServerLog;
    @Bind(R.id.http_server_log_scroll)
    ScrollView httpServerLogScroll;
    @Bind(R.id.httpserver_clone_head)
    TextView httpserverCloneHead;
    @Bind(R.id.httpserver_clone_btn)
    Button httpserverCloneBtn;
    private String url;
    private static final String webRootPath = FileUtils.getRootDirPath()+"www/";


    @Override
    protected int getLayoutid() {
        return R.layout.activity_tinyservice;
    }

    @Override
    protected void onViewCreate() {

        mRxManager.on("server_log", new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                httpServerLog.append(s+"\r\n");
            }
        });

        url = "http://" + NetworkHelper.getIp() + ":" + NetworkHelper.PORT;
        setTBTitle("小型服务器");

        httpServerLog.setMovementMethod(new ScrollingMovementMethod());
        httpServerLog.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                httpServerLog.setText("");
                return false;
            }
        });

        if (!NetworkHelper.isHttpserverRunning){
            httpServerLog.setText(Html.fromHtml(String.format(getString(R.string.http_server_log_tips), url)));
        }else {
            httpServerLog.setText("服务器在后台运行中\r\n");
            httpServerLog.append(Html.fromHtml(String.format(getString(R.string.http_server_log_tips), url)));
        }


        if (NetworkHelper.isHttpserverRunning) {
            httpServerCheckBox.setChecked(true);
            httpServerHead.setVisibility(View.VISIBLE);
        } else {
            httpServerCheckBox.setChecked(false);
        }
        httpServerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(TinyServiceActivity.this,
                            HttpService.class));
                    httpServerHead.setVisibility(View.VISIBLE);
                    Animation animation = new AlphaAnimation(0.0f, 1.0f);
                    animation.setDuration(500);
                    httpServerHead.startAnimation(animation);
                } else {
                    stopService(new Intent(TinyServiceActivity.this, HttpService.class));
                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setDuration(500);
                    httpServerHead.startAnimation(animation);
                    httpServerHead.setVisibility(View.GONE);
                }
            }
        });


    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!NetworkHelper.isHttpserverRunning){

        }
    }

    @OnClick(R.id.httpserver_clone_btn)
    public void onViewClicked() {

//        Intent intent = new Intent(this, HttpClone.class);
//        startActivity(intent);
    }
}
