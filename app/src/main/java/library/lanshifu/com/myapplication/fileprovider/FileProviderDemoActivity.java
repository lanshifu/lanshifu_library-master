package library.lanshifu.com.myapplication.fileprovider;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.lanshifu.fileprovdider7.FileProvider7;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;

public class FileProviderDemoActivity extends BaseToolBarActivity {


    private static final int REQUEST_CODE_TAKE_PHOTO = 0;
    @Bind(R.id.imageview)
    ImageView imageview;
    private String mCurrentPhotoPath;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_file_provider_demo;
    }

    @Override
    protected void onViewCreate() {

    }


    @OnClick({R.id.bt_photo, R.id.bt_install})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_photo:
                takePhoto();
                break;
            case R.id.bt_install:
                install();
                break;
        }
    }

    private void install() {

    File file = new File(Environment.getExternalStorageDirectory(),
            "111.apk");
    Intent intent = new Intent(Intent.ACTION_VIEW);
    // 仅需改变这一行
        FileProvider7.setIntentDataAndType(this,
    intent, "application/vnd.android.package-archive", file, true);
    startActivity(intent);
}
    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            mCurrentPhotoPath = file.getAbsolutePath();
            // 仅需改变这一行
            Uri fileUri = FileProvider7.getUriForFile(this, file);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_TAKE_PHOTO) {
            imageview.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }
    }


}
