package library.lanshifu.com.myapplication.bluetooth.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import library.lanshifu.com.lsf_library.adapter.recyclerview.MultiItemTypeAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ItemViewDelegate;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.bluetooth.bean.ChatInfo;

/**
 * Created by Administrator on 2017/8/26.
 */

public class ChatAdapter extends MultiItemTypeAdapter<ChatInfo> {

    private List<ChatInfo> mChatInfoList = new ArrayList<>();

    public ChatAdapter(Context context, List datas) {
        super(context, datas);
        this.mChatInfoList = datas;

        addItemViewDelegate(new ItemViewDelegate<ChatInfo>() {

            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_chat_info_left;
            }

            @Override
            public boolean isForViewType(ChatInfo item, int position) {
                return !item.isSend();
            }

            @Override
            public void convert(ViewHolder holder, ChatInfo chatInfo, int position) {
                holder.setText(R.id.item_chat_left_msg,chatInfo.getContent());

            }
        });


        addItemViewDelegate(new ItemViewDelegate<ChatInfo>() {

            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_chat_info_right;
            }

            @Override
            public boolean isForViewType(ChatInfo item, int position) {
                return  item.isSend();
            }

            @Override
            public void convert(ViewHolder holder, ChatInfo chatInfo, int position) {
                holder.setText(R.id.item_chat_right_msg,chatInfo.getContent());
            }
        });
    }


    public void add(ChatInfo chatInfo){
        this.mChatInfoList.add(chatInfo);
        notifyDataSetChanged();
    }


    public void refresh(List<ChatInfo> chatInfos){
        mChatInfoList.clear();
        mChatInfoList.addAll(chatInfos);
        notifyDataSetChanged();
    }

}
