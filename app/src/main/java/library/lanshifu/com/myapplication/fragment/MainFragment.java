package library.lanshifu.com.myapplication.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.commwidget.popmenu.PopMenu;
import library.lanshifu.com.lsf_library.commwidget.popmenu.PopMenuItem;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.DropDownDemoActivity;
import library.lanshifu.com.myapplication.FlowTagDemoActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.ToolBarDemoActivity;
import library.lanshifu.com.myapplication.bluetooth.activity.BlueToothMainActivity;
import library.lanshifu.com.myapplication.contentprovider.ProviderActivity;
import library.lanshifu.com.myapplication.databinding.DataBindingDemoActivity;
import library.lanshifu.com.myapplication.fileprovider.FileProviderDemoActivity;
import library.lanshifu.com.myapplication.guolin.cardstack.CardStackActivity;
import library.lanshifu.com.myapplication.imagepicker.PhotoPickerActivity;
import library.lanshifu.com.myapplication.popu.PopuDemoActivity;
import library.lanshifu.com.myapplication.smartrefresh.SmartRefreshDemoActivity;
import library.lanshifu.com.myapplication.surfaceview.SurfaceViewActivity;
import library.lanshifu.com.myapplication.twolist.TwoListActivity;
import library.lanshifu.com.myapplication.ui.SmileFaceActivity;
import library.lanshifu.com.myapplication.viewpager.CardSlideViewActivity;
import library.lanshifu.com.myapplication.viewpager.TabActivity;
import library.lanshifu.com.myapplication.viewpager.ViewPagerDemoActivity;
import library.lanshifu.com.myapplication.voice.VoiceListActivity;
import library.lanshifu.com.myapplication.wifi.WifiPassWorldActivity;
import library.lanshifu.com.myapplication.wifi.wifitransfe.ChooseFileActivity;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/15.
 */

public class MainFragment extends BaseFragment {
    @Bind(R.id.btn_single)
    Button btnSingle;
    @Bind(R.id.btn_multi)
    Button btnMulti;
    @Bind(R.id.btn_base)
    Button btnBase;
    @Bind(R.id.btn_mult)
    Button btnMult;
    @Bind(R.id.toolbar)
    Button toolbar;
    @Bind(R.id.popmenu)
    Button popmenu;


    private PopMenu popMenu;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initPresenter() {

    }

    class Parent {

        void say() {
            loge("我是父亲");
        }

        void sleep() {
            loge("i am sleep");
        }

    }

    class Child extends Parent {
        @Override
        void say() {
            super.say();
            if (true) {
                loge("我是儿子");
            }

        }
    }

    @Override
    protected void initView() {
//        Parent child = new Child();
//        child.say();
//        child.sleep();

    }


