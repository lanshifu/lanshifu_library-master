package library.lanshifu.com.myapplication.twolist;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;

/**
 * Created by lanxiaobin on 2017/7/17.
 */

public class ShotDetailFragment extends BaseFragment implements ItemHeaderDecoration.CheckListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<SortBean> mDatas = new ArrayList<>();
    private TowListAdapter adapter;
    private GridLayoutManager mManager;
    private boolean move;
    private int mIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.fragemnt_sort_detail;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

        initData(15);

        mManager = new GridLayoutManager(getContext(), 3);
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mDatas.get(position).isTitle() ? 3 : 1;
            }
        });
        recyclerView.setLayoutManager(mManager);
        ItemHeaderDecoration mDecoration = new ItemHeaderDecoration(getContext(), mDatas);
        mDecoration.setCheckListener(this);
        recyclerView.addItemDecoration(mDecoration);

        adapter = new TowListAdapter(getContext(), mDatas) {
            @Override
            public void convert(ViewHolder holder, final SortBean sortBean, int position) {
                if (mDatas.get(position).isTitle()) {
                    holder.setText(R.id.tv_title, "测试数据" + sortBean.getName());
                    holder.setOnClickListener(R.id.root, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            T.showShort("点击了title:" + sortBean.getName());
                        }
                    });
                } else {

                    holder.setText(R.id.tv_name, sortBean.getName());
                    holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            T.showShort("详情点击了" + sortBean.getName());
                        }
                    });
                }
//
            }


        };
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerViewListener());


    }


    private void initData(int count) {
        for (int i = 0; i < count; i++) {
            SortBean titleBean = new SortBean(String.valueOf(i));
            titleBean.setTitle(true);//头部设置为true
            titleBean.setTag(String.valueOf(i));
            mDatas.add(titleBean);
            for (int j = 0; j < 10; j++) {
                SortBean sortBean = new SortBean(String.valueOf(i + "行" + j + "个"));
                sortBean.setTag(String.valueOf(i));
                mDatas.add(sortBean);
            }
        }
    }


    @Override
    public void check(int position, boolean isScroll) {
        ((TwoListActivity) getActivity()).check(position, isScroll);
    }

    public void setData(int n) {
        if (n < 0 || n >= adapter.getItemCount()) {
            T.showShort("超出范围了");
            return;
        }
        mIndex = n;
        recyclerView.stopScroll();
        smoothMoveToPosition(n);
    }


    private void smoothMoveToPosition(int n) {
        int firstItem = mManager.findFirstVisibleItemPosition();
        int lastItem = mManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            recyclerView.smoothScrollToPosition(n);
        } else if (n <= lastItem) {
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.smoothScrollBy(0, top);
        } else {
            recyclerView.smoothScrollToPosition(n);
            move = true;
        }
    }


    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                Log.d("n---->", String.valueOf(n));
                if (0 <= n && n < recyclerView.getChildCount()) {
                    int top = recyclerView.getChildAt(n).getTop();
                    Log.d("top--->", String.valueOf(top));
                    recyclerView.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }


}
