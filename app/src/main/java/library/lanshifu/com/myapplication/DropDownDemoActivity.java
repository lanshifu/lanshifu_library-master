package library.lanshifu.com.myapplication;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.widget.DropDownView;

public class DropDownDemoActivity extends BaseToolBarActivity {

    @Bind(R.id.dropDownView)
    DropDownView dropDownView;


    @Override
    protected int getLayoutid() {
        return R.layout.activity_drop_down_demo;
    }

    @Override
    protected void onViewCreate() {

        setTBTitle("可下拉布局");

        View headView = LayoutInflater.from(this).inflate(R.layout.tag_item, null);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.pop_list, null);
        RecyclerView recyclerView = (RecyclerView) linearLayout.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CommonAdapter<String>(this, R.layout.list_item, getData()) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.title, s);

            }

        });


        dropDownView.setHeaderView(headView);
        dropDownView.setExpandedView(linearLayout);

    }

    public List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("data" + i);
        }
        return list;
    }
}
