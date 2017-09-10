package library.lanshifu.com.myapplication.mvp.presenter;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.MainActivity;
import library.lanshifu.com.myapplication.live.LiveDetailActivity;
import library.lanshifu.com.myapplication.model.Room;
import library.lanshifu.com.myapplication.model.RoomLine;
import library.lanshifu.com.myapplication.mvp.contract.LiveDetailContract;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RetrofitHelper;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;

/**
 * Created by Administrator on 2017/8/21.
 */

public class LiveDetailPresenter extends LiveDetailContract.Presenter {

    private MyObserver<Room> observer;

    @Override
    public void enterRoom(String uid, final boolean isShowing) {

        /**
         * 5秒发一次
         */
        Observable.interval(5, TimeUnit.SECONDS)
                .compose(RxSchedulerHelper.<Long>io_main())
                .compose(((LiveDetailActivity)(mView)).<Long>bindToLifecycle())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //                        mView.playUrl();
        observer = new MyObserver<Room>() {
            @Override
            public void _onNext(Room room) {
                RoomLine ws = room.getLive().getWs();
//                        mView.playUrl();
                RoomLine roomLine = room.getLive().getWs();

                RoomLine.FlvBean flv = roomLine.getFlv();
                String url = "";
                if (flv != null) {
                    url = flv.getValue(isShowing).getSrc();
                } else {
                    url = roomLine.getHls().getValue(isShowing).getSrc();
                }
                mView.playUrl(url);
            }

            @Override
            public void _onError(String e) {
                mView.showError(e);

            }
        };
        RetrofitHelper.getLiveApi().enterRoom(uid)
                .compose(RxSchedulerHelper.<Room>io_main())
                .compose(((LiveDetailActivity)(mView)).<Room>bindToLifecycle())
                .subscribe(observer);

    }
}
