package library.lanshifu.com.myapplication.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.widget.SmileView;

public class SmileFaceActivity extends BaseToolBarActivity {


    @Bind(R.id.smileFace)
    ImageView smileFace;
    @Bind(R.id.backGround)
    LinearLayout backGround;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.smileView)
    SmileView smileView;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_smil_face;
    }

    @Override
    protected void onViewCreate() {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) smileFace.getLayoutParams();
                layoutParams.bottomMargin = i*3;
                smileFace.setLayoutParams(layoutParams);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        smileView = (SmileView) findViewById(R.id.smileView);
        smileView.setNum(60,40);

    }




}
