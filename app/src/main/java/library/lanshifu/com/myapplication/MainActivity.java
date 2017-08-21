package library.lanshifu.com.myapplication;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.commwidget.IDrawerLayout;
import library.lanshifu.com.myapplication.fragment.HomeFragment;

public class MainActivity extends BaseActivity {

    @Bind(R.id.iDrawerLayout)
    IDrawerLayout iDrawerLayout;
    private long firstTime = 0;

    @Override
    protected void doBeforeSetcontentView() {
        super.doBeforeSetcontentView();
        setTheme(R.style.AppTheme);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

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
        iDrawerLayout.switchContentFragment(new HomeFragment());
    }



    private String doSomeThing() {
        return "doSomeThing";
    }




    public void switchDrawer(){
        iDrawerLayout.switchDrawer();
    }



    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        exit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 退出提示
     */
    public void exit() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime  > 1200) {//如果两次按键时间间隔大于1秒，则不退出
            Toast.makeText(this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            firstTime = secondTime;//更新firstTime
        } else {
            this.finish();
        }
    }
}
