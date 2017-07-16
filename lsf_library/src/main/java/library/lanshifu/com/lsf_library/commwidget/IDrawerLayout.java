package library.lanshifu.com.lsf_library.commwidget;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import library.lanshifu.com.lsf_library.R;

/**
 * 抽屉
 * Created by lWX385269 on 2017/1/5.
 */

public class IDrawerLayout extends FrameLayout {

    private DrawerLayout mDrawerLayout;
    private FrameLayout mContent;
    private NavigationView mNavigationView;
    private Context mContext;
    private ActionBarDrawerToggle drawerToggle;


    public IDrawerLayout(Context context) {
        this(context, null);
    }

    public IDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        View rootView = LayoutInflater.from(context).inflate(R.layout.base_drawer_layout, null);
        mDrawerLayout = rootView.findViewById(R.id.drawerLayout);
        mContent = rootView.findViewById(R.id.content);
        mNavigationView = rootView.findViewById(R.id.navigationView);

        addView(rootView);
    }

    /**
     * 加载侧滑菜单
     *
     * @param menuId
     */
    public void inflateMenu(int menuId) {
        mNavigationView.inflateMenu(menuId);
    }

    /**
     * 切换主体fragment
     *
     * @param fragment
     */
    public void switchContentFragment(Fragment fragment) {

        if (mContext instanceof FragmentActivity) {
            FragmentTransaction fragmentTransaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment).commit();
            ((AppCompatActivity) mContext).invalidateOptionsMenu();
        } else {
            try {
                throw new GirlNotFoundException("当前activity继承FragmentActivity");
            } catch (GirlNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 主体除了fragment，也可以是一个View
     *
     * @param resId
     */
    public void setContentView(int resId) {
        View contentView = LayoutInflater.from(mContext).inflate(resId, null);
        setContentView(contentView);

    }


    public void setContentView(View contentView) {
        this.removeAllViews();
        addView(contentView);
    }

    /**
     * 添加侧滑菜单header
     *
     * @param resId
     */
    public void addNavigationViewHeader(int resId) {
        View headerView = LayoutInflater.from(mContext).inflate(resId, null);
        addNavigationViewHeader(headerView);

    }

    public void addNavigationViewHeader(View headerView) {
        mNavigationView.addHeaderView(headerView);
    }


    /**
     * 侧滑菜单点击事件
     *
     * @param listener
     */
    public void setOnNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener listener) {
        mNavigationView.setNavigationItemSelectedListener(listener);
    }


    /**
     * 初始化抽屉开关，activity提供此方法供fragment调用
     *
     * @param toolbar
     */
    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            drawerToggle = new ActionBarDrawerToggle((Activity) mContext, mDrawerLayout, toolbar, R.string.open, R.string.close);
            drawerToggle.syncState();
            mDrawerLayout.addDrawerListener(drawerToggle);
        }
    }


    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mNavigationView);
    }

    public void openDrawer(){
        mDrawerLayout.openDrawer(mNavigationView);
    }

    public boolean isDrawerOpen(){
       return mDrawerLayout.isDrawerOpen(mNavigationView);
    }

    public void switchDrawer(){
        if(mDrawerLayout.isDrawerOpen(mNavigationView)){
            mDrawerLayout.closeDrawer(mNavigationView);
        }else {
            mDrawerLayout.openDrawer(mNavigationView);
        }
    }

    class GirlNotFoundException extends Exception {
        String msg;

        public GirlNotFoundException(String msg) {
            this.msg = msg;
        }

        @Override
        public String getMessage() {
            return msg;
        }
    }

}
