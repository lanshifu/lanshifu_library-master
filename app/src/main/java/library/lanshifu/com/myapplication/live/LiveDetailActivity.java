package library.lanshifu.com.myapplication.live;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import butterknife.Bind;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.mvp.contract.LiveDetailContract;
import library.lanshifu.com.myapplication.mvp.model.LiveDetailModel;
import library.lanshifu.com.myapplication.mvp.presenter.LiveDetailPresenter;

public class LiveDetailActivity extends BaseActivity<LiveDetailPresenter,LiveDetailModel> implements LiveDetailContract.View{


    @Bind(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;


    @Override
    public int getLayoutId() {
        return R.layout.activity_live_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this,mModle);
    }

    @Override
    protected void initView() {
        String uid = getIntent().getStringExtra("uid");
        mPresenter.enterRoom(uid,false);
    }


    @Override
    public void showProgressDialog(String text) {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showError(String error) {
        showShortToast(error);

    }

    @Override
    public void playUrl(String url) {

        Loge("url = "+url);
        videoplayer.setUp(url,JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");

    }
}
