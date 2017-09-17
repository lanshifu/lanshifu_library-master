package library.lanshifu.com.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.commwidget.IDrawerLayout;
import library.lanshifu.com.myapplication.bluetooth.BluetoothChatHelper;
import library.lanshifu.com.myapplication.fragment.HomeFragment;
import library.lanshifu.com.myapplication.model.UserInfo;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
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

        iDrawerLayout.switchContentFragment(new HomeFragment());

//        initUserInfo();

    }

    private void initUserInfo() {
        UserInfo userInfo = new UserInfo("this is id","lanshifu","this is token");
        boolean save = userInfo.save();
        showShortToast("插入用户信息"+save);
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
        Log.e(TAG, "onPause: ");
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume : ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothChatHelper.getInstance().close();
        Log.e(TAG, "onDestroy: ");
    }

    /**
     * 退出提示
     */
    public void exit() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime  > 1200) {//如果两次按键时间间隔大于1秒，则不退出
            showErrorToast("再按一次退出程序");
            firstTime = secondTime;//更新firstTime
        } else {
            this.finish();
        }
    }
}
