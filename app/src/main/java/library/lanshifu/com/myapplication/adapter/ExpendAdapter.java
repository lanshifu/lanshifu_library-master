package library.lanshifu.com.myapplication.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.expend.LevelOne;
import library.lanshifu.com.myapplication.model.expend.LevelThree;
import library.lanshifu.com.myapplication.model.expend.LevelTwo;

/**
 * Created by lanshifu on 2017/9/7.
 */

public class ExpendAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private Context mContext;
    public static final int LEVEL_ONE = 0;
    public static final int LEVEL_TWO = 1;
    public static final int LEVEL_THREE = 2;


    public ExpendAdapter(Context context, List<MultiItemEntity> data) {
        super(data);
        this.mContext = context;
        addItemType(LEVEL_ONE, R.layout.level_one);
        addItemType(LEVEL_TWO, R.layout.level_two);
        addItemType(LEVEL_THREE, R.layout.level_three);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {

            case LEVEL_ONE:
                final LevelOne one = (LevelOne) item;
                helper.setText(R.id.tv_level_one, one.title);

                final List<LevelTwo> twos = one.getSubItems();
                boolean isTowAllCheck = true;
                for (LevelTwo subItem : twos) {
                    if (!subItem.isChecked) {
                        isTowAllCheck = false;
                    }
                }

                boolean oneCheck = one.isChecked || isTowAllCheck;
                helper.setChecked(R.id.cb_one, oneCheck);

                helper.getView(R.id.cb_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        one.isChecked = !one.isChecked;
                        //将二级全选/反选
                        for (LevelTwo levelTwo : twos) {
                            levelTwo.isChecked = one.isChecked;
                        }
                        notifyDataSetChanged();
                    }
                });

                break;

            case LEVEL_TWO:
                final LevelTwo levelTwo = (LevelTwo) item;
                helper.setText(R.id.tv_level_two, levelTwo.title);
                helper.getView(R.id.ll_level_two).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (levelTwo.isExpanded()) {
                            collapse(pos, true);
                        } else {
                            expand(pos, true);
                        }
                    }
                });

                final List<LevelThree> levelThrees = levelTwo.getSubItems();
                boolean isThreeAllCheck = true;
                for (LevelThree levelThree : levelThrees) {
                    if (!levelThree.isChecked) {
                        isThreeAllCheck = false;
                    }
                }

                boolean twoCheck = levelTwo.isChecked || isThreeAllCheck;
                helper.setChecked(R.id.cb_two, twoCheck);

                helper.getView(R.id.cb_two).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        levelTwo.isChecked = !levelTwo.isChecked;
                        //三级全选、反选
                        for (LevelThree levelThree : levelThrees) {
                            levelThree.isChecked = levelTwo.isChecked;
                        }
                        notifyDataSetChanged();
                    }
                });

                break;

            case LEVEL_THREE:
                final LevelThree three = (LevelThree) item;
                helper.setText(R.id.tv_level_three, three.getTitle());
                helper.setText(R.id.tv_level_three_desc, three.getDesc());

                // 判断 记住的状态
                helper.setChecked(R.id.cb_three, three.isChecked);

                // 设置复选框的点击事件
                helper.getView(R.id.cb_three).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        three.isChecked = !three.isChecked;
                        notifyDataSetChanged();
                        // 使用此句可能会造成item 无法伸缩,并有可能造成出现多个 item
                        //     getData().set(helper.getLayoutPosition(), (MultiItemEntity) three);

                    }
                });
                break;

        }

    }
}
