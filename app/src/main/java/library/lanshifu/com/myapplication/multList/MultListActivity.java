package library.lanshifu.com.myapplication.multList;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.comm.BaseAppCompatActivity;
import library.lanshifu.com.myapplication.multList.bean.Cat;
import library.lanshifu.com.myapplication.multList.bean.ChatMessage;
import library.lanshifu.com.myapplication.multList.bean.Man;

public class MultListActivity extends BaseAppCompatActivity {


    @Bind(R.id.reclclerview)
    RecyclerView reclclerview;
    @Bind(R.id.activity_mult_list)
    LinearLayout activityMultList;

    @Override
    protected int getContentView() {
        return R.layout.activity_mult_list;
    }

    @Override
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
