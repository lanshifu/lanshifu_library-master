package library.lanshifu.com.myapplication.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.app.hubert.library.Controller;
import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;
import com.app.hubert.library.OnGuideChangedListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;

/**
 * Created by lanshifu on 2017/9/12.
 */

public class GuideActivity extends BaseToolBarActivity {
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onViewCreate() {
        guide1();

    }

    private void guide1() {
        NewbieGuide.with(this)//传入activity
                .setLabel("guide1")//设置引导层标示，必传！否则报错
                .addHighLight(tv1, HighLight.Type.RECTANGLE)//添加需要高亮的view
                .setLayoutRes(R.layout.view_guide1)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
                .setBackgroundColor(R.color.white_alpha_half)
                .alwaysShow(true)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {

                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        guide2();
                    }
                })
                .build()
                .show();//直接显示引导层
    }

    private void guide2() {
       NewbieGuide.with(this)
               .setLabel("guide2")//设置引导层标示，必传！否则报错
               .addHighLight(tv2, HighLight.Type.RECTANGLE)//添加需要高亮的view
               .setLayoutRes(R.layout.view_guide2)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
               .setBackgroundColor(R.color.white_alpha_half)
               .alwaysShow(true)
               .setOnGuideChangedListener(new OnGuideChangedListener() {//设置监听
                   @Override
                   public void onShowed(Controller controller) {
                       //引导层显示
                   }

                   @Override
                   public void onRemoved(Controller controller) {
                       //引导层消失
                       guide3();
                   }
               })
               .build()
               .show();//构建引导层的控制器
    }

    private void guide3() {
        NewbieGuide.with(this)
                .setLabel("guide3")//设置引导层标示，必传！否则报错
                .addHighLight(tv3, HighLight.Type.RECTANGLE)//添加需要高亮的view
                .setLayoutRes(R.layout.view_guide3)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
                .setBackgroundColor(R.color.white_alpha_half)
                .alwaysShow(true)
                .show();//构建引导层的控制器
    }




}
