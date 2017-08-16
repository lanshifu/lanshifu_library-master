package library.lanshifu.com.myapplication.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.utils.PrefUtil;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.WechatItem;
import library.lanshifu.com.myapplication.widget.Html5WebView;

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

    @Override
    protected void initView() {
        mlistBean = getIntent().getParcelableExtra("listBean");

        initToolbar();
        initDataByGetIntent();

        webviewWechat.loadUrl(mlistBean.getUrl());
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



}
