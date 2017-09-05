package library.lanshifu.com.lsf_library.baserx;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * 用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
 * Created by xsf
 * on 2016.08.14:50
 */
public class RxManager {
    public RxBus mRxBus = RxBus.getInstance();
    //管理rxbus订阅
    private Map<String, Observable<?>> mObservables = new HashMap<>();
    /*管理Observables 和 Subscribers订阅*/
    private CompositeDisposable mCompositeSubscription = new CompositeDisposable();

    /**
     * RxBus注入监听
     * @param eventName
     * @param action1
     */
    public <T>void on(String eventName, Consumer<T> action1) {
        Observable observable = mRxBus.register(eventName);
        mObservables.put(eventName, observable);
        /*订阅管理*/
        Disposable disposable = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);

//        Observable subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                .subscribe(action1, new Observable<T>() {
//                    @Override
//                    protected void subscribeActual(Observer observer) {
//
//                    }
//                });
        mCompositeSubscription.add(disposable);
    }

    /**
     * 单纯的Observables 和 Subscribers管理
     * @param disposable
     */
    private void add(Disposable disposable) {
        /*订阅管理*/
        mCompositeSubscription.add(disposable);
    }
    /**
     * 单个presenter生命周期结束，取消订阅和所有rxbus观察
     */
    public void clear() {
        mCompositeSubscription.dispose();// 取消所有订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除rxbus观察
        }
    }
    //发送rxbus
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }
}
