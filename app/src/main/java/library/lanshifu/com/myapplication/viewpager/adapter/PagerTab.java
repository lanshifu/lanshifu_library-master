package library.lanshifu.com.myapplication.viewpager.adapter;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by lanxiaobin on 2017/8/1.
 */

public class PagerTab {

    private Fragment fragment;
    private String title;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PagerTab(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

}
