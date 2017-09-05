package library.lanshifu.com.myapplication.wifi.wifitransfe;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.Constant;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.FileInfo;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.WifiMgr;
import library.lanshifu.com.myapplication.wifi.wifitransfe.framgent.FileInfoFragment;

/**
 * Created by 蓝师傅 on 2017/3/5.
 */

public class ChooseFileActivity extends BaseToolBarActivity {
    @Bind(R.id.btn_selected)
    Button btnSelected;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    private FileInfoFragment mApkInfoFragment;
    private FileInfoFragment mJpgInfoFragment;
    private FileInfoFragment mMp3InfoFragment;
    private FileInfoFragment mMp4InfoFragment;
    private FileInfoFragment mCurrentFragment;

    /**
     * 获取文件的请求码
     */
    public static final int REQUEST_CODE_GET_FILE_INFOS = 200;
    private boolean mIsWebTransfer;
    private ShowSelectedFileInfoDialog mShowSelectedFileInfoDialog;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_choose_file;
    }

    @Override
    protected void onViewCreate() {
        setTBTitle("选择文件");

        mIsWebTransfer = getIntent().getBooleanExtra(Constant.KEY_WEB_TRANSFER_FLAG, false);

        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            initData();//初始化数据
                        } else {
                            T.showShort("没有权限");
                        }
                    }
                });


    }

    private void initData() {
        mApkInfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_APK);
        mJpgInfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_JPG);
        mMp3InfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_MP3);
        mMp4InfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_MP4);
        mCurrentFragment = mApkInfoFragment;

        String[] titles = getResources().getStringArray(R.array.array_res);
        viewPager.setAdapter(new ResPagerAdapter(getSupportFragmentManager(), titles));

        viewPager.setOffscreenPageLimit(4);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        setSelectedViewStyle(false);

        mShowSelectedFileInfoDialog = new ShowSelectedFileInfoDialog(this);
        mRxManager.on(FileInfo.TAG_SELETEDFILELISTCHANGED, new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                update();
            }

        });

    }

    /**
     * 更新选中文件列表的状态
     */
    private void update() {
        if (mApkInfoFragment != null) mApkInfoFragment.updateFileInfoAdapter();
        if (mJpgInfoFragment != null) mJpgInfoFragment.updateFileInfoAdapter();
        if (mMp3InfoFragment != null) mMp3InfoFragment.updateFileInfoAdapter();
        if (mMp4InfoFragment != null) mMp4InfoFragment.updateFileInfoAdapter();

        //更新已选中Button
        getSelectedView();
    }


    /**
     * 设置选中View的样式
     *
     * @param isEnable
     */
    private void setSelectedViewStyle(boolean isEnable) {
        if (isEnable) {
            btnSelected.setEnabled(true);
            btnSelected.setBackgroundResource(R.drawable.selector_bottom_text_common);
            btnSelected.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            btnSelected.setEnabled(false);
            btnSelected.setBackgroundResource(R.drawable.shape_bottom_text_unenable);
            btnSelected.setTextColor(getResources().getColor(R.color.gray));
        }
    }


    @Override
    protected void initPresenter() {

    }


    /**
     * 资源的PagerAdapter
     */
    class ResPagerAdapter extends FragmentPagerAdapter {
        String[] sTitleArray;

        public ResPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ResPagerAdapter(FragmentManager fm, String[] sTitleArray) {
            this(fm);
            this.sTitleArray = sTitleArray;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) { //应用
                mCurrentFragment = mApkInfoFragment;
            } else if (position == 1) { //图片
                mCurrentFragment = mJpgInfoFragment;
            } else if (position == 2) { //音乐
                mCurrentFragment = mMp3InfoFragment;
            } else if (position == 3) { //视频
                mCurrentFragment = mMp4InfoFragment;
            }
            return mCurrentFragment;
        }

        @Override
        public int getCount() {
            return sTitleArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sTitleArray[position];
        }
    }


    @OnClick({R.id.btn_selected, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_selected:
                if (mShowSelectedFileInfoDialog != null) {
                    mShowSelectedFileInfoDialog.show();
                }

                break;
            case R.id.btn_next:

                if (!AppContext.getAppContext().isFileInfoMapExist()) {//不存在选中的文件
                    T.showShort("请选择你要传输的文件！");
                    return;
                }

                startActivity(new Intent(this, WifiTranserActivity.class));

        }
    }


    /**
     * 获取选中文件的View
     *
     * @return
     */
    public View getSelectedView() {
        //获取SelectedView的时候 触发选择文件
        if (AppContext.getAppContext().getFileInfoMap() != null && AppContext.getAppContext().getFileInfoMap().size() > 0) {
            setSelectedViewStyle(true);
            int size = AppContext.getAppContext().getFileInfoMap().size();
            btnSelected.setText("已选：" + size);
        } else {
            setSelectedViewStyle(false);
            btnSelected.setText("已选（0）");
        }
        return btnSelected;
    }

    @Override
    protected void onDestroy() {
        if (mShowSelectedFileInfoDialog != null) {
            mShowSelectedFileInfoDialog.rxDestory();
        }
        AppContext.getInstance().getReceiverFileInfoMap().clear();

        //清楚所选中的文件
        AppContext.getInstance().getFileInfoMap().clear();
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            WifiMgr.getInstance(this).openWifi();
        }
    }
}
