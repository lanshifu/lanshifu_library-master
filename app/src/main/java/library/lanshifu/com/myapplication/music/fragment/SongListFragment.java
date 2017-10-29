package library.lanshifu.com.myapplication.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.WrapperSongListInfo;
import library.lanshifu.com.myapplication.music.activity.MusicSongListDetailActivity;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RetrofitHelper;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;
import library.lanshifu.com.myapplication.widget.CommRecyclerView;

/**
 * Created by lanshifu on 2017/9/21.
 */

public class SongListFragment extends BaseFragment {
    @Bind(R.id.recyclerView)
    CommRecyclerView recyclerView;
    private BaseQuickAdapter<WrapperSongListInfo.SongListInfo, BaseViewHolder> adapter;

    public static final String MUSIC_URL_FORMAT = "json";
    public static final  String MUSIC_URL_FROM = "webapp_music";
    public static final String MUSIC_URL_METHOD_GEDAN ="baidu.ting.diy.gedan";
    private int pageSize =12;
    private int startPage =1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song_list;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setItemPrefetchEnabled(false);
        recyclerView.setLayoutManager(manager);
        adapter = new BaseQuickAdapter<WrapperSongListInfo.SongListInfo, BaseViewHolder>(R.layout.item_songlist, new ArrayList<WrapperSongListInfo.SongListInfo>()) {
            @Override
            protected void convert(BaseViewHolder holder, final WrapperSongListInfo.SongListInfo info) {


                int count = Integer.parseInt(info.getListenum());
                if (count > 10000) {
                    count = count / 10000;
                    holder.setText(R.id.tv_songlist_count,count+"万");
                } else {
                    holder.setText(R.id.tv_songlist_count,info.getListenum());
                }
                holder.setText(R.id.tv_songlist_name,info.getTitle());
               ImageView imageView =  holder.getView(R.id.iv_songlist_photo);
                Glide.with(getActivity())
                        .load(info.getPic_300())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(imageView);
                holder.getView(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MusicSongListDetailActivity.class);
                        intent.putExtra("songListId",info.getListid());
                        intent.putExtra("islocal", false);
                        intent.putExtra("songListPhoto", info.getPic_300());
                        intent.putExtra("songListname", info.getTitle());
                        intent.putExtra("songListTag", info.getTag());
                        intent.putExtra("songListCount", info.getListenum());
                        getActivity().startActivity(intent);
                    }
                });

            }

        };
        recyclerView.setAdapter(adapter);
        recyclerView.autoRefresh();
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                startPage = 1;
                recyclerView.setEnableLoadmore(true);
                request();
            }
        });
        recyclerView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                ++startPage;
                request();

            }
        });




    }

    private void request() {
        RetrofitHelper.getMusicApi().getSongListAll(MUSIC_URL_FORMAT,MUSIC_URL_FROM,MUSIC_URL_METHOD_GEDAN,pageSize,startPage)
                .compose(RxSchedulerHelper.<WrapperSongListInfo>io_main())
                .subscribe(new MyObserver<WrapperSongListInfo>() {
                    @Override
                    public void _onNext(WrapperSongListInfo wrapperSongListInfo) {
                        recyclerView.finishLoadmore(true);
                        recyclerView.finishRefresh(true);
                        List<WrapperSongListInfo.SongListInfo> songListInfos = wrapperSongListInfo.getContent();
                        if (songListInfos != null && songListInfos.size() != 0) {
                            if (startPage == 1) {
                                //第一次加载
                                adapter.replaceData(songListInfos);
                                L.d("第一次加载："+songListInfos.size());
                            } else {
                                //加载更多
                                adapter.addData(songListInfos);
                                L.d("数据集大小："+songListInfos.size());
                            }
                        } else {
                            startPage--;
                            recyclerView.setLoadmoreFinished(true);
                            recyclerView.setEnableLoadmore(false);
                        }
                    }

                    @Override
                    public void _onError(String e) {
                        recyclerView.finishLoadmore(false);
                        recyclerView.finishRefresh(false);
                    }
                });
    }


}
