package library.lanshifu.com.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.commwidget.IDrawerLayout;

public class MainActivity extends BaseToolBarActivity {


    @Bind(R.id.iDrawerLayout)
    IDrawerLayout iDrawerLayout;



    private String doSomeThing() {
        return "doSomeThing";
    }

//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_main;
//    }


    @Override
    protected int getLayoutid() {
        return  R.layout.activity_main;
    }

    @Override
    protected void onViewCreate() {
        setTBTitle("标题");
        hideTBBack();
//        mIv_menu.setVisibility(View.VISIBLE);
//        mIv_menu.setImageResource(R.mipmap.icon_menu2);

        getTBBackView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchDrawer();
            }
        });

    }

    @Override
    protected void initView() {

        iDrawerLayout.inflateMenu(R.menu.drawe);
//        iDrawerLayout.addNavigationViewHeader(R.layout.header);
        iDrawerLayout.setOnNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                iDrawerLayout.closeDrawer();

                switch (item.getItemId()) {
                    case R.id.first:
                        showShortToast("点击了首页");
                        return true;

                    case R.id.other:
                        showShortToast("点击了其他");
                        return true;

                    case R.id.about:
                        showShortToast("about");
                        return true;

                }
                return false;
            }
        });


        iDrawerLayout.switchContentFragment(new MainFragment());
    }


    public void switchDrawer(){
        iDrawerLayout.switchDrawer();
    }
}
