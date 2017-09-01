package library.lanshifu.com.myapplication.bluetooth.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.bluetooth.BluetoothChatHelper;
import library.lanshifu.com.myapplication.bluetooth.ChatConstant;
import library.lanshifu.com.myapplication.bluetooth.CommandHelper;
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
    private List<ChatInfo> mChatInfoList = new ArrayList<>();
    private ChatAdapter mChatAdapter;

    private static final int CODE_ISTYPING = 0;
    private static final int CODE_ISTYPING_OFF = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (CODE_ISTYPING_OFF == what) {
                sendMessag("对方停止输入", ChatConstant.VISE_COMMAND_TYPE_ISTYPING_OFF);
            }
        }
    };



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

        title.setText("与 " + mFriendInfo.getIdentificationName() + " 聊天中");

        BluetoothChatHelper.getInstance().setmChatCallback(chatCallback, mFriendInfo.getDeviceAddress());
        BluetoothChatHelper.getInstance().setStopThread(false);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                BluetoothChatHelper.getInstance().connect(mFriendInfo.getBluetoothDevice(), mFriendInfo.getDeviceAddress(), false);
            }
        }, 1000);

        initRecyclerViewAdapter();

        initEdittextListener();

    }

    private void initEdittextListener() {
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//               if(handler.hasMessages(CODE_ISTYPING_OFF)){
//                   handler.removeMessages(CODE_ISTYPING_OFF);
//               }
//               sendMessag("对方正在输入",ChatConstant.VISE_COMMAND_TYPE_ISTYPING_ON);
//
//               handler.sendEmptyMessageDelayed(CODE_ISTYPING_OFF,5000);

            }
        });

    }

    private void initRecyclerViewAdapter() {
        mChatAdapter = new ChatAdapter(this, mChatInfoList);
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
                title.setText("与 " + mFriendInfo.getIdentificationName() + " 聊天中");
            } else if (state == State.STATE_LISTEN) {
                logOut("等待连接...");
                title.setText("等待连接...");
            } else if (state == State.STATE_CONNECTING) {
                logOut("正在连接...");
                title.setText("正在连接...");
            } else if (state == State.STATE_NONE) {
                logOut("连接出错");
                title.setText("连接出错");
            }
        }

        @Override
        public void writeData(byte[] data, int type) {
            if (data == null) {
                return;
            }
            logOut("发送信息:...");
        }

        @Override
        public void readData(byte[] data, int type) {
            if (data == null) {
                logOut("收到空信息");
                return;
            }

            try {
                BaseMessage message = CommandHelper.unpackData(data);
                if (message != null) {
                    if ("对方正在输入".equals(message.getMsgContent())) {
                        if (title != null) {
                            title.setText("对方正在输入");
                        }
                        return;

                    }
                    if ("对方停止输入".equals(message.getMsgContent())) {
                        if (title != null) {
                            title.setText("与 " + mFriendInfo.getIdentificationName() + " 聊天中");
                        }
                        return;
                    }
                }


                logOut("收到信息" + message.getMsgContent());
                T.showShort("收到消息：" + message.getMsgContent());
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setMessage(message);
//                chatInfo.setReceiveTime(DateTime.getStringByFormat(new Date(), DateTime.DEFYMDHMS));
                chatInfo.setSend(false);
                chatInfo.setContent(message.getMsgContent());
                chatInfo.setReceiver(BluetoothChatHelper.getInstance().getAdapter().getAddress());
                chatInfo.setFriendInfo(mFriendInfo);
                boolean save = chatInfo.save();
                L.e("保存接收的消息" + save);
                mChatAdapter.add(chatInfo);
                recyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setDeviceName(String name) {
//            logOut("setDeviceName:" + name);
        }

        @Override
        public void showMessage(String message, int code) {

            logOut("showMessage:" + message);
            stopProgressDialog();
            T.showShort("连接失败" + message);
        }
    };


    private void logOut(String text) {
        if (tvLog != null) {
            tvLog.append("\r\n" + text);
        }
    }

    private void sendMessag(String text) {
        sendMessag(text, ChatConstant.VISE_COMMAND_TYPE_TEXT);
    }

    private void sendMessag(String text, byte msgType) {
        ChatInfo chatInfo = new ChatInfo();
        FriendInfo friendInfo = new FriendInfo();
        friendInfo.setBluetoothDevice(mFriendInfo.getBluetoothDevice());
        friendInfo.setOnline(true);
        friendInfo.setFriendNickName(mFriendInfo.getFriendNickName());
        friendInfo.setIdentificationName(mFriendInfo.getIdentificationName());
        friendInfo.setDeviceAddress(mFriendInfo.getDeviceAddress());
        chatInfo.setFriendInfo(friendInfo);
        chatInfo.setSend(true);
        chatInfo.setReceiver(mFriendInfo.getDeviceAddress());
//        chatInfo.setSendTime(DateTime.getStringByFormat(new Date(), DateTime.DEFYMDHMS));

        BaseMessage message = new BaseMessage();
        message.setMsgType(msgType);
        message.setMsgContent(text);
        message.setMsgLength(text.length());
        chatInfo.setContent(text);
        chatInfo.setMessage(message);

        try {
            BluetoothChatHelper.getInstance().write(CommandHelper.packMsg(message.getMsgContent()));
        } catch (UnsupportedEncodingException e) {
            L.e("发送出错" + e.getMessage());
            T.showShort("发送失败：" + e.getMessage());
        }

        if (msgType == ChatConstant.VISE_COMMAND_TYPE_ISTYPING_ON || msgType == ChatConstant.VISE_COMMAND_TYPE_ISTYPING_OFF) {
            return;
        }
        mChatAdapter.add(chatInfo);
        recyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
        boolean save = chatInfo.save();
        etText.setText("");
        L.e("保存消息到数据库" + save);

    }

    @OnClick(R.id.bt_send)
    public void onViewClicked() {
        if (etText.getText() != null) {
            String text = etText.getText().toString();
            sendMessag(text);
        } else {
            T.showShort("发送内容不能为空");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //根据会话id查询当前的会话信息
        List<ChatInfo> infoList = null;
        try {
            infoList = DataSupport.select()
                    .find(ChatInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (infoList != null && infoList.size() > 0) {
            mChatAdapter.refresh(infoList);
            recyclerView.scrollToPosition(infoList.size() - 1);
        }
    }
}
