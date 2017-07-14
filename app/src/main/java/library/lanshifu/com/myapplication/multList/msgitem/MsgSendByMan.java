package library.lanshifu.com.myapplication.multList.msgitem;

import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ItemViewDelegate;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.multList.bean.ChatMessage;
import library.lanshifu.com.myapplication.multList.bean.Man;

/**
 * Created by 蓝师傅 on 2016/12/10.
 */

public class MsgSendByMan implements ItemViewDelegate<ChatMessage> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.main_chat_send_msg;
    }

    @Override
    public boolean isForViewType(ChatMessage item, int position) {
        return item instanceof Man;
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage, int position) {
        holder.setText(R.id.chat_send_content, chatMessage.getContent());
        holder.setText(R.id.chat_send_name, chatMessage.getName());
        holder.setImageResource(R.id.chat_send_icon, chatMessage.getIcon());
    }
}
