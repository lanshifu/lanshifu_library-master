package library.lanshifu.com.myapplication.hongyang;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;

/**
 * Created by lanxiaobin on 2017/9/5.
 */

public class VRActivity extends BaseToolBarActivity {
    @Bind(R.id.mVrPanoramaView)
    VrPanoramaView mVrPanoramaView;
    private VrPanoramaView.Options options;
    private String[] paths;
    private int currentPosition = 0;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_google_vr;
    }

    @Override
    protected void onViewCreate() {

        initPaths();

        options = new VrPanoramaView.Options();
        options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;

        mVrPanoramaView.setInfoButtonEnabled(true);//设置最左边信息的按钮
        mVrPanoramaView.setStereoModeButtonEnabled(false);//设置立体模型按钮
        mVrPanoramaView.setEventListener(new AvtivityEventListener());//监听
        //加载本地图片
//        mVrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeResource(
//                getResources(), R.mipmap.ic_launcher), options);
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504589803317&di=d4cd4ee8cecf3e7e1ec3412967862a68&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D6f92f77c094f78f0940692b011586020%2Ff636afc379310a552766b7dcbd4543a982261053.jpg";
        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mVrPanoramaView.loadImageFromBitmap(resource, options);
                    }
                });

    }

    private void initPaths() {
        paths = new String[]{
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504590773598&di=b22cf79a1add3f06a160db6f61af90e3&imgtype=0&src=http%3A%2F%2Fimages.199u2.com%2Fdata%2Fattachment%2Fforum%2F201509%2F14%2F205055qbfybztqff2bd2f9.jpg"
                , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504590773598&di=6131a3ff80007cb892060e5146d63646&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D5ceb022a8d44ebf8797c6c7cb190bd5f%2F1b4c510fd9f9d72a57d22b34de2a2834349bbb60.jpg"
                , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504590773599&di=4d3999a345a9968fc52d87fa164b6b6c&imgtype=0&src=http%3A%2F%2Fphotos.tuchong.com%2F1057750%2Ff%2F14373493.jpg"
                , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504590927290&di=2df566765206d3265852da8337c9465a&imgtype=0&src=http%3A%2F%2Fcyjctrip.qiniudn.com%2F1377828330%2F438FD2BD-B990-408E-AAF9-3D899A1616F5.jpg"
                , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505185433&di=fdcaed4a15688adaf44da822ac4b9305&imgtype=jpg&er=1&src=http%3A%2F%2Fimg8.zol.com.cn%2Fbbs%2Fupload%2F22484%2F22483075.jpg"
        };


    }



    @OnClick(R.id.btn_switch)
    public void onViewClicked() {
        if(currentPosition >= paths.length){
            currentPosition = 0;
        }

        String url = paths[currentPosition];

        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mVrPanoramaView.loadImageFromBitmap(resource, options);
                    }
                });
        currentPosition ++;
    }


    private class AvtivityEventListener extends VrPanoramaEventListener {

        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
            Loge("load success");
        }

        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
            Loge("load error");
        }

        @Override
        public void onClick() {
            super.onClick();
            Loge("onclick");
        }

        @Override
        public void onDisplayModeChanged(int newDisplayMode) {
            super.onDisplayModeChanged(newDisplayMode);
            Loge("onDisplayModeChanged " + newDisplayMode);
        }
    }

}
