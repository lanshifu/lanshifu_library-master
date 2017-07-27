package library.lanshifu.com.myapplication.voice;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;

public class VoiceListActivity extends BaseToolBarActivity {


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    @Bind(R.id.lv)
    RecyclerView lv;
    @Bind(R.id.button_rec)
    RecordVoiceButton buttonRec;
    private CommonAdapter adapter;

    // 语音动画
    private AnimationDrawable voiceAnimation;
    private VoiceManager voiceManager;


    @Override
    protected int getLayoutid() {
        return R.layout.activity_voice_list;
    }

    @Override
    protected void onViewCreate() {
        setTBTitle("录音播放");

        voiceManager = VoiceManager.getInstance(this);
        adapter = new CommonAdapter<Voice>(this, R.layout.item_voice_list, new ArrayList<Voice>()) {
            @Override
            protected void convert(final ViewHolder holder, final Voice voice, final int position) {

                holder.setText(R.id.tv_length, voice.getStrLength());
                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (voiceManager.isPlaying()) {
                            voiceManager.stopPlay();
                        } else {
                            voiceManager.startPlay(voice.getFilePath());
                        }
                    }
                });

            }
// Dangerous

        };


        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(adapter);
        buttonRec.setEnrecordVoiceListener(new RecordVoiceButton.EnRecordVoiceListener() {
            @Override
            public void onFinishRecord(long length, String strLength, String filePath) {
                adapter.add(new Voice(length, strLength, filePath));
            }
        });




    }



}
