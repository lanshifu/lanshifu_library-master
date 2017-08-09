package library.lanshifu.com.myapplication.fileprovider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lanshifu.fileprovdider7.FileProvider7;
import com.lzy.imagepicker.util.BitmapUtil;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import rx.functions.Action1;

public class FileProviderDemoActivity extends BaseToolBarActivity {


    private static final int REQUEST_CODE_TAKE_PHOTO = 0;
    @Bind(R.id.imageview)
    ImageView imageview;
    private String mCurrentPhotoPath;
    private String cuttedPicturePath;
    private static final int REQUEST_CUT_PHOTO = 1;
    private static final int REQUEST_CODE_LOCAL = 2;
    private String picturePath;
    private String cutPicPath;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_file_provider_demo;
    }

    @Override
    protected void onViewCreate() {

    }


    @OnClick({R.id.bt_photo, R.id.bt_install, R.id.bt_selectpic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_photo:
                RxPermissions.getInstance(this).request("android.permission.CAMERA")
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                takePhoto();
                            }
                        });

                break;
            case R.id.bt_install:
                install();
                break;
            case R.id.bt_selectpic:
                selectPicFromLocal();
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            mCurrentPhotoPath = file.getAbsolutePath();
            // 仅需改变这一行
            Uri fileUri = FileProvider7.getUriForFile(this, file);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        }
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            if (data != null) {
                cutPicture(mCurrentPhotoPath);
            }

        } else if (requestCode == REQUEST_CUT_PHOTO) {
            Loge( "onActivityResult: data="+data);
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                Drawable drawable = new BitmapDrawable(this.getResources(), photo);
                    imageview.setImageDrawable(drawable);
            }
            showShortToast("裁剪成功");


        } else if (requestCode == REQUEST_CODE_LOCAL) {
            showShortToast("获取照片成功");
            if (data != null) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    sendPicByUri(selectedImage);
                }
            }
        }
    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            picturePath = file.getAbsolutePath();
        }

        cutPicture(picturePath);
    }


    private void cutPicture(String path) {


        cutPicPath = path+ "output_image" + System.currentTimeMillis() + ".jpg";
        File outputImage = new File(cutPicPath);

        File file = new File(path);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputImage);
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CUT_PHOTO);
    }


}
