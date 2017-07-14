package library.lanshifu.com.lsf_library.base;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import library.lanshifu.com.lsf_library.R;
import library.lanshifu.com.lsf_library.utils.ViewIdGenerator;

/**
 * Created by 蓝师傅 on 2016/12/30.
 */

public  abstract class BaseToolBarActivity extends BaseActivity {

    private Menu mTBMenu;
    private TextView mToolBarTitle;
    private Toolbar mToolbar;

    @Override
    public int getLayoutId() {
        return  R.layout.base_activity;
    }

    @Override
    protected void doAfterSetContentView() {
        mToolBarTitle = (TextView) findViewById(R.id.comm_toolbar_title);// 自定义的标题TextView
        FrameLayout container =  (FrameLayout) findViewById(R.id.comm_container);
        mToolbar = (Toolbar) findViewById(R.id.comm_toolbar);

        if (onIfShowTB()) {
            mToolbar.setVisibility(View.VISIBLE);
            mToolBarTitle.setVisibility(View.VISIBLE);
            initToolBar(mToolbar);
            initContainer(container);

        } else {
            mToolbar.setVisibility(View.GONE);
            mToolBarTitle.setVisibility(View.GONE);
            initContainer(container);
        }
    }


    @Override
    protected void initView() {

    }

    private void initContainer(FrameLayout container) {
        View view = getLayoutInflater().inflate(getLayoutid(),null);
        if(view!=null){
            container.addView(view);
        }else{
            throw new IllegalArgumentException(this.getClass().getSimpleName() + "------getLayoutid 没有返回布局id");
        }
    }


    // ====== ToolBar或者ActionBar的初始化 ======

    private void initToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 为了能够调整标题居中，隐藏actionbar自带的标题，用自己定义的标题TextView
            actionBar.setDisplayShowTitleEnabled(false);
            // 设置显示返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    // 当菜单创建时调用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mTBMenu = menu;
//        mTBMenu.clear();
        if (getTBMenuId() > 0) {
            getMenuInflater().inflate(getTBMenuId(), menu);
        }

        View tbBackView = getTBBackView();
        if( tbBackView!=null){
            tbBackView.setOnClickListener(backListener);
        }
        //菜单加载完再OnVeiwCreate
        onViewCreate();
        return super.onCreateOptionsMenu(menu);
    }


    // 当菜单被选中时调用
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



    /**
     * 得到返回的view
     *
     * @return 返回的视图对象
     */
    protected View getTBBackView() {

        return mToolbar.getChildAt(0);
    }


    /**
     * 隐藏返回
     */
    protected void hideTBBack(){
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null){
            supportActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    /**
     * 显示返回
     */
    protected void showTBBack(){
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected View getTBTitleView() {
        return mToolBarTitle;
    }

    /**
     * 设置标题栏的名字
     *
     * @param title 标题栏的名字
     */
    protected void setTBTitle(CharSequence title) {
        mToolBarTitle.setText(title);
    }

    protected Menu getTBMenu() {
        return mTBMenu;
    }

    protected MenuItem getTBMenuItem(int menuItemId) {
        return mTBMenu != null ? mTBMenu.findItem(menuItemId) : null;
    }


    /**
     * 获取右边菜单中对应menuItemId的视图，可以用来做很多事呀
     *
     * @param menuItemId menu.xml中对应的id
     * @return 对应menuItemId的视图对象
     */
    protected View getTBMenuItemActionView(int menuItemId) {
        MenuItem tbMenuItem = getTBMenuItem(menuItemId);
        return tbMenuItem != null ? MenuItemCompat.getActionView(tbMenuItem) : null;
    }

    protected int addTBMenuItem(String name, Drawable icon, int actionEnum, View actionView, int order) {
        if (mTBMenu != null) {
            MenuItem menuItem = mTBMenu.add(0, ViewIdGenerator.generateViewId(), order, name);
            if (icon != null) {
                menuItem.setIcon(icon);
            }
            MenuItemCompat.setShowAsAction(menuItem, actionEnum);

            if (actionView != null) {
                MenuItemCompat.setActionView(menuItem, actionView);
            }
            return menuItem.getItemId();
        }
        return 0;
    }

    protected int addTBMenuItem(String name, Drawable icon, int actionEnum, int order) {
        return addTBMenuItem(name, icon, actionEnum, null, order);
    }

    protected int addTBMenuItem(String name, Drawable icon, int actionEnum) {
        return addTBMenuItem(name, icon, actionEnum, null, 0);
    }

    protected int addTBMenuItem(String name, int actionEnum, int order) {
        return addTBMenuItem(name, null, actionEnum, null, order);
    }

    /**
     *
     * @param name
     * @param actionEnum  see  MenuItemCompat.SHOW_AS_ACTION_ALWAYS
     * @return
     */
    protected int addTBMenuItem(String name, int actionEnum) {
        return addTBMenuItem(name, null, actionEnum, null, 0);
    }


    protected void hideTBMenuItem(int menuId){
        MenuItem tbMenuItem = getTBMenuItem(menuId);
        tbMenuItem.setVisible(false);
    }

    protected void showTBMenuItem(int menuId){
        MenuItem tbMenuItem = getTBMenuItem(menuId);
        tbMenuItem.setVisible(true);
    }

    /**
     * 动态添加菜单在右边的展开列表中，其回调事件要重写{@code AppCompatActivity.onOptionsItemSelected()}方法，根据菜单名来回调。<br/>
     * 注意：菜单名不能重复
     *
     * @param menulist 菜单列表数组
     */
    protected void addTBMore(String[] menulist) {
        int length = menulist.length;
        for (int i = 0; i < length; i++) {
            addTBMenuItem(menulist[i], null, MenuItemCompat.SHOW_AS_ACTION_NEVER);
        }
    }

    protected void addCustomerViewToTB(View view){
        FrameLayout tbContainer = (FrameLayout) mToolbar.getParent();
        tbContainer.addView(view);
    }




    // 当按返回时调用
    private View.OnClickListener backListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    //======ToolBar的操作方法：按从左到右的顺序排列，即返回、标题、菜单======

    protected Toolbar getTB() {
        return mToolbar;
    }

    protected int getTBHeight(){
        return mToolbar.getHeight();
    }

    /**
     * 隐藏ToolBar
     */
    protected void hideTB(){
        mToolbar.setVisibility(View.GONE);
        mToolBarTitle.setVisibility(View.GONE);
    }

    /**
     * 显示ToolBar
     */
    protected void showTB(){
        mToolbar.setVisibility(View.VISIBLE);
        mToolBarTitle.setVisibility(View.VISIBLE);
    }

    //======子类需要实现的方法======

    /**
     * 是否显示toolbar
     * @return
     */
    protected  boolean onIfShowTB(){
        return true;
    }

    /**
     * 布局id
     * @return
     */
    protected abstract int getLayoutid();


    /**
     * View 初始化完成了
     */
    protected abstract void onViewCreate();

    /**
     * 如果要设置右边三个点的菜单，重写并返回一个新的menuId
     *
     * @return 新的menuId，只有大于0时才会去使用返回的menu资源id，即如果返回小于0的数就能隐藏掉设置的Menu
     */
    protected  int getTBMenuId(){
        return 0;
    }


    protected void setFragment(Fragment fragment){
        setFragment(R.id.comm_container,fragment);
    }

    protected void setFragment(int layoutId, Fragment fragment){
        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        f.add(layoutId,fragment);
        f.commit();

    }

}
