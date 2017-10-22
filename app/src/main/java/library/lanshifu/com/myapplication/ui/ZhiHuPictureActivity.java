package library.lanshifu.com.myapplication.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.net.MyObserver;

/**
 * Created by lanxiaobin on 2017/10/19.
 */

public class ZhiHuPictureActivity extends BaseToolBarActivity {
    private static final int REQUEST_CODE_CHOOSE = 10;
    @Bind(R.id.tv_log)
    TextView tvLog;
    private List<Uri> mSelected;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private BaseQuickAdapter<Uri, BaseViewHolder> adapter;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_zhihu;
    }

    @Override
    protected void onViewCreate() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<Uri, BaseViewHolder>(R.layout.item_chat_image,
                new ArrayList<Uri>()) {

            @Override
            protected void convert(BaseViewHolder helper, Uri item) {
                ImageView imageView = helper.getView(R.id.iv_img);
                Glide.with(ZhiHuPictureActivity.this)
                        .load(item)
                        .into(imageView);

            }

        };
        recyclerView.setAdapter(adapter);

    }



    @OnClick({R.id.bt_selectpic, R.id.bt_selectvideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_selectpic:

                String absolutePath = getExternalCacheDir().getAbsolutePath();
                String fileDir = getExternalFilesDir(null).getAbsolutePath();
                String filePath = getExternalFilesDir(null).getPath();
                L.d("path  "+absolutePath);
                L.d("file  "+fileDir);
                L.d("filePath  "+filePath);

                checkPermisson();

                break;
            case R.id.bt_selectvideo:

                openVideoSelect();
                break;
        }
    }

    private void checkPermisson() {
        new RxPermissions(this)
                .request("android.permission.READ_EXTERNAL_STORAGE")
                .subscribe(new MyObserver<Boolean>() {
                    @Override
                    public void _onNext(Boolean aBoolean) {
                        openPicSelect();
                    }

                    @Override
                    public void _onError(String e) {

                        showErrorToast("没有权限");
                    }
                });
    }

    private void openVideoSelect() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .captureStrategy(new CaptureStrategy(true,null))
                .captureStrategy(new CaptureStrategy(true, getPackageName()+".fileprovider"))
                .capture(true)
                .countable(true)
                .maxSelectable(9)
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private void openPicSelect() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .captureStrategy(new CaptureStrategy(true,null))
                .countable(true)
                .maxSelectable(9)
                .captureStrategy(new CaptureStrategy(true, getPackageName()+".fileprovider"))
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            tvLog.setText("");
            tvLog.append("\rmSelected: " + mSelected);
            Log.d("Matisse", "mSelected: " + mSelected);
            adapter.replaceData(mSelected);
        }
    }
}
