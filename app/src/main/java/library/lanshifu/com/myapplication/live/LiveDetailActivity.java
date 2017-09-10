package library.lanshifu.com.myapplication.live;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.mvp.contract.LiveDetailContract;
import library.lanshifu.com.myapplication.mvp.model.LiveDetailModel;
import library.lanshifu.com.myapplication.mvp.presenter.LiveDetailPresenter;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;

public class LiveDetailActivity extends BaseActivity<LiveDetailPresenter, LiveDetailModel> implements LiveDetailContract.View {


    @Bind(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;


    @Override
    public int getLayoutId() {
        return R.layout.activity_live_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModle);
    }

    @Override
    protected void initView() {
        String uid = getIntent().getStringExtra("uid");
        mPresenter.enterRoom(uid, false);


//        Observable.interval(5, TimeUnit.SECONDS)
//                .compose(RxSchedulerHelper.<Long>io_main())
//                .compose(this.<Long>bindToLifecycle())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(@NonNull Long aLong) throws Exception {
//                        T.showShort("延迟5秒后收到");
//                    }
//                });


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

        Loge("url = " + url);
        videoplayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");

    }
}
