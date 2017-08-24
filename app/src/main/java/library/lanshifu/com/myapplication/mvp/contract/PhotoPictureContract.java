package library.lanshifu.com.myapplication.mvp.contract;

import java.util.List;

import library.lanshifu.com.lsf_library.basemvp.BaseModle;
import library.lanshifu.com.lsf_library.basemvp.BasePresenter;
import library.lanshifu.com.lsf_library.basemvp.BaseView;
import library.lanshifu.com.myapplication.imagepicker.PictureBean;

/**
 * Created by lanxiaobin on 2017/8/24.
 */

public interface PhotoPictureContract {
    interface Model extends BaseModle{
    }

    interface View extends BaseView{

        void composeSuccess(List<String> path);
        void upLoadPicSuccess();

    }

    abstract class Presenter extends BasePresenter<View,BaseModle>{

        //上传
        public abstract void upLoadPic(List<PictureBean> pictureBeanList);

        //压缩
        public abstract void compressWithLs(List<String> files);


    }
}
