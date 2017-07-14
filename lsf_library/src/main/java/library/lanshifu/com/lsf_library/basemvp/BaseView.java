package library.lanshifu.com.lsf_library.basemvp;

/**
 * Created by 蓝师傅 on 2016/12/28.
 */

public interface BaseView {

    void showProgressDialog(String text);

    void hideProgressDialog();

    void showError(String error);

}
