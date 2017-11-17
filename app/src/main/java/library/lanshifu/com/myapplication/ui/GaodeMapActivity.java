package library.lanshifu.com.myapplication.ui;

import android.Manifest;
import android.widget.Button;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.Bind;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.myapplication.R;

/**
 * Created by lanxiaobin on 2017/11/15.
 */

public class GaodeMapActivity extends BaseActivity {
    @Bind(R.id.btn_location)
    Button btnLocation;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_gaodemap;
    }

    @Override
    protected void initView() {

        checkPermission();


    }

    private void checkPermission() {

        new RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            startLocation();
                        }
                    }
                });
    }

    private void startLocation() {

    }


}
