package library.lanshifu.com.myapplication.ui;

import android.content.Intent;
import android.text.TextUtils;

import org.litepal.crud.DataSupport;

import java.io.File;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.widget.FileDisplayView;

/**
 * Created by lanshifu on 2017/9/29.
 */

public class FileDisplayActivity extends BaseToolBarActivity {
    @Bind(R.id.fileDisplayView)
    FileDisplayView fileDisplayView;

    private String filePath;
    @Override
    protected int getLayoutid() {
        return R.layout.activity_file_display;
    }

    @Override
    protected void onViewCreate() {
        Intent intent = this.getIntent();
        String path = (String) intent.getSerializableExtra("path");
        if (!TextUtils.isEmpty(path)) {
            Loge("文件path:" + path);
            filePath = path;
        }

        if (filePath.contains("http")) {//网络地址要先下载
            showErrorToast("需要下载才能使用");
        } else {
            fileDisplayView.displayFile(new File(filePath));
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fileDisplayView != null) {
            fileDisplayView.onStopDisplay();
        }
    }
}
