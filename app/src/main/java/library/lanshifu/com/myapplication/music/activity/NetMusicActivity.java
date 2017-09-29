package library.lanshifu.com.myapplication.music.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import library.lanshifu.com.lsf_library.base.BaseTabActivity;
import library.lanshifu.com.myapplication.music.fragment.SongListFragment;
import library.lanshifu.com.myapplication.viewpager.transformer.CardTransformer;

/**
 * Created by lanshifu on 2017/9/21.
 */

public class NetMusicActivity extends BaseTabActivity {


    @Override
    protected void onViewCreate() {
        setTBTitle("音乐");

        getTabLayout().setTabMode(TabLayout.MODE_FIXED);
        String[] titles = new String[]{"歌单"};
        setupTabLayout(titles, new Fragment[]{
                //
                new SongListFragment(),

        });

        getViewPager().setOffscreenPageLimit(1);
        getViewPager().setPageTransformer(true, new CardTransformer());
    }


}
