package library.lanshifu.com.myapplication.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.preview.GPreviewActivity;
import library.lanshifu.com.myapplication.preview.enitity.IThumbViewInfo;
import library.lanshifu.com.myapplication.preview.wight.SmoothImageView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by lanshifu on 2017/12/3.
 */

public class BasePhotoFragment extends Fragment {

    /**
     * 预览图片 类型
     */
    private static final String KEY_TRANS_PHOTO = "is_trans_photo";
    private static final String KEY_SING_FILING = "isSingleFling";
    private static final String KEY_PATH = "key_item";
    private static final String KEY_DRAG = "isDrag";
    private IThumbViewInfo beanViewInfo;
    private boolean isTransPhoto = false;
    protected SmoothImageView imageView;
    private View rootView;
    private ProgressBar loading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_photo_layout, container, false);
    }

    public static BasePhotoFragment getInstance(Class<? extends BasePhotoFragment> fragmentClass, IThumbViewInfo item, boolean currentIndex, boolean isSingleFling,boolean isDrag) {
        BasePhotoFragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (Exception e) {
            fragment = new BasePhotoFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BasePhotoFragment.KEY_PATH, item);
        bundle.putBoolean(BasePhotoFragment.KEY_TRANS_PHOTO, currentIndex);
        bundle.putBoolean(BasePhotoFragment.KEY_SING_FILING, isSingleFling);
        bundle.putBoolean(BasePhotoFragment.KEY_DRAG, isDrag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initDate();
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        loading = (ProgressBar) view.findViewById(R.id.loading);
        imageView = (SmoothImageView) view.findViewById(R.id.photoView);
        rootView = view.findViewById(R.id.rootView);
        rootView.setDrawingCacheEnabled(false);
        imageView.setDrawingCacheEnabled(false);


    }

    /**
     * 初始化数据
     */
    private void initDate() {
        Bundle bundle = getArguments();
        boolean isSingleFling = true;
        if (bundle != null) {
            isSingleFling = bundle.getBoolean(KEY_SING_FILING);
            //地址
            beanViewInfo = bundle.getParcelable(KEY_PATH);
            //位置
            assert beanViewInfo != null;
            imageView.setThumbRect(beanViewInfo.getBounds());
            imageView.setDrag(bundle.getBoolean(KEY_DRAG));
            imageView.setTag(beanViewInfo.getUrl());
            //是否展示动画
            isTransPhoto = bundle.getBoolean(KEY_TRANS_PHOTO, false);
            //加载原图
            Glide.with(getActivity())
                    .load(beanViewInfo.getUrl())
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }
        // 非动画进入的Fragment，默认背景为黑色
        if (!isTransPhoto) {
            rootView.setBackgroundColor(Color.BLACK);
        } else {
            imageView.setMinimumScale(0.6f);
        }
        if (isSingleFling) {
            imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (imageView.checkMinScale()) {
                        ((GPreviewActivity) getActivity()).transformOut();
                    }
                }
            });
        } else {
            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (imageView.checkMinScale()) {
                        ((GPreviewActivity) getActivity()).transformOut();
                    }
                }

            });
        }
        imageView.setAlphaChangeListener(new SmoothImageView.OnAlphaChangeListener() {
            @Override
            public void onAlphaChange(int alpha) {
                rootView.setBackgroundColor(getColorWithAlpha(alpha / 255f, Color.BLACK));
            }
        });
        imageView.setTransformOutListener(new SmoothImageView.OnTransformOutListener() {
            @Override
            public void onTransformOut() {
                if (imageView.checkMinScale()) {
                    ((GPreviewActivity) getActivity()).transformOut();
                }
            }
        });
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    public void transformIn() {
        imageView.transformIn(new SmoothImageView.onTransformListener() {
            @Override
            public void onTransformCompleted(SmoothImageView.Status status) {
                rootView.setBackgroundColor(Color.BLACK);
            }
        });
    }

    public void transformOut(SmoothImageView.onTransformListener listener) {
        imageView.transformOut(listener);
    }


    public void changeBg(int color) {
        rootView.setBackgroundColor(color);
    }

    public IThumbViewInfo getBeanViewInfo() {
        return beanViewInfo;
    }
}
