package library.lanshifu.com.myapplication.mvp.presenter;

import android.graphics.BitmapFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;
import library.lanshifu.com.lsf_library.utils.ImageUtil;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.imagepicker.PictureBean;
import library.lanshifu.com.myapplication.mvp.contract.PhotoPictureContract;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by lanxiaobin on 2017/8/24.
 */

public class PhotoPicturePresenter extends PhotoPictureContract.Presenter {

    private List<BmobObject> fileList = new ArrayList<>();
    @Override
    public void upLoadPic(List<PictureBean> pictureBeanList) {

    }

    @Override
    public void compressWithLs(final List<String> photos) {

        Luban.with(mContext)
                .load(photos)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        L.e("compressWithLs ->onSuccess");
                        showResult(photos, file);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();

    }

    private void showResult(List<String> photos, File file) {
        int[] thumbSize = computeSize(file.getAbsolutePath());
//        String originArg = String.format(Locale.CHINA, "原图参数：%d*%d, %dk", originSize[0], originSize[1], new File(photos.get(mAdapter.getItemCount())).length() >> 10);
        String thumbArg = String.format(Locale.CHINA, "压缩后参数：%d*%d, %dk", thumbSize[0], thumbSize[1], file.length() >> 10);

        String filePath = file.getAbsolutePath();
        String name = getFileName(filePath);
        PictureBean pictureBean = new PictureBean(name,filePath, ImageUtil.imageToBase64(filePath),"描述");
        fileList.add(pictureBean);

        if(photos.size() == fileList.size()){
            upLoad();
        }

    }

    private void upLoad() {
        T.showShort("正在上传");
        mView.showProgressDialog("正在上传");
        new BmobObject().insertBatch(mContext, fileList, new SaveListener() {
            @Override
            public void onSuccess() {
                T.showShort("上传成功");
                mView.upLoadPicSuccess();
                mView.showProgressDialog("正在上传");
                fileList.clear();
            }

            @Override
            public void onFailure(int i, String s) {

                T.showShort("上传失败");
            }
        });
    }

    private String getFileName(String filePath) {

        if (filePath == null) {
            return "";
        }
        int index = filePath.lastIndexOf("/");
        String name = filePath.substring(index,filePath.length()-1);
        return name;
    }


    private int[] computeSize(String srcImg) {
        int[] size = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(srcImg, options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;

        return size;
    }
}
