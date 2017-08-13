package library.lanshifu.com.myapplication.live;

import android.app.Activity;
import android.content.Intent;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;

/**
 * Created by Administrator on 2017/8/13.
 */

public class LiveActivity extends BaseToolBarActivity {

    public static final String TITLE = "title";
    public static final String URI = "URI";

    private static String title;
    private static String uri;
    @Bind(R.id.videoplayer)
    JCVideoPlayerStandard jcVideoPlayerStandard;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_live;
    }


    @Override
    protected void onViewCreate() {
        title = getIntent().getStringExtra(TITLE);
        uri = getIntent().getStringExtra(URI);
        setTBTitle(title);
        initPlayer();

    }

    private void initPlayer() {
        jcVideoPlayerStandard.setUp(uri
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
