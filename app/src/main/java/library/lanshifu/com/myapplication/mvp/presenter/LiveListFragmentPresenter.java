package library.lanshifu.com.myapplication.mvp.presenter;

import java.util.List;

import library.lanshifu.com.myapplication.model.LiveCategory;
import library.lanshifu.com.myapplication.mvp.contract.LiveListFragmentContract;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RetrofitHelper;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;

/**
 * Created by Administrator on 2017/8/19.
 */

public class LiveListFragmentPresenter extends LiveListFragmentContract.Presenter {



    @Override
    public void getLiveList() {


        RetrofitHelper.getLiveApi().getAllCategories()
                .compose(RxSchedulerHelper.<List<LiveCategory>>io_main())
                .subscribe(new MyObserver<List<LiveCategory>>() {
                    @Override
                    public void _onNext(List<LiveCategory> liveCategoryList) {
                        mView.returnLiveList(liveCategoryList);
                    }

                    @Override
                    public void _onError(String e) {
                        mView.showError("出错了："+e);
                    }
                });




    }
}
