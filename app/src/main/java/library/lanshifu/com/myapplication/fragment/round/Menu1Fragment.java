package library.lanshifu.com.myapplication.fragment.round;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;

/**
 * Created by Administrator on 2017/8/13.
 */

public class Menu1Fragment extends BaseFragment {
    @Bind(R.id.reclclerview)
    RecyclerView reclclerview;

    private List<HomeGridInfo> pageOneData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu1;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

        initData();

        reclclerview.setLayoutManager(new GridLayoutManager(getActivity(),4));
        reclclerview.setAdapter(new CommonAdapter<HomeGridInfo>(getActivity(),R.layout.item_gridview,pageOneData) {
            @Override
            protected void convert(ViewHolder holder, final HomeGridInfo homeGridInfo, int position) {
                holder.setText(R.id.grid_title,homeGridInfo.getGridTitle());
                holder.setImageResource(R.id.grid_icon,homeGridInfo.getGridIcon());
                holder.setOnClickListener(R.id.item_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        T.showShort("点击了"+homeGridInfo.getGridTitle());
                    }
                });

            }


        });

    }

    private void initData() {
        String[] gridTitles = getResources().getStringArray(R.array.home_bar_labels);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.home_bar_icon);
        for (int i = 0; i < gridTitles.length; i++) {
            if (i < 8) {
                pageOneData.add(new HomeGridInfo(typedArray.getResourceId(i,0),gridTitles[i]));
            }
        }
    }


}
