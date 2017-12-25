package library.lanshifu.com.myapplication;

import android.content.Intent;
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
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.alive.ScreenManager;
import library.lanshifu.com.myapplication.alive.ScreenReceiverUtil;
import library.lanshifu.com.myapplication.bluetooth.BluetoothChatHelper;
import library.lanshifu.com.myapplication.fragment.HomeFragment;
import library.lanshifu.com.myapplication.model.UserInfo;
import library.lanshifu.com.myapplication.service.DaemonService;

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

        keepAlive();

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


    //保活
    private void keepAlive(){
        //1前台services
        startDaemonService();
        //2 锁屏1像素惨案
        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);

    }

    private void startDaemonService() {
        Intent intent = new Intent(this, DaemonService.class);
        startService(intent);
    }

    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;
    // 代码省略...
    private ScreenReceiverUtil.SreenStateListener
            mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            // 移除"1像素"
            L.d("屏幕开启,结束1像素activity");
            mScreenManager.finishActivity();
        }
        @Override
        public void onSreenOff() {
            // 接到锁屏广播，将SportsActivity切换到可见模式
            // "咕咚"、"乐动力"、"悦动圈"就是这么做滴
            // Intent intent =
            //new Intent(SportsActivity.this,SportsActivity.class);
            // startActivity(intent);
            // 如果你觉得，直接跳出SportActivity很不爽
            // 那么，我们就制造个"1像素"惨案
            mScreenManager.startActivity();
        }
        @Override
        public void onUserPresent() {
            // 解锁，暂不用，保留
        }
    };

}
