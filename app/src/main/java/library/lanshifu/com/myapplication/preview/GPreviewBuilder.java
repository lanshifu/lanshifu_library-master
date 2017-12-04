package library.lanshifu.com.myapplication.preview;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


import java.util.ArrayList;

import library.lanshifu.com.myapplication.preview.enitity.IThumbViewInfo;
import library.lanshifu.com.myapplication.preview.view.BasePhotoFragment;

/**
 * Created by yangc on 2017/9/12.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public final class GPreviewBuilder {
    private Activity mContext;
    private Intent intent;
    private Class className;

    private GPreviewBuilder(@NonNull Activity activity) {
        mContext = activity;
        intent = new Intent();
    }

    /***
     * 设置开始启动预览
     * @param activity  启动
     *@return     GPreviewBuilder
     * **/
    public static GPreviewBuilder from(@NonNull Activity activity) {
        return new GPreviewBuilder(activity);
    }

    /***
     * 设置开始启动预览
     * @param fragment  启动
     *@return     GPreviewBuilder
     * **/
    public static GPreviewBuilder from(@NonNull Fragment fragment) {
        return new GPreviewBuilder(fragment.getActivity());
    }

    /****
     *自定义预览activity 类名
     * @param className   继承GPreviewActivity
     *@return     GPreviewBuilder
     * **/
    public GPreviewBuilder to(@NonNull Class className) {
        this.className = className;
        intent.setClass(mContext, className);
        return this;
    }

    /***
     * 设置数据源
     * @param imgUrls 数据
     *@param   <T>    你的实体类类型
     * @return GPreviewBuilder
     * **/
    public <T extends IThumbViewInfo> GPreviewBuilder setData(@NonNull ArrayList<T> imgUrls) {
        intent.putParcelableArrayListExtra("imagePaths", imgUrls);
        return this;
    }

    /***
     * 设置数据源
     * @param className 你的Fragment类
     * @return GPreviewBuilder
     * **/
    public GPreviewBuilder setUserFragment(@NonNull Class<? extends BasePhotoFragment> className) {
        intent.putExtra("className", className);
        return this;
    }

    /***
     * 设置默认索引
     * @param currentIndex 数据
     * @return GPreviewBuilder
     * **/
    public GPreviewBuilder setCurrentIndex(int currentIndex) {
        intent.putExtra("position", currentIndex);
        return this;
    }

    /***
     * 设置指示器类型
     * @param indicatorType 枚举
     * @return GPreviewBuilder
     * **/
    public GPreviewBuilder setType(@NonNull IndicatorType indicatorType) {
        intent.putExtra("type", indicatorType);
        return this;
    }

    /***
     * 设置图片禁用拖拽返回
     * @param isDrag  true  可以 false 默认 true
     * @return GPreviewBuilder
     * **/
    public GPreviewBuilder setDrag(boolean isDrag) {
        intent.putExtra("isDrag", isDrag);
        return this;
    }

    /***
     * 设置超出内容点击退出（黑色区域）
     * @param isSingleFling  true  可以 false
     * @return GPreviewBuilder
     * **/
    public GPreviewBuilder setSingleFling(boolean isSingleFling) {
        intent.putExtra("isSingleFling", isSingleFling);
        return this;
    }

    /***
     * 启动
     * **/
    public void start() {
        if (className == null) {
            intent.setClass(mContext, GPreviewActivity.class);
        } else {
            intent.setClass(mContext, className);
        }
        mContext.startActivity(intent);
        intent = null;
        mContext = null;
    }

    public enum IndicatorType {
        Dot, Number
    }
}
