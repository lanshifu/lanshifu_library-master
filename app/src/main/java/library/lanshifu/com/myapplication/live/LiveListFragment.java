package library.lanshifu.com.myapplication.live;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.DividerItemDecoration;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.basemvp.BaseModle;
import library.lanshifu.com.lsf_library.basemvp.NullModle;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.LiveCategory;
import library.lanshifu.com.myapplication.mvp.contract.LiveListFragmentContract;
import library.lanshifu.com.myapplication.mvp.presenter.LiveListFragmentPresenter;

/**
 * Created by Administrator on 2017/8/13.
 */

public class LiveListFragment extends BaseFragment<LiveListFragmentPresenter,BaseModle> implements LiveListFragmentContract.View {
    @Bind(R.id.recycler)
    RecyclerView recyclerView;

    private List<LiveBean> liveBeanList = new ArrayList<>();

    private String []names = new String[]{
            "香港电影","综艺频道","高清音乐","动作电影","电影","周星驰","成龙","喜剧","儿歌","LIVE生活"
    };

    private String []urls = new String[]{
            "http://live.gslb.letv.com/gslb?stream_id=lb_hkmovie_1300&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=letv&expect=1",
            "http://live.gslb.letv.com/gslb?stream_id=lb_ent_1300&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=letv&expect=1",
            "http://live.gslb.letv.com/gslb?stream_id=lb_music_1300&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=letv&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_dzdy_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_movie_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_zxc_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_cl_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_comedy_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_erge_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_livemusic_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1"
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_livelist;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this,mModle);


    }

    @Override
    protected void initView() {

        initData();
//        mPresenter.getLiveList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(new CommonAdapter<LiveBean>(getActivity(),R.layout.item_live,liveBeanList) {
            @Override
            protected void convert(ViewHolder holder, LiveBean liveBean, int position) {
                String uri = liveBean.url;
                String title = liveBean.name;
                JCVideoPlayerStandard jcVideoPlayerStandard = holder.getView(R.id.videoplayer);
                jcVideoPlayerStandard.setUp(uri,JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);

            }

        });

    }

    private void initData() {
        for (int i = 0; i < names.length; i++) {
            liveBeanList.add(new LiveBean(names[i],urls[i]));
        }
    }



    class LiveBean{

        public String name;
        public String url;
        public LiveBean(String name, String url) {
            this.name = name;
            this.url = url;
        }

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
            JCVideoPlayer.releaseAllVideos();
        } else {// 重新显示到最前端中

        }
    }



    @Override
    public void returnLiveList(List<LiveCategory> liveCategoryList) {
        showShortToast("成功："+liveCategoryList.size());
        loge(liveCategoryList.get(0).toString());

    }

    @Override
    public void showProgressDialog(String text) {
        startProgressDialog();

    }

    @Override
    public void hideProgressDialog() {
        stopProgressDialog();

    }

    @Override
    public void showError(String error) {
        showShortToast(error);

    }
}
