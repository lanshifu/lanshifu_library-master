package library.lanshifu.com.myapplication.mvp.contract;

import java.util.List;

import library.lanshifu.com.lsf_library.basemvp.BaseModle;
import library.lanshifu.com.lsf_library.basemvp.BasePresenter;
import library.lanshifu.com.lsf_library.basemvp.BaseView;
import library.lanshifu.com.myapplication.model.LiveCategory;

/**
 * Created by Administrator on 2017/8/19.
 */

public interface LiveListFragmentContract {


    interface View extends BaseView {

        void returnLiveList(List<LiveCategory> liveCategoryList);
    }

    abstract class Presenter extends BasePresenter<View, BaseModle> {

        public abstract void getLiveList();

    }
}
