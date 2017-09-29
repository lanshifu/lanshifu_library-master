package library.lanshifu.com.myapplication.music.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.SongDetailInfo;
import library.lanshifu.com.myapplication.model.SongListDetail;
import library.lanshifu.com.myapplication.music.service.MediaPlayService;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RetrofitHelper;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;

/**
 * Created by lanshifu on 2017/9/22.
 */

public class MusicSongListDetailActivity extends BaseActivity {
    @Bind(R.id.album_art)
    ImageView albumArt;
    @Bind(R.id.overlay)
    View overlay;
    @Bind(R.id.iv_songlist_photo)
    ImageView ivSonglistPhoto;
    @Bind(R.id.tv_songlist_count)
    TextView tvSonglistCount;
    @Bind(R.id.fra)
    FrameLayout fra;
    @Bind(R.id.tv_songlist_name)
    TextView tvSonglistName;
    @Bind(R.id.tv_songlist_detail)
    TextView tvSonglistDetail;
    @Bind(R.id.iv_collect)
    ImageView ivCollect;
    @Bind(R.id.ll_collect)
    LinearLayout llCollect;
    @Bind(R.id.iv_comment)
    ImageView ivComment;
    @Bind(R.id.ll_comment)
    LinearLayout llComment;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.ll_share)
    LinearLayout llShare;
    @Bind(R.id.ll_download)
    LinearLayout llDownload;
    @Bind(R.id.headerdetail)
    RelativeLayout headerdetail;
    @Bind(R.id.headerview)
    FrameLayout headerview;
    @Bind(R.id.rl_toobar)
    RelativeLayout rlToobar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bottom_container)
    FrameLayout bottomContainer;
    @Bind(R.id.iv_playing_pre)
    ImageView ivPlayingPre;
    @Bind(R.id.iv_playing_play)
    ImageView ivPlayingPlay;
    @Bind(R.id.iv_playing_next)
    ImageView ivPlayingNext;


    private String songListid;
    private boolean isLocal;
    private String photoUrl;
    private String listName;
    private String detail;
    private String count;
    private static Context mContext;
    private static int radius = 25;
    private List<SongListDetail.SongDetail> mList = new ArrayList<>();
    private Intent mIntent;
//        private static MediaPlayService.MediaBinder mMediaBinder;
    private List<SongListDetail.SongDetail> mMReturnList;
        private MediaPlayService mService;
    private boolean isPlayAll = false;
        private MediaServiceConnection mConnection;
    private static MediaPlayService.MediaBinder mMediaBinder;
    //song_id 对应的在集合中的位置
    private HashMap<String, Integer> positionMap = new HashMap<>();
    //请求返回的SongDetailInfo先存放在数组中，对应下标索引是其在集合中所处位置
//    private SongDetailInfo[] mInfos;
    //指示现在加入musicList集合中的元素下标应该是多少
    AtomicInteger index = new AtomicInteger(0);

    public static final String MUSIC_URL_FROM = "webapp_music";
    public static final String MUSIC_URL_VERSION = "5.6.5.6";
    public static final String MUSIC_URL_FORMAT = "json";
    public static final String MUSIC_URL_METHOD_SONGLIST_DETAIL = "baidu.ting.diy.gedanInfo";
    private BaseQuickAdapter<SongListDetail.SongDetail, BaseViewHolder> adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_songlist_detail;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            songListid = (String) extras.get("songListId");
            isLocal = (boolean) extras.get("islocal");
            photoUrl = (String) extras.get("songListPhoto");
            listName = (String) extras.get("songListname");
            detail = (String) extras.get("songListTag");
            count = (String) extras.get("songListCount");
            setUI();
            initRecyclerView();
            requestSongList();
            initServices();
        }

    }

    private void initServices() {
        if (mIntent == null) {
            mIntent = new Intent(this, MediaPlayService.class);
            mConnection = new MediaServiceConnection();
            startService(mIntent);
            bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        }
    }

    private void initRecyclerView() {
        //                holder.setText(R.id.tv_trackNumber,);
        adapter = new BaseQuickAdapter<SongListDetail.SongDetail, BaseViewHolder>(R.layout.item_songlist_detail_list, new ArrayList<SongListDetail.SongDetail>()) {
            @Override
            protected void convert(BaseViewHolder holder, final SongListDetail.SongDetail detail) {

                int adapterPosition = holder.getAdapterPosition() + 1;
                holder.setText(R.id.tv_song_title, detail.getTitle());
                holder.setText(R.id.tv_song_artist, detail.getAuthor());
                holder.setText(R.id.tv_trackNumber, adapterPosition + "");
                holder.getView(R.id.rl_song).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showShortToast(detail.getTitle());
                        //请求详情，播放
                        requestSongDetail(detail.getSong_id());

                    }
                });
                holder.getView(R.id.popup_menu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showShortToast("more " + detail.getTitle());
                    }
                });

            }


        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setUI() {
        Glide.with(this)
                .load(photoUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivSonglistPhoto);

        tvSonglistCount.setText(count);
        tvSonglistName.setText(listName);
        String[] split = detail.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("标签：");
        for (int i = 0; i < split.length; i++) {
            stringBuffer.append(split[i] + " ");
        }
        tvSonglistDetail.setText(stringBuffer);

    }

    private void requestSongList() {
        RetrofitHelper.getMusicApi().getSongListDetail(MUSIC_URL_FORMAT, MUSIC_URL_FROM, MUSIC_URL_METHOD_SONGLIST_DETAIL, songListid)
                .compose(RxSchedulerHelper.<SongListDetail>io_main())
                .subscribe(new MyObserver<SongListDetail>() {
                    @Override
                    public void _onNext(SongListDetail songListDetail) {

                        if (songListDetail.getContent() != null && songListDetail.getContent().size() > 0) {
                            adapter.addData(songListDetail.getContent());
                        }
                    }

                    @Override
                    public void _onError(String e) {
                        showErrorToast(e);
                    }
                });
    }


    private void requestSongDetail(String songId){
        RetrofitHelper.getMusicApi().getSongDetail("android","5.6.5.6","json","baidu.ting.song.play",songId)
                .compose(RxSchedulerHelper.<SongDetailInfo>io_main())
                .subscribe(new MyObserver<SongDetailInfo>() {
                    @Override
                    public void _onNext(SongDetailInfo songDetailInfo) {
                        playMusic(songDetailInfo);

                    }

                    @Override
                    public void _onError(String e) {
                        showErrorToast(e);
                    }
                });

    }


    private void playMusic(SongDetailInfo songDetailInfo) {
        if(songDetailInfo.getSonginfo() == null){
            showErrorToast("songDetailInfo == null ");
            return;
        }

        if(mService == null){
            mService = mMediaBinder.getMediaPlayService();
        }

        mService.playSong(songDetailInfo,false);

    }



    private static class MediaServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMediaBinder = (MediaPlayService.MediaBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}
