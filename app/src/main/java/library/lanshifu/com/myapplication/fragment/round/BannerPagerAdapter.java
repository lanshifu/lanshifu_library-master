package library.lanshifu.com.myapplication.fragment.round;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import library.lanshifu.com.myapplication.fragment.round.BannerFragment;


public class BannerPagerAdapter extends FragmentPagerAdapter {
        
        private int[] imgRes;

        public BannerPagerAdapter(FragmentManager fm, int[] imgRes) {
            super(fm);
            this.imgRes = imgRes;
        }

        @Override
        public Fragment getItem(int position) {
            position %= imgRes.length;

            BannerFragment fragment = new BannerFragment();
            fragment.setImgRes(imgRes[position]);
            return fragment;
        }

        @Override
        public int getCount() {
            return 10000;
        }
    }