    private void initPopuMenu() {

        popMenu = new PopMenu.Builder(getActivity())
                .columnCount(4)
                .addMenuItem(new PopMenuItem(getActivity(), "流布局", R.mipmap.icon_menu1))
                .addMenuItem(new PopMenuItem(getActivity(), "popu", R.mipmap.icon_menu2))
                .addMenuItem(new PopMenuItem(getActivity(), "7.0适配", R.mipmap.icon_menu3))
                .addMenuItem(new PopMenuItem(getActivity(), "刷新控件", R.mipmap.icon_menu4))
                .addMenuItem(new PopMenuItem(getActivity(), "可下拉布局", R.mipmap.icon_menu5))
                .addMenuItem(new PopMenuItem(getActivity(), "菜单6", R.mipmap.icon_menu5))
                .addMenuItem(new PopMenuItem(getActivity(), "菜单7", R.mipmap.icon_menu5))
                .addMenuItem(new PopMenuItem(getActivity(), "菜单8", R.mipmap.icon_menu5))
                .setOnPopMenuItemListener(new PopMenu.OnPopMenuItemClickListener() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        showShortToast("菜单" + position);
                        if (position == 0) {
                            startActivity(new Intent(getActivity(), FlowTagDemoActivity.class));
                        }
                        if (position == 1) {
                            startActivity(new Intent(getActivity(), PopuDemoActivity.class));
                        }
                        if (position == 2) {
                            startActivity(new Intent(getActivity(), FileProviderDemoActivity.class));
                        }
                        if (position == 3) {
                            startActivity(new Intent(getActivity(), SmartRefreshDemoActivity.class));
                        }
                        if (position == 4) {
                            startActivity(new Intent(getActivity(), DropDownDemoActivity.class));
                        }
                    }
                })
                .build();
    }


    @OnClick({R.id.btn_single, R.id.btn_multi, R.id.btn_base, R.id.btn_mult, R.id.toolbar, R.id.popmenu
            , R.id.activity_main, R.id.pagerfragment, R.id.bt_contentprovider, R.id.bt_voice,
            R.id.bt_photopicker, R.id.bt_slid_pager, R.id.btn_viewpager, R.id.btn_databinding
            , R.id.btn_twolist, R.id.btn_face, R.id.btn_cardstack, R.id.btn_surefaceview
            ,R.id.btn_bluetooth,R.id.btn_wifi})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            case R.id.btn_single:
                String[] str = new String[]{"1", "2", "1", "2", "1", "2", "1", "2"};
                new AlertDialog.Builder(getContext())
                        .setTitle("单选框")
                        .setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                T.showShort("点击了" + which);
                                dialog.dismiss();
                                int i = 8 / 0;
                                int j = i;
                            }
                        })
                        .show();

                break;
            case R.id.btn_multi:
                String[] str2 = new String[]{"1", "2", "3", "4"};
                boolean[] select = new boolean[]{false, false, false, true};
                new AlertDialog.Builder(getContext())
                        .setTitle("多选框")
                        .setMultiChoiceItems(str2, select, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                T.showShort("点击了" + which + "，isChecked=" + isChecked);
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;


            case R.id.btn_base:
                //打开辅助功能
//                startActivity(new Intent(getActivity(), DropDownDemoActivity.class));
                requestAlertWindowPermission();
                break;
            case R.id.toolbar:
                startActivity(new Intent(getContext(), ToolBarDemoActivity.class));
                break;

            case R.id.btn_mult:
                startActivity(new Intent(getContext(), WifiPassWorldActivity.class));
                break;

            case R.id.popmenu:
                if (popMenu == null) {
                    initPopuMenu();
                }
                if (!popMenu.isShowing()) {
                    popMenu.show();
                }
                break;

            case R.id.pagerfragment:
                startActivity(new Intent(getContext(), TabActivity.class));
                break;
            case R.id.bt_contentprovider:
                startActivity(new Intent(getContext(), ProviderActivity.class));
                break;

            case R.id.bt_voice:
                startActivity(new Intent(getContext(), VoiceListActivity.class));
                break;
            case R.id.bt_photopicker:
                startActivity(new Intent(getContext(), PhotoPickerActivity.class));
                break;
            case R.id.bt_slid_pager:
                startActivity(new Intent(getContext(), CardSlideViewActivity.class));
                break;
            case R.id.btn_viewpager:
                startActivity(new Intent(getContext(), ViewPagerDemoActivity.class));
                break;
            case R.id.btn_databinding:
                startActivity(new Intent(getContext(), DataBindingDemoActivity.class));
                break;
            case R.id.btn_twolist:
                startActivity(new Intent(getContext(), TwoListActivity.class));
                break;
            case R.id.btn_face:
                startActivity(new Intent(getContext(), SmileFaceActivity.class));
                break;
            case R.id.btn_cardstack:
                startActivity(new Intent(getContext(), CardStackActivity.class));
                break;
            case R.id.btn_surefaceview:
                startActivity(new Intent(getContext(), SurfaceViewActivity.class));
                break;

            case R.id.btn_bluetooth:
                startActivity(new Intent(getContext(), BlueToothMainActivity.class));
                break;
            case R.id.btn_wifi:
                startActivity(new Intent(getContext(), ChooseFileActivity.class));
                break;
        }

    }


    private static final int REQUEST_CODE = 1;

    private void requestAlertWindowPermission() {
        final String settings = Settings.ACTION_ACCESSIBILITY_SETTINGS;

        RxPermissions.getInstance(getActivity()).request(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {

                            showShortToast("有权限");
                            startActivity(new Intent(settings));
                        } else {
                            showShortToast("没有权限，请求");
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                            startActivityForResult(intent, REQUEST_CODE);
                        }

                    }
                });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getActivity())) {
                    Log.i("111", "onActivityResult success");
                    String settings = Settings.ACTION_ACCESSIBILITY_SETTINGS;
                    startActivity(new Intent(settings));

                    //联系人 一段时间
//                    intent = new Intent(this,WindowService.class);
//                    startService(intent);
                }
            }
        }
    }
}
