package library.lanshifu.com.myapplication.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.bluetooth.bean.FriendInfo;
import library.lanshifu.com.myapplication.widget.CommRecyclerView;

/**
 * Created by lanxiaobin on 2017/8/25.
 */

public class BlueToothMainActivity extends BaseActivity {
    @Bind(R.id.recyclerview)
    CommRecyclerView recyclerview;

    private List<FriendInfo> friendInfoList = new ArrayList<>();
    private CommonAdapter<FriendInfo> adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bluetooth_main;
    }

    @Override
    protected void initView() {

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<FriendInfo>(this, R.layout.item_friend_info, friendInfoList) {
            @Override
            protected void convert(ViewHolder holder, FriendInfo friendInfo, int position) {
                holder.setText(R.id.item_friend_name,friendInfo.getIdentificationName());
                holder.setText(R.id.item_friend_address,friendInfo.getDeviceAddress());
                holder.setText(R.id.item_friend_status,friendInfo.isOnline() ? "在线" : "离线");
            }

        };
        recyclerview.setAdapter(adapter);

        getSaveDevices();

    }

    private void getSaveDevices(){
        if(BluetoothUtil.isSupportBle(mContext)){
            BluetoothUtil.enableBluetooth((Activity) mContext, 1);
            findDevice();
        } else{
            T.showShort("设备不支持蓝牙");
        }
    }

    private void findDevice(){
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

        }
    }



}
