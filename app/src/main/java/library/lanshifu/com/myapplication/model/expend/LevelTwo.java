package library.lanshifu.com.myapplication.model.expend;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import library.lanshifu.com.myapplication.adapter.ExpendAdapter;

/**
 * Created by SKY on 2017-4-22.
 */

public class LevelTwo extends AbstractExpandableItem<LevelThree> implements MultiItemEntity{

    public String title;
    public Boolean isChecked = false;

    public LevelTwo(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return 1;
    }

    @Override
    public int getLevel() {
        return ExpendAdapter.LEVEL_TWO;
    }
}
