package library.lanshifu.com.myapplication.mvp.contract;

import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.basemvp.BaseModle;
import library.lanshifu.com.lsf_library.basemvp.BasePresenter;
import library.lanshifu.com.lsf_library.basemvp.BaseView;
import library.lanshifu.com.myapplication.model.Recommend;

/**
 * Created by Administrator on 2017/8/20.
 */

public interface RecommendFragmentContract {
    interface Model extends BaseModle{
    }

    interface View extends BaseView{

       void returnRecommenInfos(Recommend recommend);
    }

    abstract class Presenter extends BasePresenter<View, BaseModle> {
        public abstract void getRecommendInfo();
    }
}
