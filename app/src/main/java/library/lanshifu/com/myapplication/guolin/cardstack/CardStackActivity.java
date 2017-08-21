package library.lanshifu.com.myapplication.guolin.cardstack;

import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.StackView;

import com.loopeer.cardstack.CardStackView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;

public class CardStackActivity extends BaseToolBarActivity implements CardStackView.ItemExpendListener{


    @Bind(R.id.stackview_main)
    CardStackView stackviewMain;
    @Bind(R.id.button_container)
    LinearLayout buttonContainer;
    private TestStackAdapter mTestStackAdapter;
    public static Integer[] TEST_DATAS = new Integer[]{
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6,
            R.color.color_7,
            R.color.color_8,
            R.color.color_9,
            R.color.color_10,

    };

    @Override
    protected int getLayoutid() {
        return R.layout.activity_card_stack;
    }

    @Override
    protected void onViewCreate() {

        stackviewMain.setItemExpendListener(this);
        mTestStackAdapter = new TestStackAdapter(this);
        stackviewMain.setAdapter(mTestStackAdapter);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mTestStackAdapter.updateData(Arrays.asList(TEST_DATAS));
                    }
                }
                , 200
        );

    }




    @Override
    public void onItemExpend(boolean expend) {

    }
}
