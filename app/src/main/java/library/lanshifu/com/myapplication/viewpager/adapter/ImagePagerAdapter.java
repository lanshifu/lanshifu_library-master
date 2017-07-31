package library.lanshifu.com.myapplication.viewpager.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import library.lanshifu.com.myapplication.R;

/**
 * Created by lanxiaobin on 2017/7/28.
 */

public abstract class ImagePagerAdapter<T> extends PagerAdapter {

    private int layoutId;
    private List<T> mData;
    private Context mContext;
    private SparseArray<View> mViews = new SparseArray<>();

    public ImagePagerAdapter(Context context,int layoutId,List<T> mData) {
        this.layoutId = layoutId;
        this.mData = mData;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = mViews.get(position);
        if(view == null){
            view = View.inflate(mContext,layoutId,null);
        }

        bindView(view,mData,position);
        mViews.put(position,view);
        container.addView(view);
        return view;
    }


    public abstract void bindView(View view,List<T> mData, int position);
}
