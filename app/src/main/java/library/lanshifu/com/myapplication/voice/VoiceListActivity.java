package library.lanshifu.com.myapplication.voice;

import android.widget.ListView;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;

public class VoiceListActivity extends BaseToolBarActivity {


    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.button_rec)
    RecordVoiceButton buttonRec;
    private VoiceAdapter adapter;



    @Override
    protected int getLayoutid() {
        return R.layout.activity_voice_list;
    }

    @Override
    protected void onViewCreate() {
        setTBTitle("录音播放");

        adapter = new VoiceAdapter(this);
        lv.setAdapter(adapter);
        buttonRec.setEnrecordVoiceListener(new RecordVoiceButton.EnRecordVoiceListener() {
            @Override
            public void onFinishRecord(long length, String strLength, String filePath) {

                adapter.add(new Voice(length, strLength, filePath));
            }
        });

    }

}
