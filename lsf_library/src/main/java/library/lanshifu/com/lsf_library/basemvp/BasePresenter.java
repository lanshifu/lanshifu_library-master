package library.lanshifu.com.lsf_library.basemvp;

import android.content.Context;

import library.lanshifu.com.lsf_library.baserx.RxManager;

/**
 * Created by 蓝师傅 on 2016/12/28.
 */

public class BasePresenter<V , M > {

    public Context mContext;
    public V mView;
    public M mModle;
    public RxManager mRxManager = new RxManager();


    public void setVM(V mView,M mModle){
        this.mView = mView;
        this.mModle = mModle;

        start();
    }


    public void start() {
        //可以做一下初始化操作，例如rxbus添加观察者

    }

    public void destory(){
        //做一些释放资源，回收操作,在activity/fragment ondestory中调用
        mRxManager.clear();
    }



}
