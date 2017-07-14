package library.lanshifu.com.myapplication.multList.msgitem;

import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ItemViewDelegate;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.multList.bean.Cat;
import library.lanshifu.com.myapplication.multList.bean.ChatMessage;

/**
 * Created by 蓝师傅 on 2016/12/10.
 */

public class MsgSendByCat implements ItemViewDelegate<ChatMessage> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.main_chat_from_msg;
    }

    @Override
    public boolean isForViewType(ChatMessage item, int position) {
        return item instanceof Cat;
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage, int position) {
        holder.setText(R.id.chat_from_content, chatMessage.getContent());
        holder.setText(R.id.chat_from_name, chatMessage.getName());
        holder.setImageResource(R.id.chat_from_icon, chatMessage.getIcon());

    }
}
