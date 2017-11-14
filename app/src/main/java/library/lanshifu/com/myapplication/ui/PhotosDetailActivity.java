package library.lanshifu.com.myapplication.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.commwidget.StatusBarCompat;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.utils.MyUtils;
import library.lanshifu.com.myapplication.widget.PullBackLayout;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by lanshifu on 2017/11/14.
 */

public class PhotosDetailActivity extends AppCompatActivity implements PullBackLayout.Callback{
    @Bind(R.id.photoView)
    PhotoView photoView;
    @Bind(R.id.pull_back_layout)
    PullBackLayout pullBackLayout;

    private ColorDrawable mBackground;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_photodetail);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        pullBackLayout.setCallback(this);

        initBackground();

        Glide.with(this)
                .load("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3689295147,509373218&fm=26&gp=0.jpg")
                .into(photoView);
    }

    private void initBackground() {
        mBackground = new ColorDrawable(Color.BLACK);
        MyUtils.getRootView(this).setBackgroundDrawable(mBackground);
    }

    /**** 监听******************/
    @Override
    public void onPullStart() {

        L.d("onPullStart");
    }

    @Override
    public void onPull(float progress) {
        progress = Math.min(1f, progress * 3f);
        mBackground.setAlpha((int) (0xff/*255*/ * (1f - progress)));
    }

    @Override
    public void onPullCancel() {
        L.d("onPullCancel");
    }

    @Override
    public void onPullComplete() {
        //
        L.d("onPullComplete");
        supportFinishAfterTransition();
    }

    //动画结束之后finish
    @Override
    public void supportFinishAfterTransition() {
        super.supportFinishAfterTransition();
    }
}
