package library.lanshifu.com.myapplication.ui;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.FileUtil;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.MusicInfo;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;
import library.lanshifu.com.myapplication.widget.CommRecyclerView;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.FileInfo;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.FileUtils;

/**
 * Created by lanshifu on 2017/12/25.
 */

public class AudioListActivity extends BaseToolBarActivity {
    @Bind(R.id.comm_recyclerView)
    CommRecyclerView mCommRecyclerView;

    public static final String EXTRA_FILE_CHOOSER = "extra_file_chooser";
    private BaseQuickAdapter<MusicInfo, BaseViewHolder> mAdapter;
    private boolean mIsFristInsert = true;

    @Override
    protected int getLayoutid() {
        return R.layout.layout_comm_recyclerview;
    }

    @Override
    protected void onViewCreate() {
        setTBTitle("音频列表");

        mAdapter = new BaseQuickAdapter<MusicInfo, BaseViewHolder>(R.layout.item_musicfile, new ArrayList<MusicInfo>()) {
            @Override
            protected void convert(BaseViewHolder holder, final MusicInfo musicInfo) {
                holder.setText(R.id.tv_name, musicInfo.getFilename());
                holder.setText(R.id.tv_size, musicInfo.getFileSize());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickItem(musicInfo);
                    }
                });
            }
        };
        mCommRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommRecyclerView.setAdapter(mAdapter);
        mCommRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData();
            }
        });
        mCommRecyclerView.setEnableLoadmore(false);
        checkSDPermission();

    }

    private void loadData() {
        Observable.create(new ObservableOnSubscribe<List<MusicInfo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<MusicInfo>> e) throws Exception {
                List<MusicInfo> all = DataSupport.findAll(MusicInfo.class);
                if (mIsFristInsert && all.size() > 1) {
                    mIsFristInsert = true;
                    e.onNext(all);
                } else {
                    List<MusicInfo> musicInfos = updateFileItems();
                    e.onNext(musicInfos);
                }
            }
        }).compose(RxSchedulerHelper.<List<MusicInfo>>io_main())
                .subscribe(new MyObserver<List<MusicInfo>>() {
                    @Override
                    public void _onNext(List<MusicInfo> musicInfos) {
                        mAdapter.replaceData(musicInfos);
                        mCommRecyclerView.finishRefresh();
                    }

                    @Override
                    public void _onError(String e) {
                        mCommRecyclerView.finishRefresh();
                        showErrorToast(e);
                    }
                });


    }

    private List<MusicInfo> updateFileItems() {
        List<MusicInfo> list = new ArrayList<>();
        List<FileInfo> typeFiles = FileUtils.getSpecificTypeFiles(mContext, new String[]{FileInfo.EXTEND_MP3});
        DataSupport.deleteAll(MusicInfo.class);
        for (FileInfo fileInfo : typeFiles) {
            MusicInfo musicInfo = new MusicInfo(fileInfo.getFilePath(), fileInfo.getName(), fileInfo.getSize() + "");
            musicInfo.save();
            list.add(musicInfo);
        }
        return list;
    }

    private void checkSDPermission() {
        new RxPermissions(this)
                .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mCommRecyclerView.autoRefresh();
                        } else {
                            T.showShort("权限拒绝");
                        }
                    }
                });
    }

    private void clickItem(MusicInfo info) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FILE_CHOOSER, info.getFilepath());
        setResult(RESULT_OK, intent);
        finish();
    }

}
