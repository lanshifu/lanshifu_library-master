package library.lanshifu.com.myapplication.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.SystemManage;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.utils.Mp3CutLogic;
import library.lanshifu.com.myapplication.utils.StorageUtil;
import library.lanshifu.com.myapplication.widget.CustomRangeSeekBar;

/**
 * Created by lanshifu on 2017/12/25.
 */

public class AudioCutActivity extends BaseToolBarActivity {
    private static final int REQUEST_CODE_AUDIL_LIST = 20;
    private static final int REQUEST_CODE_ASK_WRITE_SETTINGS = 21;
    @Bind(R.id.iv_player_min_voice)
    ImageView mIvPlayerMinVoice;
    @Bind(R.id.iv_player_max_voice)
    ImageView mIvPlayerMaxVoice;
    @Bind(R.id.seekbar_voice)
    SeekBar mSeekbarVoice;
    @Bind(R.id.rl_player_voice)
    RelativeLayout mRlPlayerVoice;
    @Bind(R.id.range_seekbar)
    CustomRangeSeekBar mRangeSeekbar;
    @Bind(R.id.btn_choose_audio)
    Button mBtnChooseAudio;
    @Bind(R.id.btn_play_pause)
    Button mBtnPlayPause;
    @Bind(R.id.btn_translate)
    Button mBtnTranslate;
    @Bind(R.id.btn_save)
    Button mBtnSave;

    private String mSelMusicPath;
    public MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    // 标识当前播放的滑块为min or max
    private boolean mIsMinFlag = true;

    private Disposable mDisposable;
    private Observable<Long> mUpdateProgressObservable = Observable.interval(0, 1, TimeUnit.SECONDS);
    private Consumer mUpdateProgressConsumer = new Consumer() {
        @Override
        public void accept(Object o) throws Exception {
            int curPosition = mMediaPlayer.getCurrentPosition();
            Number maxValue = mRangeSeekbar.getAbsoluteMaxValue();

            if (!setSeekBarProgressValue(curPosition, mIsMinFlag) || curPosition >= maxValue
                    .intValue()) {
                pause();
            }
        }
    };

    @Override
    protected int getLayoutid() {
        return R.layout.activity_audio_cut;
    }

    @Override
    protected void onViewCreate() {
        mMediaPlayer = new MediaPlayer();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mRangeSeekbar.setThumbListener(mThumbListener);

        mSeekbarVoice.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        mSeekbarVoice.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        mSeekbarVoice.setOnSeekBarChangeListener(mVoiceChangeListener);
    }

