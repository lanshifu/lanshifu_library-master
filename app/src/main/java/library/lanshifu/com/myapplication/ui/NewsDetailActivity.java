package library.lanshifu.com.myapplication.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.utils.PrefUtil;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.WechatItem;
import library.lanshifu.com.myapplication.widget.Html5WebView;
import library.lanshifu.com.myapplication.widget.transition.EasyTransition;

public class NewsDetailActivity extends BaseActivity {


    @Bind(R.id.ivImage)
    ImageView ivImage;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.webview_wechat)
    Html5WebView webviewWechat;
    @Bind(R.id.nestedscrollview_wechat)
    NestedScrollView nestedscrollviewWechat;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private WechatItem.ResultBean.ListBean mlistBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    private boolean finishEnter = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAnimIn(savedInstanceState);
    }

    @Override
    protected void initView() {
        mlistBean = getIntent().getParcelableExtra("listBean");

        initToolbar();
        initDataByGetIntent();

        webviewWechat.loadUrl(mlistBean.getUrl());
    }


    private void initAnimIn(Bundle savedInstanceState) {
        // if re-initialized, do not play any anim
        long transitionDuration = 500;
        if (null != savedInstanceState)
            transitionDuration = 0;

        // transition enter
        finishEnter = false;
        EasyTransition.enter(
                this,
                transitionDuration,
                new DecelerateInterpolator(),
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // init other views after transition anim
                        finishEnter = true;
                        initOtherViews();
                    }
                });
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initDataByGetIntent() {
//        boolean isNotLoad = (boolean) PrefUtil.getInstance().getBoolean("notload", false);
        boolean isNotLoad = false;
        if (!isNotLoad) {
            Glide.with(this)
                    .load(mlistBean.getFirstImg())
                    .placeholder(R.drawable.loading_progress)
                    .error(R.mipmap.ic_launcher)
                    .crossFade(1000)
                    .into(ivImage);
        }
        getSupportActionBar().setTitle(mlistBean.getTitle());
    }


    private void initOtherViews() {
        fab.setVisibility(View.VISIBLE);
        fab.setScaleX(0);
        fab.setScaleY(0);
        fab.animate()
                .setDuration(200)
                .scaleX(1)
                .scaleY(1);

        webviewWechat.setVisibility(View.VISIBLE);
        webviewWechat.setAlpha(0);
        webviewWechat.animate()
                .setDuration(200)
                .alpha(1);
    }

    @Override
    public void onBackPressed() {
        if (finishEnter) {
            finishEnter = false;
            startBackAnim();
        }
    }

    private void startBackAnim() {
        // forbidden scrolling
//        sv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
//
//        // start our anim
        fab.animate()
                .setDuration(200)
                .scaleX(0)
                .scaleY(0)
        .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                EasyTransition.exit(NewsDetailActivity.this, 200, new DecelerateInterpolator());
//                        finish();
            }
        });

        webviewWechat.animate()
                .setDuration(300)
                .translationY(1000);

    }


}
