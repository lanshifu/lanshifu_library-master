package library.lanshifu.com.myapplication.fragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.commwidget.popmenu.PopMenu;
import library.lanshifu.com.lsf_library.commwidget.popmenu.PopMenuItem;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.SystemManage;
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
import library.lanshifu.com.myapplication.hongyang.VRActivity;
import library.lanshifu.com.myapplication.imagepicker.PhotoPickerActivity;
import library.lanshifu.com.myapplication.music.activity.NetMusicActivity;
import library.lanshifu.com.myapplication.network_into.activity.NetWorkMainActivity;
import library.lanshifu.com.myapplication.popu.PopuDemoActivity;
import library.lanshifu.com.myapplication.shell.FileManagerActivity;
import library.lanshifu.com.myapplication.smartrefresh.SmartRefreshDemoActivity;
import library.lanshifu.com.myapplication.surfaceview.SurfaceViewActivity;
import library.lanshifu.com.myapplication.twolist.TwoListActivity;
import library.lanshifu.com.myapplication.ui.ExpendActivity;
import library.lanshifu.com.myapplication.ui.GaoKaoSearchActivity;
import library.lanshifu.com.myapplication.ui.GuideActivity;
import library.lanshifu.com.myapplication.ui.JsoupActivity;
import library.lanshifu.com.myapplication.ui.LoadingActivity;
import library.lanshifu.com.myapplication.ui.SmileFaceActivity;
import library.lanshifu.com.myapplication.ui.ZhiHuPictureActivity;
import library.lanshifu.com.myapplication.utils.SmsWriteOpUtil;
import library.lanshifu.com.myapplication.viewpager.CardSlideViewActivity;
import library.lanshifu.com.myapplication.viewpager.TabActivity;
import library.lanshifu.com.myapplication.viewpager.ViewPagerDemoActivity;
import library.lanshifu.com.myapplication.voice.VoiceListActivity;
import library.lanshifu.com.myapplication.wifi.WifiPassWorldActivity;
import library.lanshifu.com.myapplication.wifi.wifitransfe.ChooseFileActivity;

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
    private PendingIntent sentPI;
    private Intent deliverIntent;
    private PendingIntent deliverPI;

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
                .addMenuItem(new PopMenuItem(getActivity(), "高考查询", R.mipmap.icon_menu5))
                .addMenuItem(new PopMenuItem(getActivity(), "菜单7", R.mipmap.icon_menu5))
                .addMenuItem(new PopMenuItem(getActivity(), "菜单8", R.mipmap.icon_menu5))
                .setOnPopMenuItemListener(new PopMenu.OnPopMenuItemClickListener() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        if (position == 0) {
                            startActivity(new Intent(getActivity(), FlowTagDemoActivity.class));
                        } else if (position == 1) {
                            startActivity(new Intent(getActivity(), PopuDemoActivity.class));
                        } else if (position == 2) {
                            startActivity(new Intent(getActivity(), FileProviderDemoActivity.class));
                        } else if (position == 3) {
                            startActivity(new Intent(getActivity(), SmartRefreshDemoActivity.class));
                        } else if (position == 4) {
                            startActivity(new Intent(getActivity(), DropDownDemoActivity.class));
                        } else if (position == 5) {
                            startActivity(new Intent(getActivity(), GaoKaoSearchActivity.class));
                        }else {
                            showErrorToast("点击了+"+position);
                        }
                    }
                })
                .build();
    }


    @OnClick({R.id.btn_single, R.id.btn_multi, R.id.btn_base, R.id.btn_mult, R.id.toolbar, R.id.popmenu
            , R.id.activity_main, R.id.pagerfragment, R.id.bt_contentprovider, R.id.bt_voice,
            R.id.bt_photopicker, R.id.bt_slid_pager, R.id.btn_viewpager, R.id.btn_databinding
            , R.id.btn_twolist, R.id.btn_face, R.id.btn_cardstack, R.id.btn_surefaceview
            , R.id.btn_bluetooth, R.id.btn_wifi, R.id.btn_vr, R.id.btn_expend, R.id.btn_shell
            , R.id.btn_sms , R.id.btn_loading, R.id.btn_guide, R.id.btn_jsoup, R.id.btn_network_tool, R.id.btn_music})
            , R.id.btn_sms , R.id.btn_loading, R.id.btn_guide, R.id.btn_jsoup
            , R.id.btn_network_tool, R.id.btn_zhihu_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            case R.id.btn_single:
                String[] str = new String[]{"1", "2", "1", "2", "1", "2", "1", "2"};
                new AlertDialog.Builder(getContext())
                        .setTitle("单选框")
                        .setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showShortToast("点击了" + which);
                                dialog.dismiss();

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
                                showShortToast("点击了" + which + "，isChecked=" + isChecked);
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
           case R.id.btn_vr:
                startActivity(new Intent(getContext(), VRActivity.class));
                break;
            case R.id.btn_expend:
                startActivity(new Intent(getContext(), ExpendActivity.class));
                break;
            case R.id.btn_shell:
                startActivity(new Intent(getContext(), FileManagerActivity.class));
                break;
           case R.id.btn_loading:
                startActivity(new Intent(getContext(), LoadingActivity.class));
                break;
           case R.id.btn_guide:
                startActivity(new Intent(getContext(), GuideActivity.class));
                break;
            case R.id.btn_jsoup:
                startActivity(new Intent(getContext(), JsoupActivity.class));
                break;
            case R.id.btn_sms:
                new RxPermissions(getActivity()).request("android.permission.SEND_SMS")
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if(aBoolean){
                                    showShortToast("发送短信");
//                                    requestPermmission();
                                    chooseSim();
//                                    doSendSMSTo("10086","cxll");
                                }else {
                                    showShortToast("没有权限");
                                }
                            }
                        });

                break;

            case R.id.btn_network_tool:
                startActivity(new Intent(getContext(), NetWorkMainActivity.class));
                break;
          case R.id.btn_music:
                startActivity(new Intent(getContext(), NetMusicActivity.class));
                break;
             case R.id.btn_zhihu_pic:
                startActivity(new Intent(getContext(), ZhiHuPictureActivity.class));
                break;
        }

    }


    private static final int REQUEST_CODE = 1;

    private void requestAlertWindowPermission() {
        final String settings = Settings.ACTION_ACCESSIBILITY_SETTINGS;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(getActivity())) {
                startActivity(new Intent(settings));
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else {
            startActivity(new Intent(settings));
        }


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
                }
            }
        }
    }



    /**
     * 直接调用短信接口发短信
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(final String phoneNumber, final String message){

        //处理返回的发送状态
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        if (sentPI == null) {
            sentPI = PendingIntent.getBroadcast(getActivity(), 0, sentIntent,
                    0);
            // register the Broadcast Receivers
            getActivity().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context _context, Intent _intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            T.showShort("发送成功");
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            break;
                    }
                }
            }, new IntentFilter(SENT_SMS_ACTION));
        }

        //处理返回的接收状态
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        if (deliverIntent == null) {
            deliverIntent = new Intent(DELIVERED_SMS_ACTION);
            deliverPI = PendingIntent.getBroadcast(getActivity(), 0,
                    deliverIntent, 0);
            getActivity().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context _context, Intent _intent) {
                    Toast.makeText(getActivity(),
                            "收信人已经成功接收", Toast.LENGTH_SHORT)
                            .show();
                }
            }, new IntentFilter(DELIVERED_SMS_ACTION));
        }

        //获取短信管理器
        //5.0 之前只能获取卡1
         final SmsManager smsManager;
        //5.0 后通过subid来获取SmsManager对象
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if(SystemManage.hasTwoSimCard(getActivity())){
                //如果有两张卡
                new AlertDialog.Builder(getActivity())
                        .setTitle("选择要发短信的卡")
                        .setPositiveButton("卡1", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 SmsManager sm = SmsManager.getSmsManagerForSubscriptionId(SystemManage.getSubId(getActivity(),0));
                                sendMsgBySmsManager(sm,phoneNumber,message);
                            }
                        })
                        .setNegativeButton("卡2", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SmsManager sm = SmsManager.getSmsManagerForSubscriptionId(SystemManage.getSubId(getActivity(),1));
                                sendMsgBySmsManager(sm,phoneNumber,message);
                            }
                        }).show();
                return;

            }else{
                //默认卡1
                smsManager = SmsManager.getSmsManagerForSubscriptionId(SystemManage.getSubId(getActivity(),0));
            }
        }else {
            smsManager = SmsManager.getDefault();
        }
        sendMsgBySmsManager(smsManager,phoneNumber,message);
    }

    public void sendMsgBySmsManager(SmsManager smsManager,String phoneNumber,String message){
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI);
        }
    }



    public void chooseSim(){

        new RxPermissions(getActivity()).request("android.permission.READ_PHONE_STATE")
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean){
                            String[] imsi = SystemManage.getIMSI(getActivity());
                            String phoneNum = SystemManage.getPhoneNum(getActivity());

                            readSms();
//                            sendSMS("10086","cxll");
                                if(!SmsWriteOpUtil.isWriteEnabled(getActivity())){
                                    boolean writeEnabled = SmsWriteOpUtil.setWriteEnabled(getActivity(), true);
                                    loge("writeEnabled = "+writeEnabled);
                                }
                        }else {
                            L.e("没有权限");
                        }
                    }
                });

    }


    /**
     * 读取sms表
     */
    private void readSms() {
//        getSessionCursor();
        getThreads();
    }

    /**
     * 获取历史会话列表
     */
    public Cursor getSessionCursor() {

        ContentResolver cr = getActivity().getContentResolver();
        String[] projection = new String[] { "_id", "address", "person",
                "body", "date", "type", "thread_id","count(distinct thread_id)" };
        Uri uri = Uri.parse("content://sms/");
        Cursor cur = cr.query(uri, projection,"1=1) GROUP BY (thread_id", null, "date desc");
        // make the first access on the main thread a lot faster
        if (cur != null) {
            //乐视获取只有1条
            L.d("logd cur.getCount()"+cur.getCount());
            cur.getCount();
        }
        return cur;
    }

    public Cursor getThreads() {

//        Cursor cur = getActivity().getContentResolver().query(Uri.parse("content://sms/"),
//                new String[]{"* from threads --"}, null, null, null);

        Cursor cur = getActivity().getContentResolver().query(Uri.parse("content://sms/"),
                new String[]{" a.message_count, b.address, b.type from threads a, sms b " +
                        "where a.recipient_ids = b.thread_id group by b.address--"}, null, null, null);

        //        Cursor cur = getActivity().getContentResolver().query(Telephony.Threads.CONTENT_URI, new String[]{"id"}, null, null, "date desc");

//        ContentResolver cr = getActivity().getContentResolver();
//        String[] projection = new String[] { "_id"};
//        Uri uri = Uri.parse("content://threads/");
//        Cursor cur = cr.query(uri, null,null, null, "date desc");
        // make the first access on the main thread a lot faster
        if (cur != null) {
            L.d("logd therad cur.getCount()"+cur.getCount());
            cur.getCount();
        }
        return cur;
    }
    /**
     *
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber,String message){
        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }

    private void requestPermmission(){
//        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings");
//        Intent intent = new Intent();
//        intent.setComponent(cn);
//        intent.putExtra(":android:show_fragment", "com.android.settings.applications.AppOpsSummary");
//        startActivity(intent);

        //选择默认短信
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getActivity().getPackageName());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
