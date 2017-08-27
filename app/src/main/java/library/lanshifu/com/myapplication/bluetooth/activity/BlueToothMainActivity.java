package library.lanshifu.com.myapplication.bluetooth.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.bluetooth.BluetoothChatHelper;
import library.lanshifu.com.myapplication.bluetooth.BluetoothUtil;
import library.lanshifu.com.myapplication.bluetooth.CommandHelper;
import library.lanshifu.com.myapplication.bluetooth.HexUtil;
import library.lanshifu.com.myapplication.bluetooth.State;
import library.lanshifu.com.myapplication.bluetooth.bean.BaseMessage;
import library.lanshifu.com.myapplication.bluetooth.bean.ChatInfo;
import library.lanshifu.com.myapplication.bluetooth.bean.FriendInfo;
import library.lanshifu.com.myapplication.bluetooth.callback.IChatCallback;
import library.lanshifu.com.myapplication.widget.CommRecyclerView;

/**
 * Created by lanxiaobin on 2017/8/25.
 */

public class BlueToothMainActivity extends BaseActivity {
    private static final int CODE_OPEN_BLUETOOTH = 10;
    @Bind(R.id.recyclerview)
    CommRecyclerView recyclerview;

    private List<FriendInfo> friendInfoList = new ArrayList<>();
    private CommonAdapter<FriendInfo> adapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothChatHelper bluetoothChatHelper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bluetooth_main;
    }

    @Override
    protected void initView() {

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<FriendInfo>(this, R.layout.item_friend_info, friendInfoList) {
            @Override
            protected void convert(ViewHolder holder, final FriendInfo friendInfo, int position) {
                holder.setText(R.id.item_friend_name, friendInfo.getIdentificationName());
                holder.setText(R.id.item_friend_address, friendInfo.getDeviceAddress());
                holder.setText(R.id.item_friend_status,"好友");
                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("friendInfo", friendInfo);
                        Intent intent = new Intent(BlueToothMainActivity.this,BlueToothChatActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

        };
        recyclerview.setAdapter(adapter);
        recyclerview.setEnableLoadmore(false);

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter
                    .getDefaultAdapter();
        }
        getSaveDevices();

    }

    private void getSaveDevices() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                findDevice();
            } else {
                // 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
                // 那么将会收到RESULT_OK的结果，
                // 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
                Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(mIntent, CODE_OPEN_BLUETOOTH);
            }
        } else {
            T.showShort("设备不支持蓝牙");
        }
    }

    private void findDevice() {
        // 获得已经保存的配对设备
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            friendInfoList.clear();

            for (BluetoothDevice device : pairedDevices) {
                FriendInfo friendInfo = new FriendInfo();
                friendInfo.setIdentificationName(device.getName());
                friendInfo.setDeviceAddress(device.getAddress());
                friendInfo.setFriendNickName(device.getName());
                friendInfo.setOnline(false);
//                friendInfo.setJoinTime(DateTime.getStringByFormat(new Date(), DateTime.DEFYMDHMS));
                friendInfo.setBluetoothDevice(device);
                adapter.add(friendInfo);
            }

        } else {
            T.showShort("没有已配对的设备");
        }

//        if (bluetoothChatHelper == null) {
//            bluetoothChatHelper = new BluetoothChatHelper(chatCallback);
//        }
//        bluetoothChatHelper.start(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_OPEN_BLUETOOTH && resultCode == Activity.RESULT_OK) {
            findDevice();
        }
    }


    private IChatCallback<byte[]> chatCallback = new IChatCallback<byte[]>() {
        @Override
        public void connectStateChange(State state) {
            L.d("connectStateChange:" + state.getCode());
            if (state == State.STATE_CONNECTED) {
                stopProgressDialog();
                T.showShort("连接成功");
            } else if (state == State.STATE_LISTEN) {
                T.showShort("等待连接");
            } else if (state == State.STATE_CONNECTING) {
                T.showShort("正在连接");
            }
        }

        @Override
        public void writeData(byte[] data, int type) {
            if (data == null) {
//                logOut("writeData is Null or Empty!");

                return;
            }
            T.showShort("writeData:" + HexUtil.encodeHexStr(data));
        }

        @Override
        public void readData(byte[] data, int type) {
            if (data == null) {
//                logOut("readData is Null or Empty!");
                return;
            }
            T.showShort("readData:" + HexUtil.encodeHexStr(data));

            try {
                BaseMessage message = CommandHelper.unpackData(data);
                T.showShort("收到消息："+message);
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setMessage(message);
//                chatInfo.setReceiveTime(DateTime.getStringByFormat(new Date(), DateTime.DEFYMDHMS));
                chatInfo.setSend(false);
//                chatInfo.setFriendInfo(mFriendInfo);
//                mChatAdapter.add(chatInfo);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setDeviceName(String name) {
            L.e(("setDeviceName:" + name));
        }

        @Override
        public void showMessage(String message, int code) {
            if (!isFinishing()) {
                return;
            }
            stopProgressDialog();
            T.showShort("连接失败");
        }
    };


    @OnClick(R.id.bt_add)
    public void onViewClicked() {
        startActivity(new Intent(this,BlueToothSearchActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothChatHelper.close();
    }
}
