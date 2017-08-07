package library.lanshifu.com.myapplication.twolist;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;

/**
 * Created by lanxiaobin on 2017/8/7.
 */

public class TwoListActivity extends BaseToolBarActivity {

    private static final String KEY_ = "";
    @Bind(R.id.rv_sort)
    RecyclerView mRecyclerView;
    private RvAdapter<String> adapter;
    private ShotDetailFragment fragment;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_two_list;
    }

    @Override
    protected void onViewCreate() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RvAdapter<String>(this, R.layout.item_classify_detail, getData());
        mRecyclerView.setAdapter(adapter);
        fragment = new ShotDetailFragment();
        addFragment(R.id.lin_fragment, fragment);

    }

    class RvAdapter<String> extends CommonAdapter {

        protected int checkPosition = -1;

        public void setcheck(int position) {
            checkPosition = position;
            notifyDataSetChanged();
        }

        public RvAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, Object o, final int position) {
            holder.setText(R.id.tv_name, o.toString());
            if (checkPosition == position) {
                holder.setImageResource(R.id.ivAvatar, R.mipmap.dialog_msg_success);
            } else {
                holder.setImageResource(R.id.ivAvatar, R.mipmap.ic_launcher);
            }

            holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check(position, true);
                }
            });

        }
    }


    private List<String> getData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            list.add("标题" + i);
        }
        return list;
    }


    public void check(int position, boolean isScroll) {
        adapter.setcheck(position);
        mRecyclerView.scrollToPosition(position);

        if (isScroll) {
            fragment.setData(position * 10 + position);
        }
    }


}
