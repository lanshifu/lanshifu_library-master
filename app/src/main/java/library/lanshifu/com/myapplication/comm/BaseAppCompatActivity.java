package library.lanshifu.com.myapplication.comm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.comm.search.SearchActivity;

/**
 * Created by 蓝师傅 on 2016/12/8.
 */

public abstract class BaseAppCompatActivity extends ToolBarActivity{

    // getContentView或getmContentView返回的布局视图
    private View mView;

    // 搜索菜单的id号
    private int searchId;
    // 搜索Activity的requestCode
    public static final int SEARCH_CODE = 999;

    @Override
    protected boolean isTopActivity() {
        return false;
    }

    @Override
    protected boolean onIfShowTB() {
        return true;
    }



    @Override
    protected void onViewCreated() {
        if(onGetTBMenuId() == 0){
            searchId = addTBMenuItem("search",getResources().getDrawable(R.drawable.ic_search_white), MenuItem.SHOW_AS_ACTION_ALWAYS, 5);
        }
        initView();

    }







    @Override
    protected int onGetTBMenuId() {
        return 0;
    }

    @Override
    protected View onCreateView() {
        int id = getContentView();
        View view = getmContentView();
        if( view != null){
            mView = view;
            return mView;
        }
        else if (id > 0) {
            mView = getLayoutInflater().inflate(id, null);
            return mView;
        }
        return null;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == searchId) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(SearchActivity.EXTRA_ACTIVITY_NAME, this.getClass().getSimpleName());
            startActivityForResult(intent,SEARCH_CODE);
        }
//        else if(item.getItemId() == moreId){
//            MorePopupWindowWrap popupWindow = new MorePopupWindowWrap(this, mMoreTitles);
//            popupWindow.setOnMenuClickListener(this);
//            popupWindow.showAsDropDown(getTB().findViewById(moreId));
//        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 返回一个布局id
     *
     * @return 布局id
     */
    protected abstract int getContentView();

    /**
     * 返回一个显示在布局中的View，当重写此方法时getContentView将失效
     *
     * @return View
     */
    protected View getmContentView(){
        return null;
    }

    /**
     * 在这里进行findViewById时, 使用getView().findViewById()会更好
     */
    protected abstract void initView();

    /**
     * 根据getContentView返回的布局inflate出来的视图
     *
     * @return 视图
     */
    protected View getView(){
        return mView;
    }


    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void showLoadingDialog() {
        showLoadingDialog(true);
    }


    public void showLoadingDialog(boolean cancleable) {
        showLoadingDialog(cancleable,null);

    }

    public void showLoadingDialog(boolean cancleable ,DialogInterface.OnCancelListener cancelListener) {
        // 如果当前Activity已经finish掉了，就不要再显示dialog了，不然会崩溃的
        if (this.isFinishing()) {
            return;
        }



    }

}
