package library.lanshifu.com.myapplication.alive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.MainActivity;

/**
 * Created by lanxiaobin on 2017/11/30.
 */

public class SinglePixelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("onCreate--->启动1像素保活");
        // 获得activity的Window对象，设置其属性
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams
                attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
        // 绑定SinglePixelActivity到ScreenManager
        ScreenManager.getScreenManagerInstance(this)
                .setSingleActivity(this);
    }

    @Override
    protected void onDestroy() {
        L.d("onDestroy--->1像素保活被终止");
        Intent intentAlive = new Intent(this, MainActivity.class);
        intentAlive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentAlive);
        super.onDestroy();
    }
}
