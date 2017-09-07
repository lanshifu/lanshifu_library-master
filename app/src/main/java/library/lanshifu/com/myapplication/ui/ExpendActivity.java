package library.lanshifu.com.myapplication.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.adapter.ExpendAdapter;
import library.lanshifu.com.myapplication.model.expend.LevelOne;
import library.lanshifu.com.myapplication.model.expend.LevelThree;
import library.lanshifu.com.myapplication.model.expend.LevelTwo;

public class ExpendActivity extends BaseToolBarActivity {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<MultiItemEntity> multiList;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_expend;
    }

    @Override
    protected void onViewCreate() {

        multiList = generateData();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ExpendAdapter adapter = new ExpendAdapter(this, multiList);
        recyclerView.setAdapter(adapter);

        // 使一级列表默认展开
        for (int i = multiList.size() - 1; i >= 0; i--) {
            adapter.expand(i, false, false);
        }

    }


    private ArrayList<MultiItemEntity> generateData() {

        int levelOne = 10;
        int levelTwo = 3;

        ArrayList<MultiItemEntity> res = new ArrayList<>();

        for (int i = 0; i < levelOne; i++) {

            LevelOne lv1 = new LevelOne("一级列表" + i);

            for (int j = 0; j < levelTwo; j++) {

                LevelTwo lv2 = new LevelTwo("二级列表：" + j);

                lv2.addSubItem(new LevelThree("三级列表" + j, "德玛西亚：" + j
                ));

                lv1.addSubItem(lv2);
            }
            res.add(lv1);
        }
        return res;
    }
}
