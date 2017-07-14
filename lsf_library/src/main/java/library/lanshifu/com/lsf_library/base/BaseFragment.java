package library.lanshifu.com.lsf_library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.basemvp.BaseModle;
import library.lanshifu.com.lsf_library.basemvp.BasePresenter;
import library.lanshifu.com.lsf_library.baserx.RxManager;
import library.lanshifu.com.lsf_library.commwidget.LoadingDialog;
import library.lanshifu.com.lsf_library.utils.TUtil;

/**
 * Created by 蓝师傅 on 2017/1/2.
 */

public abstract class BaseFragment<P extends BasePresenter, M extends BaseModle> extends Fragment{

    private View mRootView;
    private RxManager mRxManage;
    private P mPresenter;
    private M mModle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mRootView ==null){
            mRootView = inflater.inflate(getLayoutId(),container,false);
        }
        ButterKnife.bind(mRootView);
        mRxManage = new RxManager();
        mPresenter = TUtil.getT(this,0);
        mModle = TUtil.getT(this,1);
        if(mPresenter !=null){
            mPresenter.mContext = this.getActivity();
        }


        return mRootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initPresenter();
        initView();
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }



    /**
     * 开启浮动加载进度条
     */
    public void startProgressDialog() {
        LoadingDialog.showDialogForLoading(getActivity());
    }

    /**
     * 开启浮动加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
        LoadingDialog.showDialogForLoading(getActivity(), msg, true);
    }

    /**
     * 停止浮动加载进度条
     */
    public void stopProgressDialog() {
        LoadingDialog.cancelDialogForLoading();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRxManage.clear();
        if(mPresenter!=null){
            mPresenter.destory();
        }

    }

    /**
     * 子类去实现
     * @return
     */
    protected abstract int getLayoutId();

    protected abstract void initPresenter();

    protected abstract void initView();
}
