package library.lanshifu.com.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.DividerItemDecoration;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.comm.BaseAppCompatActivity;

public class Main2Activity extends BaseAppCompatActivity {



    LinearLayout activityMain2;
    @BindView(R.id.dialog)
    Button dialog;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.reclclerview)
    RecyclerView reclclerview;
    private CommonAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initView() {
        setTBTitle("标题");

        addTBMore(new String[]{"啦啦啦", "哈哈哈"});


        final List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("这是数据" + i);
        }


        reclclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter(this, R.layout.recycler_item, datas) {


            @Override
            protected void convert(ViewHolder holder, Object o, final int position) {
                final String s = (String) o;
                holder.setText(R.id.title, s);
                holder.setOnClickListener(R.id.title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.showShort("点击了" + s);
                    }
                });


            }
        };
        reclclerview.setAdapter(adapter);
        /**添加分割线*/
        reclclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, R.mipmap.comm_divider_h));


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.dialog)
    public void onClick() {

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("啦啦啦");
//        builder.setMessage("message");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.show();


        View view = View.inflate(this, R.layout.layout_progress_dialog, null);


        new AlertDialog
                .Builder(this)
                .setMessage("mess")
                .setTitle("标题")
//                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .show();


    }


    int i = 0;

    @OnClick({R.id.add, R.id.delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                adapter.addData(1, "添加的数据" + i++);

                break;
            case R.id.delete:
                adapter.removeData(1);
                break;
        }
    }
}
