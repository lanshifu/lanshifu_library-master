package library.lanshifu.com.lsf_library.base;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import library.lanshifu.com.lsf_library.R;
import library.lanshifu.com.lsf_library.base.adapter.BasePagerFragmentAdapter;

/**
 * Created by Administrator on 2017/7/23.
 */

public abstract class BasePagerFragmentActivity extends BaseActivity {


    FragmentManager fm;
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    private TabLayout tablayout;
    private ViewPager viewpager;


    @Override
    public int getLayoutId() {
        return R.layout.layout_base_pager;
    }

    @Override
    protected void initView() {
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        fm = getSupportFragmentManager();
        tablayout.setupWithViewPager(viewpager);


        viewpager.setAdapter(new BasePagerFragmentAdapter(fm, initTitleList(), initFragmentList()));
        tablayout.setupWithViewPager(viewpager);

    }



    protected abstract List<Fragment> initFragmentList();

    protected abstract List<String> initTitleList();


}
