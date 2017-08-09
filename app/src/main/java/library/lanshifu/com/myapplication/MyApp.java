package library.lanshifu.com.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import library.lanshifu.com.lsf_library.baseapp.BaseApplication;

/**
 * Created by Administrator on 2017/7/15.
 */

public class MyApp extends BaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();

        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context);
            }


        });
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initHotFix();
    }

    private void initHotFix() {
        Log.e(this.getClass().getSimpleName(), "initHotFix: " );
        SophixManager.getInstance().setContext(this)
                .setAppVersion("1.0.0")
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Log.e(this.getClass().getSimpleName(), "onLoad: 补丁加载成功");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀，以此加快应用补丁
                            // 建议调用killProcessSafely，详见1.3.2.3
                            // SophixManager.getInstance().killProcessSafely();
                            Log.e(this.getClass().getSimpleName(), "onLoad: CODE_LOAD_RELAUNCH");
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            // SophixManager.getInstance().cleanPatches();
                            Log.e(this.getClass().getSimpleName(), "onLoad: CODE_LOAD_FAIL");
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.e(this.getClass().getSimpleName(), "onLoad: 其它错误信息, 查看PatchStatus类说明");
                        }
                    }
                }).initialize();
    }
}
