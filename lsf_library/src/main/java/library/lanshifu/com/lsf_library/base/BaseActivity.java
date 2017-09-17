package library.lanshifu.com.lsf_library.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Window;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.R;
import library.lanshifu.com.lsf_library.baseapp.AppManager;
import library.lanshifu.com.lsf_library.basemvp.BaseModle;
import library.lanshifu.com.lsf_library.basemvp.BasePresenter;
import library.lanshifu.com.lsf_library.baserx.RxManager;
import library.lanshifu.com.lsf_library.commwidget.LoadingDialog;
import library.lanshifu.com.lsf_library.commwidget.StatusBarCompat;
import library.lanshifu.com.lsf_library.commwidget.toast.TopToast;
import library.lanshifu.com.lsf_library.daynightmodeutils.ChangeModeController;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.lsf_library.utils.TUtil;

/**
 * 基类
 */
/***************使用例子*********************/
//1.mvp模式
//public class SampleActivity extends BaseActivity<NewsChanelPresenter, NewsChannelModel>implements NewsChannelContract.View {
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_news_channel;
//    }
//
//    @Override
//    public void initPresenter() {
//        mPresenter.setVM(this, mModel);
//    }
//
//    @Override
//    public void initView() {
//    }
//}
//2.普通模式
//public class SampleActivity extends BaseActivity {
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_news_channel;
//    }
//
//    @Override
//    public void initPresenter() {
//    }
//
//    @Override
//    public void initView() {
//    }
//}

/**
 * Created by 蓝师傅 on 2016/12/24.
 */
public abstract class BaseActivity<P extends BasePresenter, M extends BaseModle> extends RxAppCompatActivity{


    public P mPresenter;
    public M mModle;
    public Context mContext;
    public RxManager mRxManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(getLayoutId());
        doAfterSetContentView();
        ButterKnife.bind(this);
        mRxManager = new RxManager();
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModle = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }



        initPresenter();
        initView();


    }

    /**
     *
     */
    protected  void doAfterSetContentView(){}


    /**
     * 设置layout前配置
     */
    protected void doBeforeSetcontentView() {

        //设置昼夜主题
        initTheme();
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 默认着色状态栏
        SetStatusBarColor();


    }


    /**
     * 设置主题
     */
    private void initTheme() {
        ChangeModeController.setTheme(this, R.style.DayTheme, R.style.NightTheme);
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(){
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.main_color));
    }
    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color){
        StatusBarCompat.setStatusBarColor(this,color);
    }
    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar(){
        StatusBarCompat.translucentStatusBar(this);
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
        intent.setClass(this, cls);
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
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void addFragment(int container, Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(container,fragment);
        ft.commit();
    }


    /**
     * 开启浮动加载进度条
     */
    public void startProgressDialog() {
        LoadingDialog.showDialogForLoading(this);
    }

    /**
     * 开启浮动加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
        LoadingDialog.showDialogForLoading(this, msg, true);
    }

    /**
     * 停止浮动加载进度条
     */
    public void stopProgressDialog() {
        LoadingDialog.cancelDialogForLoading();
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    public void showShortToast(String text) {
//        T.showShort(text);
        TopToast.with(this)
                .setMessage(text,R.color.black)
                .setDuration(2000)
                .setIcon(R.drawable.ic_success,R.color.green)
                .setBackgroundColor(R.color.white)
                .show();
    }

    public void showErrorToast(String text){
        TopToast.with(this)
                .setMessage(text)
                .setIcon(R.drawable.ic_error)
                .setDuration(3000)
                .setBackgroundColor(R.color.red)
                .show();
    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    public void showLongToast(String text) {
        T.showLong(text);
    }

    protected void Loge(String text){
        L.e(text);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        mRxManager.clear();
        if(mPresenter!=null){
            mPresenter.destory();
        }
    }



    /*********************
     * 子类实现
     *****************************/
    protected abstract int getLayoutId();

    protected  void initPresenter(){}

    protected abstract void initView();
}
