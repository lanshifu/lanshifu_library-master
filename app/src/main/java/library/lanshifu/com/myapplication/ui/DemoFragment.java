package library.lanshifu.com.myapplication.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;

/**
 * Created by Administrator on 2017/7/23.
 */

public class DemoFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.demo_fragment;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

        RecyclerView recyclerview = mRootView.findViewById(R.id.recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(new CommonAdapter<String>(getActivity(), R.layout.item_textview, getData()) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        });

        L.e("initView");

    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("data" + i);
        }
        return list;
    }


}
