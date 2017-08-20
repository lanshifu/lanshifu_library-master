package library.lanshifu.com.myapplication.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Created by Administrator on 2017/8/20.
 */

public class CommRecyclerView extends SmartRefreshLayout {

    private RecyclerView recyclerView;

    public CommRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public CommRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(params);
        addView(recyclerView);
    }

    //recyclerview的方法,有需要的添加进去
    public void setAdapter(RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration){
        recyclerView.addItemDecoration(decoration);
    }

   public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        recyclerView.setLayoutManager(layoutManager);
    }



}
