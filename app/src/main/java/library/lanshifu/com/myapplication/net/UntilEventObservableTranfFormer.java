package library.lanshifu.com.myapplication.net;

import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;


/**
 * Created by lanshifu on 2017/9/10.
 */

 public class UntilEventObservableTranfFormer<T,R> implements ObservableTransformer<T,R> {



    @Override
    public ObservableSource<R> apply(@NonNull Observable<T> observable) {
        return null;
    }
}
