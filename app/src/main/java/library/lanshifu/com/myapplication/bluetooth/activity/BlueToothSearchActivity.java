package library.lanshifu.com.myapplication.bluetooth.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.bluetooth.bean.FriendInfo;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;
import library.lanshifu.com.myapplication.widget.CommRecyclerView;
import rx.functions.Action1;

public class BlueToothSearchActivity extends BaseActivity {


    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    CommRecyclerView recyclerView;
    private CommonAdapter<FriendInfo> adapter;
    private List<FriendInfo> friendInfoList = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice remoteDevice;
    private BluetoothSocket clientSocket;
    private UUID MY_UUID = UUID.fromString("abcd1234-ab12-ab12-ab12-abcdef123456"); //随便定义一个uid
    private OutputStream os;
    private String KEY_BLUE_TOOTH = "bluetooth";
    private BroadcastReceiver mPairBroadcastReceiver;

    @Override
    public int getLayoutId() {
        return R.layout.activity_blue_tooth_search;
    }

    @Override
    protected void initView() {

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter
                    .getDefaultAdapter();
        }
        registerBlueToothReceiver();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setEnableLoadmore(false);
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }

                RxPermissions.getInstance(mContext).request(Manifest.permission.ACCESS_COARSE_LOCATION)
                        .compose(RxSchedulerHelper.<Boolean>io_main())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {

                                if(aBoolean){
                                    mBluetoothAdapter.startDiscovery();
                                }else {
                                    T.showShort("权限拒绝");
                                }
                            }
                        });
                adapter.removeAllData();
            }
        });
        recyclerView.autoRefresh(1000);

    }

    private void initAdapter() {
        adapter = new CommonAdapter<FriendInfo>(this, R.layout.item_friend_info, friendInfoList) {
            @Override
            protected void convert(ViewHolder holder, final FriendInfo friendInfo, int position) {
                holder.setText(R.id.item_friend_name, friendInfo.getIdentificationName());
                holder.setText(R.id.item_friend_address, friendInfo.getDeviceAddress());
                holder.setText(R.id.item_friend_status,  friendInfo.isFriend() ? "好友" :"在线" );
                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BluetoothDevice bluetoothDevice = friendInfo.getBluetoothDevice();
                        if(bluetoothDevice.getBondState()!= BluetoothDevice.BOND_BONDED){
                            startProgressDialog("正在连接... "+friendInfo.getIdentificationName());
                            startPair();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                bluetoothDevice.createBond();
                            }else {
                                //利用反射方法调用BluetoothDevice.createBond(BluetoothDevice remoteDevice);
                                Method createBondMethod = null;
                                try {
                                    createBondMethod =BluetoothDevice.class.getMethod("createBond");
                                    createBondMethod.invoke(bluetoothDevice);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else {
                            T.showShort("已经配对");
                        }
//                        connectRemoteDevice(friendInfo.getIdentificationName(),friendInfo.getDeviceAddress());

                    }
                });
            }

        };
    }


    private void registerBlueToothReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //每搜索一个设备就会发送该广播
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        //全部搜索完会发送该广播
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        this.registerReceiver(receiver, intentFilter);
    }

    /**
     * 定义广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == BluetoothDevice.ACTION_FOUND) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                FriendInfo friendInfo = new FriendInfo();
                friendInfo.setIdentificationName(device.getName());
                friendInfo.setDeviceAddress(device.getAddress());
                friendInfo.setFriendNickName(device.getName());
                friendInfo.setBluetoothDevice(device);
                friendInfo.setFriend(device.getBondState()== BluetoothDevice.BOND_BONDED);
                adapter.add(friendInfo);
            } else if (intent.getAction() == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                title.setText("搜索完成。");
                recyclerView.finishRefresh();
            }

        }
    };



    /**
     * 连接设备
     * @param address
     */
    private void connectRemoteDevice(String name,String address) {
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }

        remoteDevice = mBluetoothAdapter.getRemoteDevice(address);

        try {
            if(clientSocket == null){
                clientSocket = remoteDevice.createRfcommSocketToServiceRecord(MY_UUID);
                clientSocket.connect();
                os = clientSocket.getOutputStream();
            }
        } catch (IOException e) {
            T.showShort("设备连接失败");
            stopProgressDialog();
            e.printStackTrace();
        }

        if(os!=null){
            try {
                os.write("蓝牙信息来了".getBytes("utf-8"));
                T.showShort("发送消息...");
            } catch (IOException e) {
                T.showShort("设备连接失败os");
                stopProgressDialog();
                e.printStackTrace();
            }
        }
    }



    private void startPair(){

        if(mPairBroadcastReceiver == null){
            mPairBroadcastReceiver = new BroadcastReceiver(){

                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                        //取得状态改变的设备，更新设备列表信息（配对状态）
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if(device != null){
                            resolveBondingState(device.getBondState());
                        }
                    }
                }
            };
        }
        //注册蓝牙配对监听器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mPairBroadcastReceiver, intentFilter);
    }

    private void resolveBondingState(final int bondState) {
        switch (bondState) {
            case BluetoothDevice.BOND_BONDED://已配对
                T.showShort("已配对");
                stopProgressDialog();
                break;
            case BluetoothDevice.BOND_BONDING://配对中
                T.showShort("配对中");
                break;
            case BluetoothDevice.BOND_NONE://未配对
                T.showShort("未配对");
                stopProgressDialog();
                break;
            default:
                T.showShort("未知状态");
                stopProgressDialog();
                break;
        }
    }



    @Override
    protected void onDestroy() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        if(mPairBroadcastReceiver != null){
            unregisterReceiver(mPairBroadcastReceiver);
            mPairBroadcastReceiver = null;
        }
        super.onDestroy();
    }

}
