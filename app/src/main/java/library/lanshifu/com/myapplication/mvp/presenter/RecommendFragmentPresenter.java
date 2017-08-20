package library.lanshifu.com.myapplication.mvp.presenter;

import library.lanshifu.com.myapplication.model.Recommend;
import library.lanshifu.com.myapplication.mvp.contract.RecommendFragmentContract;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RetrofitHelper;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;

/**
 * Created by Administrator on 2017/8/20.
 */

public class RecommendFragmentPresenter extends RecommendFragmentContract.Presenter {
    @Override
    public void getRecommendInfo() {

        RetrofitHelper.getLiveApi().getRecommend()
                .compose(RxSchedulerHelper.<Recommend>io_main())
                .subscribe(new MyObserver<Recommend>() {
                    @Override
                    public void _onNext(Recommend recommend) {
                        mView.returnRecommenInfos(recommend);
                    }

                    @Override
                    public void _onError(String e) {
                        mView.showError(e);
                    }
                });

    }
}
