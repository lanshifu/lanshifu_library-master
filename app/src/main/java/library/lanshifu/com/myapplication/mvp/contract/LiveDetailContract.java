package library.lanshifu.com.myapplication.mvp.contract;

import library.lanshifu.com.lsf_library.basemvp.BaseModle;
import library.lanshifu.com.lsf_library.basemvp.BasePresenter;
import library.lanshifu.com.lsf_library.basemvp.BaseView;

/**
 * Created by Administrator on 2017/8/21.
 */

public interface LiveDetailContract {
    interface Model extends BaseModle{
    }

    interface View extends BaseView{
        void playUrl(String url);
    }

    abstract class Presenter extends BasePresenter<View,Model>{
        public abstract void enterRoom(String uid,final boolean isShowing);
    }
}
