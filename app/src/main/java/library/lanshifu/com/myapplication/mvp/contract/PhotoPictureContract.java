package library.lanshifu.com.myapplication.mvp.contract;

import library.lanshifu.com.lsf_library.basemvp.BaseModle;
import library.lanshifu.com.lsf_library.basemvp.BasePresenter;
import library.lanshifu.com.lsf_library.basemvp.BaseView;

/**
 * Created by lanxiaobin on 2017/8/24.
 */

public interface PhotoPictureContract {
    interface Model {
    }

    interface View extends BaseView{

    }

    abstract class Presenter extends BasePresenter<View,BaseModle>{
    }
}
