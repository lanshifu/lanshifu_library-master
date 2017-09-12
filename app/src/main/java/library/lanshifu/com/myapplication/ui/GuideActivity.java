package library.lanshifu.com.myapplication.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.app.hubert.library.Controller;
import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;
import com.app.hubert.library.OnGuideChangedListener;

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

        NewbieGuide.with(this)//传入activity
                .setLabel("guide1")//设置引导层标示，必传！否则报错
                .addHighLight(tv1, HighLight.Type.RECTANGLE)//添加需要高亮的view
                .setLayoutRes(R.layout.view_guide1)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
                .setBackgroundColor(R.color.white_alpha_half)
                .alwaysShow(true)
                .show();//直接显示引导层


//        guide1();

    }

    private void guide2() {
        Controller controller = NewbieGuide.with(this)
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
                .setBackgroundColor(Color.BLACK)//设置引导层背景色，建议有透明度，默认背景色为：0xb2000000
                .setEveryWhereCancelable(false)//设置点击任何区域消失，默认为true
                .setLayoutRes(R.layout.view_guide2, R.id.tv2)//自定义的提示layout,第二个可变参数为点击隐藏引导层view的id
                .alwaysShow(true)//是否每次都显示引导层，默认false
                .setLabel("guide2")
                .build();//构建引导层的控制器
        controller.resetLabel("guide2");
        controller.remove();//移除引导层
        controller.show();//显示引导层
    }

    private void guide3() {
        Controller controller = NewbieGuide.with(this)
                .setOnGuideChangedListener(new OnGuideChangedListener() {//设置监听
                    @Override
                    public void onShowed(Controller controller) {
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        //引导层消失
                    }
                })
                .setBackgroundColor(Color.BLACK)//设置引导层背景色，建议有透明度，默认背景色为：0xb2000000
                .setEveryWhereCancelable(false)//设置点击任何区域消失，默认为true
                .setLayoutRes(R.layout.view_guide3, R.id.tv3)//自定义的提示layout,第二个可变参数为点击隐藏引导层view的id
                .alwaysShow(true)//是否每次都显示引导层，默认false
                .setLabel("guide3")
                .build();//构建引导层的控制器
        controller.resetLabel("guide3");
        controller.remove();//移除引导层
        controller.show();//显示引导层
    }

    private void guide1() {
        Controller controller = NewbieGuide.with(this)
                .setOnGuideChangedListener(new OnGuideChangedListener() {//设置监听
                    @Override
                    public void onShowed(Controller controller) {
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        //引导层消失
                        guide2();
                    }
                })
                .setBackgroundColor(Color.BLACK)//设置引导层背景色，建议有透明度，默认背景色为：0xb2000000
                .setEveryWhereCancelable(false)//设置点击任何区域消失，默认为true
                .setLayoutRes(R.layout.view_guide1, R.id.tv1)//自定义的提示layout,第二个可变参数为点击隐藏引导层view的id
                .alwaysShow(true)//是否每次都显示引导层，默认false
                .setLabel("guide1")
                .build();//构建引导层的控制器
        controller.resetLabel("guide1");
        controller.remove();//移除引导层
        controller.show();//显示引导层
    }


}
