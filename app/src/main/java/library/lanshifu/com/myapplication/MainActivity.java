package library.lanshifu.com.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.commwidget.IDrawerLayout;

public class MainActivity extends BaseActivity {

    @Bind(R.id.iDrawerLayout)
    IDrawerLayout iDrawerLayout;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
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
                        return true;

                    case R.id.other:
                        return true;

                    case R.id.about:
                        return true;

                }
                return false;
            }
        });

        //横屏 被覆盖 谷歌输入法
        iDrawerLayout.switchContentFragment(new MainFragment());
    }


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        ButterKnife.bind(this);
//        iDrawerLayout.inflateMenu(R.menu.drawe);
////        iDrawerLayout.addNavigationViewHeader(R.layout.header);
//        iDrawerLayout.setOnNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                iDrawerLayout.closeDrawer();
//
//                switch (item.getItemId()) {
//                    case R.id.first:
//                        return true;
//
//                    case R.id.other:
//                        return true;
//
//                    case R.id.about:
//                        return true;
//
//                }
//                return false;
//            }
//        });
//
//    //横屏 被覆盖 谷歌输入法
//        iDrawerLayout.switchContentFragment(new MainFragment());
//    }


    private String doSomeThing() {
        return "doSomeThing";
    }




    public void switchDrawer(){
        iDrawerLayout.switchDrawer();
    }
}