    /**
     * 声音滑块滑动事件
     */
    private SeekBar.OnSeekBarChangeListener mVoiceChangeListener = new SeekBar.OnSeekBarChangeListener() {

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 设置音量
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        }
    };


    // 音乐滑块事件
    private CustomRangeSeekBar.ThumbListener mThumbListener = new CustomRangeSeekBar.ThumbListener() {

        @Override
        public void onClickMinThumb(Number max, Number min) {
        }

        @Override
        public void onClickMaxThumb() {

        }

        @Override
        public void onMinMove(Number max, Number min) {
            seekToForIsMin(true);
        }

        @Override
        public void onMaxMove(Number max, Number min) {
            seekToForIsMin(false);
        }

        @Override
        public void onUpMinThumb(Number max, Number min) {
        }

        @Override
        public void onUpMaxThumb() {
        }
    };


    @OnClick({R.id.btn_choose_audio, R.id.btn_play_pause, R.id.btn_translate, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_choose_audio:
                openFile();
                break;
            case R.id.btn_play_pause:
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    if (mDisposable != null) {
                        mDisposable.dispose();
                        mDisposable = null;
                    }
                    return;
                }
                if (TextUtils.isEmpty(mSelMusicPath)) {
                    showErrorToast("请选择音乐");
                    return;
                }
                seekToForIsMin();
                play();
                //开启进度rx轮询事件
                mDisposable = mUpdateProgressObservable.observeOn(AndroidSchedulers.mainThread()).
                        subscribe(mUpdateProgressConsumer);
                break;
            case R.id.btn_translate:
                mIsMinFlag = !mIsMinFlag;
                seekToForIsMin();
                break;
            case R.id.btn_save:
                if (isSelectedMp3()) {
                    final Number minNumber = mRangeSeekbar.getSelectedAbsoluteMinValue();
                    final Number maxNumber = mRangeSeekbar.getSelectedAbsoluteMaxValue();
                    if (maxNumber.intValue() <= minNumber.longValue()) {
                        showErrorToast("截取的文件长度必须大于0");
                        return;
                    }
                    showInputNameDialog(minNumber.longValue(),maxNumber.longValue());

                }

                break;
        }
    }

    private void showInputNameDialog(final long min, final long max) {
        final EditText editText  = new EditText(this);
        editText.setWidth(200);
        new AlertDialog.Builder(this)
                .setTitle("输入剪切后的名字")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editText.getText().toString();
                        if (TextUtils.isEmpty(name)){
                            showErrorToast("名字不能为空");
                            return;
                        }
                        doCutter(name,min,max);

                    }
                }).show();

    }


    public boolean isSelectedMp3() {
        if (TextUtils.isEmpty(mSelMusicPath)) {
            showErrorToast("请先选择音乐");
            return false;
        }
        return true;
    }

    public boolean setSeekBarProgressValue(int value, boolean isMin) {
        if (isMin) {
            return mRangeSeekbar.setSelectedAbsoluteMinValue(value);
        } else {
            return mRangeSeekbar.setSelectedAbsoluteMaxValue(value);
        }
    }

    private void play() {
        mMediaPlayer.start();
        setPlayBtnWithStatus(true);
    }

    private void seekToForIsMin() {
        int curValue;
        if (mIsMinFlag) {
            curValue = ((Number) mRangeSeekbar.getSelectedAbsoluteMinValue()).intValue();
        } else {
            curValue = ((Number) mRangeSeekbar.getSelectedAbsoluteMaxValue()).intValue();
        }
        mMediaPlayer.seekTo(curValue);
    }

    private void seekToForIsMin(boolean isMinBar) {
        int curValue;
        if (judgeIsPlayingThumb(isMinBar)) {
            if (isMinBar) {
                curValue = ((Number) mRangeSeekbar.getSelectedAbsoluteMinValue()).intValue();
            } else {
                curValue = ((Number) mRangeSeekbar.getSelectedAbsoluteMaxValue()).intValue();
            }
            mMediaPlayer.seekTo(curValue);
        }
    }

    /**
     * //判断滑动的滑块是否为当前正在播放的滑块
     *
     * @param isMinBar 滑动的滑块 true: min  false: max
     */
    private boolean judgeIsPlayingThumb(boolean isMinBar) {
        boolean isPlaying = false;
        if (mIsMinFlag) {
            if (isMinBar) {
                isPlaying = true;
            }
        } else {
            if (!isMinBar) {
                isPlaying = true;
            }
        }
        return isPlaying;
    }

    private void openFile() {
        startActivityForResult(AudioListActivity.class, REQUEST_CODE_AUDIL_LIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_AUDIL_LIST) {
            mSelMusicPath = data.getStringExtra(AudioListActivity.EXTRA_FILE_CHOOSER);
            try {
                if (!TextUtils.isEmpty(mSelMusicPath)) {
                    resetSeekBarSelValue();
                    setPlayBtnWithStatus(false);
                    setSeekBarEnable(true);
                    pause();
                    reset();
                    setDataSource(mSelMusicPath);
                    prepare();
                    setSeekBarMaxValue(getDuration());
                    addBarGraphRenderers();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == REQUEST_CODE_ASK_WRITE_SETTINGS){

        }

    }

    private void addBarGraphRenderers() {
        //添加音频渲染
    }

    private void setSeekBarMaxValue(int duration) {
        mRangeSeekbar.setAbsoluteMaxValue(duration);
    }

    private int getDuration() {
        return mMediaPlayer.getDuration();
    }

    private void prepare() {
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDataSource(String path) {
        try {
            mMediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        mMediaPlayer.reset();
    }

    private void pause() {
        mMediaPlayer.pause();
        setPlayBtnWithStatus(false);
        cancelUpdateProgress();
    }

    /**
     * 取消更新seekbar rx轮询事件
     */
    private void cancelUpdateProgress() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private void setSeekBarEnable(boolean isClickable) {
        mRangeSeekbar.setEnabled(isClickable);
    }

    private void setPlayBtnWithStatus(boolean isPlaying) {
        mBtnPlayPause.setText(isPlaying ? "暂停" : "播放");

    }

    private void resetSeekBarSelValue() {
        mRangeSeekbar.restorePercentSelectedMinValue();
        mRangeSeekbar.restorePercentSelectedMaxValue();
    }

    /**
     * 剪切音乐
     */
    public void doCutter(final String fileName, final long minValue, final long maxValue) {
        final String mp3_cutDir = StorageUtil.getDir("mp3_cut");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                if (!TextUtils.isEmpty(fileName)) {
                    Mp3CutLogic helper = new Mp3CutLogic(new File(mSelMusicPath));
                    String targetMp3FilePath = mp3_cutDir + "/" + fileName + ".mp3";
                    try {
                        helper.generateNewMp3ByTime(targetMp3FilePath, minValue, maxValue);
                        addMp3ToDb(targetMp3FilePath);
                        e.onNext(targetMp3FilePath);
                    } catch (Exception e1) {
                        e.onError(e1);
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void _onNext(String value) {
                        String cutterPath = value;
                        doCutterSuccess(cutterPath);
                    }

                    @Override
                    public void _onError(String e) {
                        showErrorToast("裁剪失败 " + e);
                        L.d(e);
                    }
                });
    }



    private void doCutterSuccess(final String cutterPath) {
        showShortToast("裁剪成功，保存在 "+cutterPath);
        new AlertDialog.Builder(this)
                .setTitle("是否设置为铃声")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkPermissionAndSettingVoice(cutterPath);
                    }
                }).show();

    }

    private void checkPermissionAndSettingVoice(final String cutterPath) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, REQUEST_CODE_ASK_WRITE_SETTINGS);
            } else {
                SystemManage.setMyRingtone(cutterPath, AudioCutActivity.this);
            }
        }
    }

    private void addMp3ToDb(String targetMp3FilePath) {

    }

}
