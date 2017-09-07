package library.lanshifu.com.myapplication.net;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lWX385269 lanshifu on 2017/4/13.
 */

public abstract class MyObserver<T> implements Observer<T> {


    @Override
    public void onError(Throwable e) {
        String msg = e.getMessage();

        if (e instanceof UnknownHostException) {
            msg = "没有网络";
        } else if (e instanceof SocketTimeoutException) {
            // 超时
            msg = "请求超时";
        }else{
            msg = "请求失败，请稍后重试...";
        }
        _onError(msg);
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    public abstract void _onNext(T t);

    public abstract void _onError(String e);


}
