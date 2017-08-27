package library.lanshifu.com.myapplication.bluetooth.activity;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.bluetooth.BluetoothChatHelper;
import library.lanshifu.com.myapplication.bluetooth.ChatConstant;
import library.lanshifu.com.myapplication.bluetooth.CommandHelper;
import library.lanshifu.com.myapplication.bluetooth.HexUtil;
import library.lanshifu.com.myapplication.bluetooth.State;
import library.lanshifu.com.myapplication.bluetooth.adapter.ChatAdapter;
import library.lanshifu.com.myapplication.bluetooth.bean.BaseMessage;
import library.lanshifu.com.myapplication.bluetooth.bean.ChatInfo;
import library.lanshifu.com.myapplication.bluetooth.bean.FriendInfo;
import library.lanshifu.com.myapplication.bluetooth.callback.IChatCallback;

public class BlueToothChatActivity extends BaseActivity {


    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_log)
    TextView tvLog;
    @Bind(R.id.et_text)
    EditText etText;
    @Bind(R.id.bt_send)
    Button btSend;
    private FriendInfo mFriendInfo;
    private BluetoothChatHelper mBluetoothChatHelper;
    private List<ChatInfo> mChatInfoList = new ArrayList<>();
    private ChatAdapter mChatAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_blue_tooth_chat;
    }

    @Override
    protected void initView() {

        mFriendInfo = this.getIntent().getParcelableExtra("friendInfo");
        if (mFriendInfo == null) {
            return;
        }
        mBluetoothChatHelper = new BluetoothChatHelper(chatCallback);

        mBluetoothChatHelper.setStopThread(false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothChatHelper.connect(mFriendInfo.getBluetoothDevice(), false);
            }
        }, 1000);

        initRecyclerViewAdapter();

    }

    private void initRecyclerViewAdapter() {
        mChatAdapter = new ChatAdapter(this,mChatInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mChatAdapter);
    }

    private IChatCallback<byte[]> chatCallback = new IChatCallback<byte[]>() {
        @Override
        public void connectStateChange(State state) {
            L.d("connectStateChange:" + state.getCode());
            if (state == State.STATE_CONNECTED) {
                stopProgressDialog();
                title.setText("连接成功");
                logOut("连接成功");
            } else if (state == State.STATE_LISTEN) {
                logOut("等待连接...");
                title.setText("等待连接...");
            } else if (state == State.STATE_CONNECTING) {
                logOut("正在连接...");
                title.setText("正在连接...");
            }else if (state == State.STATE_NONE) {
                logOut("连接出错");
                title.setText("连接出错");
            }
        }

        @Override
        public void writeData(byte[] data, int type) {
            if (data == null) {
                logOut("writeData is Null or Empty!");

                return;
            }
            logOut("writeData:" + HexUtil.encodeHexStr(data));
        }

        @Override
        public void readData(byte[] data, int type) {
            if (data == null) {
                logOut("readData is Null or Empty!");
                return;
            }
            logOut("readData:" + HexUtil.encodeHexStr(data));

            try {
                BaseMessage message = CommandHelper.unpackData(data);
                T.showShort("收到消息："+message);
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setMessage(message);
//                chatInfo.setReceiveTime(DateTime.getStringByFormat(new Date(), DateTime.DEFYMDHMS));
                chatInfo.setSend(false);
                chatInfo.setFriendInfo(mFriendInfo);
                mChatAdapter.add(chatInfo);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setDeviceName(String name) {
            logOut("setDeviceName:" + name);
        }

        @Override
        public void showMessage(String message, int code) {
            if (!isFinishing()) {
                return;
            }
            logOut("showMessage:" + message);
            stopProgressDialog();
            T.showShort("连接失败");
        }
    };


    private void logOut(String text) {
        tvLog.append("\r\n" + text);
    }

    private void sendMessag(String text) {

        ChatInfo chatInfo = new ChatInfo();
        FriendInfo friendInfo = new FriendInfo();
        friendInfo.setBluetoothDevice(mBluetoothChatHelper.getAdapter().getRemoteDevice(mBluetoothChatHelper.getAdapter().getAddress()));
        friendInfo.setOnline(true);
        friendInfo.setFriendNickName(mBluetoothChatHelper.getAdapter().getName());
        friendInfo.setIdentificationName(mBluetoothChatHelper.getAdapter().getName());
        friendInfo.setDeviceAddress(mBluetoothChatHelper.getAdapter().getAddress());
        chatInfo.setFriendInfo(friendInfo);
        chatInfo.setSend(true);
//        chatInfo.setSendTime(DateTime.getStringByFormat(new Date(), DateTime.DEFYMDHMS));
        BaseMessage message = null;

        message = new BaseMessage();
        message.setMsgType(ChatConstant.VISE_COMMAND_TYPE_TEXT);
        message.setMsgContent(text);
        message.setMsgLength(text.length());
        chatInfo.setMessage(message);
        mChatAdapter.add(chatInfo);
        etText.setText("");

        try {
            mBluetoothChatHelper.write(CommandHelper.packMsg(message.getMsgContent()));
        } catch (UnsupportedEncodingException e) {
            L.e("发送出错" + e.getMessage());
            T.showShort("发送失败："+e.getMessage());
        }

    }


    @OnClick(R.id.bt_send)
    public void onViewClicked() {
        if (etText.getText() != null) {
            String text = etText.getText().toString();
            sendMessag(text);
        }else {
            T.showShort("发送内容不能为空");
        }
    }


}
