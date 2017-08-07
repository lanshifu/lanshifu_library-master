package library.lanshifu.com.myapplication.twolist;

import android.content.Context;

import java.util.List;

import library.lanshifu.com.lsf_library.adapter.recyclerview.MultiItemTypeAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ItemViewDelegate;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.myapplication.R;

/**
 * Created by lanxiaobin on 2017/7/17.
 */

public abstract class TowListAdapter extends MultiItemTypeAdapter<SortBean> {

    private static final int VIEWTYPE_1 = 445;
    private static final int VIEWTYPE_2 = 446;

    public TowListAdapter(Context context, List<SortBean> datas) {
        super(context, datas);


        addItemViewDelegate(VIEWTYPE_1, new ItemViewDelegate<SortBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_title;
            }

            @Override
            public boolean isForViewType(SortBean item, int position) {
                return item.isTitle();
            }

            @Override
            public void convert(ViewHolder holder, SortBean sortBean, int position) {
                TowListAdapter.this.convert(holder,sortBean,position);

            }
        });

        addItemViewDelegate(VIEWTYPE_2, new ItemViewDelegate<SortBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_classify_detail;
            }

            @Override
            public boolean isForViewType(SortBean item, int position) {
                return !item.isTitle();
            }

            @Override
            public void convert(ViewHolder holder, SortBean sortBean, int position) {
                TowListAdapter.this.convert(holder,sortBean,position);

            }
        });
    }

    public abstract void convert(ViewHolder holder, SortBean sortBean, int position);

}
