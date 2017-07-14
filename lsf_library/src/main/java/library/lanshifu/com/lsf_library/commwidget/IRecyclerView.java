package library.lanshifu.com.lsf_library.commwidget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import library.lanshifu.com.lsf_library.R;

/**
 * Created by lWX385269 on 2017/1/4.
 */

public class IRecyclerView extends FrameLayout {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View loadMoreView;
    private Context mContent;

    private boolean isRefreshing = false; //正在刷新
    private boolean isLoadingMore = false; //正在加载更多

    public IRecyclerView(Context context) {
        this(context,null);
    }

    public IRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContent = context;

        View view  = LayoutInflater.from(context).inflate(R.layout.irecycler_view, null);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mrecyclerView);
        loadMoreView = view.findViewById(R.id.loadMoreView);

        addListener();
        initRefreshLayout();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        addView(view,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
    }


    /**
     * 初始化刷新控件颜色
     */
    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,   //第一圈的颜色。。。以此类推
                android.R.color.holo_purple,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }



    /**
     * 可以自己设置刷新球的颜色
     */
    public void setColorSchemeResources(@ColorRes int... colorResIds){
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);

    }


    private void addListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(mSwipeRefreshListener !=null){
                    mSwipeRefreshListener.onRefresh();
                }
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);

                int lastVisibleItemPosition = -1;

                if(newState == RecyclerView.SCROLL_STATE_IDLE && !isLoadingMore && mSwipeRefreshListener != null){

                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if(layoutManager instanceof LinearLayoutManager){
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }

                    //最后一条
                    if(layoutManager.getChildCount() >0 && lastVisibleItemPosition>= layoutManager.getItemCount()-1){
                        setIsLoadingMore(true);
                        mSwipeRefreshListener.onLoadMore();
                    }

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }



    private void  setIsLoadingMore(boolean isLoadMore){
        this.isLoadingMore = isLoadMore;
        loadMoreView.setVisibility(isLoadMore ? VISIBLE : GONE);

    }


    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setAdapter(adapter);
    }


    /**
     * 加载更多完成
     */
    public void loadMoreComplete(){
        setIsLoadingMore(false);

    }


    /***
     * 刷新完成
     */
    public void refreshComplete(){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private OnSwipeRefreshListener mSwipeRefreshListener;

    /**
     * 设置刷新监听
     * @param mSwipeRefreshListener
     */
    public void setOnRefreshListener(OnSwipeRefreshListener mSwipeRefreshListener){
        this.mSwipeRefreshListener = mSwipeRefreshListener;

    }

    public interface OnSwipeRefreshListener {
        void onRefresh();
        void onLoadMore();
    }





}
