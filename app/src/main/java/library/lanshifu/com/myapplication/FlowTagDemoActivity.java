package library.lanshifu.com.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.commwidget.flowtaglayout.FlowTagAdapter;
import library.lanshifu.com.lsf_library.commwidget.flowtaglayout.FlowTagLayout;
import library.lanshifu.com.lsf_library.commwidget.flowtaglayout.OnTagClickListener;
import library.lanshifu.com.lsf_library.commwidget.flowtaglayout.OnTagSelectListener;

public class FlowTagDemoActivity extends BaseToolBarActivity {


    @Bind(R.id.flowtag1)
    FlowTagLayout flowtag1;
    @Bind(R.id.flowtag2)
    FlowTagLayout flowtag2;
    @Bind(R.id.flowtag3)
    FlowTagLayout flowtag3;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_flow_tag_demo;
    }

    @Override
    protected void onViewCreate() {

        setTBTitle("Tab标签");


        FlowTagAdapter adapter1 = new FlowTagAdapter<String>(this, new ArrayList<String>()) {
            @Override
            public int getLayoutId() {
                return R.layout.tag_item;
            }

            @Override
            public void bindView(View itemView, int position, String data) {

                TextView textView = itemView.findViewById(R.id.tv_tag);
                textView.setText(data);
            }

        };

        FlowTagAdapter adapter2 = new FlowTagAdapter<String>(this, new ArrayList<String>()) {
            @Override
            public int getLayoutId() {
                return R.layout.tag_item;
            }

            @Override
            public void bindView(View itemView, int position, String data) {
                TextView textView = itemView.findViewById(R.id.tv_tag);
                textView.setText(data);
            }
        };

        FlowTagAdapter adapter3 = new FlowTagAdapter<String>(this, new ArrayList<String>()) {
            @Override
            public int getLayoutId() {
                return R.layout.tag_item;
            }

            @Override
            public void bindView(View itemView, int position, String data) {
                TextView textView = itemView.findViewById(R.id.tv_tag);
                textView.setText(data);
            }
        };

        flowtag1.setAdapter(adapter1);
        flowtag2.setAdapter(adapter2);
        flowtag3.setAdapter(adapter3);

        flowtag2.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        flowtag3.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);

        flowtag1.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onItemClick(FlowTagLayout parent, View view, int position) {
                showShortToast(position + "");
            }
        });
        flowtag2.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList.size() > 0) {
                    showShortToast("选择了" + selectedList.get(0));
                } else {
                    showShortToast("未选择");
                }
            }
        });

        flowtag3.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList.size() == 0) {
                    showShortToast("未选择");
                    return;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < selectedList.size(); i++) {
                    Integer position = selectedList.get(i);
                    String item = (String) parent.getAdapter().getItem(position);
                    builder.append(item + ",");
                }
                showShortToast(builder.toString());
            }
        });


        adapter1.addAll(initList("item"));
        adapter2.addAll(initList("单选"));
        adapter3.addAll(initList("多选"));


        flowtag3.setCheckItem(1, 2, 3);


    }

    private List initList(String text) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            list.add(text + i);
        }
        return list;
    }


}
