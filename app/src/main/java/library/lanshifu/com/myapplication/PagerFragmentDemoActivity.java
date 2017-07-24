package library.lanshifu.com.myapplication;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import library.lanshifu.com.lsf_library.base.BasePagerFragmentActivity;
import library.lanshifu.com.lsf_library.base.DemoFragment;

public class PagerFragmentDemoActivity extends BasePagerFragmentActivity {


    @Override
    protected List<Fragment> initFragmentList() {

        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DemoFragment demoFragment = new DemoFragment();
            demoFragment.setTitle("这个"+i);
            list.add(demoFragment);
        }
        return list;
    }

    @Override
    protected List<String> initTitleList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("这个"+i);
        }
        return list;
    }
}
