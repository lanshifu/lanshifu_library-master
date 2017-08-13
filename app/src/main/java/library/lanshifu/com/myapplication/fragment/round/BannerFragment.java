package library.lanshifu.com.myapplication.fragment.round;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.myapplication.R;

/**
 * Created by asus on 2016/8/28.
 */
public class BannerFragment extends BaseFragment {

    @Bind(R.id.banner_iv_bg)
    ImageView imageView;
    private int imgRes;

    public BannerFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_banner;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

        imageView.setBackgroundResource(imgRes);

    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }


}
