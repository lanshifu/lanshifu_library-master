package library.lanshifu.com.myapplication.multList;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.multList.bean.Cat;
import library.lanshifu.com.myapplication.multList.bean.ChatMessage;
import library.lanshifu.com.myapplication.multList.bean.Man;

public class MultListActivity extends BaseToolBarActivity {


    @Override
    protected int getLayoutid() {
        return R.layout.activity_mult_list;
    }

    @Override
    protected void onViewCreate() {
        initView();
    }

    @Bind(R.id.reclclerview)
    RecyclerView reclclerview;

    @Bind(R.id.activity_mult_list)
    LinearLayout activityMultList;


    protected void initView() {


        List<ChatMessage> mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ChatMessage mes;
            if (i % 2 == 0) {
                mes = new Cat();
                mes.setContent("猫猫在说话");
                mes.setName("猫");
                mes.setIcon(R.mipmap.ic_launcher);
            } else {
                mes = new Man();
                mes.setContent("打人在说话");
                mes.setIcon(R.mipmap.comm_delete);
                mes.setName("人");
            }
            mDatas.add(mes);
        }


        ChatAdapter adapter = new ChatAdapter(this, mDatas);
        reclclerview.setAdapter(adapter);
        reclclerview.setLayoutManager(new LinearLayoutManager(this));

    }


}
