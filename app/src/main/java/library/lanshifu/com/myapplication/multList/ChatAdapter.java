package library.lanshifu.com.myapplication.multList;

import android.content.Context;

import java.util.List;

import library.lanshifu.com.lsf_library.adapter.recyclerview.MultiItemTypeAdapter;
import library.lanshifu.com.myapplication.multList.bean.ChatMessage;
import library.lanshifu.com.myapplication.multList.msgitem.MsgSendByCat;
import library.lanshifu.com.myapplication.multList.msgitem.MsgSendByMan;

/**
 * Created by 蓝师傅 on 2016/12/10.
 */

public class ChatAdapter extends MultiItemTypeAdapter<ChatMessage> {
    public ChatAdapter(Context context, List<ChatMessage> datas) {
        super(context, datas);


        addItemViewDelegate(new MsgSendByCat());
        addItemViewDelegate(new MsgSendByMan());
    }
}
