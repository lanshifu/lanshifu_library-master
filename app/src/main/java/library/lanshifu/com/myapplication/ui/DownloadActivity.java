package library.lanshifu.com.myapplication.ui;

import android.Manifest;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import butterknife.Bind;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.net.DownLoadObserver;
import library.lanshifu.com.myapplication.net.RetrofitHelper;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;
import library.lanshifu.com.myapplication.net.progress.body.ProgressInfo;
import library.lanshifu.com.myapplication.utils.StorageUtil;
import library.lanshifu.com.myapplication.widget.NumberProgressBar;
import okhttp3.ResponseBody;

/**
 * Created by lanxiaobin on 2017/11/11.
 */

public class DownloadActivity extends BaseToolBarActivity {
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private BaseQuickAdapter<DownloadBean, BaseViewHolder> adapter;

    @Override
    protected int getLayoutid() {
        return R.layout.layout_recyclerview;
    }

    @Override
    protected void onViewCreate() {

        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        showShortToast("有权限");
                    }
                });


        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<DownloadBean, BaseViewHolder>(R.layout.item_download, new ArrayList<DownloadBean>()) {
            @Override
            protected void convert(BaseViewHolder helper, final DownloadBean item) {
                helper.setText(R.id.name, item.name);
                final TextView downloadSize = helper.getView(R.id.downloadSize);
                final TextView tvProgress = helper.getView(R.id.tvProgress);
                final NumberProgressBar pbProgress = helper.getView(R.id.pbProgress);
                 pbProgress.setMax(1000);

                helper.getView(R.id.start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RetrofitHelper.getDownloadApi().downloadQQ()
                                .compose(RxSchedulerHelper.<ResponseBody>io_main())
                                .subscribe(new DownLoadObserver<ResponseBody>(item.url,
                                        StorageUtil.getMessageFileDir() + "qq.apk") {
                                    @Override
                                    protected void onProgress(ProgressInfo progressInfo) {
                                        L.d(progressInfo.toString());

//                                        tvProgress.setText(progressInfo.getPercent());
                                        String size = progressInfo.getCurrentbytes()/1024/1024+"-M/"
                                                +progressInfo.getContentLength()/1024/1024+"M";
                                        downloadSize.setText(size);
                                        pbProgress.setProgress(progressInfo.getPercent() *1000);
                                    }

                                    @Override
                                    protected void onDownLoadSuccess() {
                                        showShortToast("下载成功");

                                    }

                                    @Override
                                    protected void onDownFailed(String error) {
                                        showShortToast("下载失败"+error);


                                    }
                                });
                    }
                });
            }

        };
        recyclerview.setAdapter(adapter);

        DownloadBean bean = new DownloadBean();
        bean.name= "QQ";
        bean.url=  "http://121.29.10.1/f3.market.xiaomi.com/download/AppStore/0ff0604fd770f481927d1edfad35675a3568ba656/com.tencent.mobileqq.apk";
        adapter.addData(bean);

    }


    static class DownloadBean{
        public String name;
        public String icon_Url;
        public String url;
    }


}
