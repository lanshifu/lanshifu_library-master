package library.lanshifu.com.lsf_library.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.R;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;

/**
 * Created by Administrator on 2017/7/23.
 */

public class DemoFragment extends BaseFragment {


    private RecyclerView recyclerview;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recyclerview;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

        recyclerview = mRootView.findViewById(R.id.recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(new CommonAdapter<String>(getActivity(),R.layout.layout_item,getData()) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }

        });

    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("data"+i);
        }
        return list;
    }

    public void setTitle(String title) {
//        textView.setText(title);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
