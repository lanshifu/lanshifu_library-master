package library.lanshifu.com.myapplication.imagepicker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.ImageUtil;
import library.lanshifu.com.myapplication.R;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.text.TextUtils.isEmpty;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;

public class PhotoPickerActivity extends BaseToolBarActivity {

    private static final int REQUEST_CODE_SELECT = 100;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private ImagePicker imagePicker;


    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数
    private CommonAdapter<ImageItem> adapter;


    @Override
    protected int getLayoutid() {
        return R.layout.activity_photo_picker;
    }

    @Override
    protected void onViewCreate() {
        Bmob.initialize(this, "767611a082094fedc64a0633a4a8caa4");
        imagePicker = ImagePicker.getInstance();

        initImagePicker();
        initWidget();

    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        selImageList.add(new ImageItem());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new CommonAdapter<ImageItem>(this, R.layout.list_item_image, selImageList) {
            @Override
            protected void convert(ViewHolder holder, ImageItem imageItem, final int position) {
                if (isEmpty(imageItem.path)) {
                    //add
                    holder.setImageResource(R.id.iv_img, R.drawable.selector_image_add);
                    holder.setOnClickListener(R.id.iv_img, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //打开图库或者相机
                            selectPhoto();
                        }
                    });
                } else {
                    holder.setText(R.id.tv_size,"大小："+imageItem.size);
                    ImageView iv_img = holder.getView(R.id.iv_img);
                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, imageItem.path, iv_img, 0, 0);
                    holder.setOnClickListener(R.id.iv_img, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showShortToast("打开大图");
                            //打开预览
                            Intent intentPreview = new Intent(PhotoPickerActivity.this, ImagePreviewDelActivity.class);

                            ArrayList<ImageItem> list = new ArrayList<>(selImageList);
                            list.remove(list.size() - 1);
                            intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, list);
                            intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                            intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                            startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                        }
                    });

                }

            }

        };

        recyclerView.setAdapter(adapter);


    }


    private void selectPhoto() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(new String[]{"拍照", "相册"}, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        switch (position) {
                            case 0: // 直接调起相机
                                /**
                                 * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                 *
                                 * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                 *
                                 * 如果实在有所需要，请直接下载源码引用。
                                 */
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(PhotoPickerActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);

                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(PhotoPickerActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                        dialogInterface.dismiss();

                    }
                }).create();
        alertDialog.show();
    }

    ArrayList<ImageItem> images = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    selImageList.add(new ImageItem());
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    selImageList.add(new ImageItem());
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }




    @OnClick(R.id.bt_commit)
    public void onViewClicked() {
        if(selImageList ==null ||selImageList.size() == 1){
            showShortToast("请先选择图片");
            return;
        }
        uploadPictures();
    }

    private void uploadPictures() {

        //selImageList
        final List<String> filePaths = new ArrayList<>();
        for (int i = 0; i < selImageList.size()-1; i++) {
            ImageItem imageItem = selImageList.get(i);
            filePaths.add(imageItem.path);
            Loge("filePath: "+imageItem.path);
        }
        compressWithLs(filePaths);

    }


    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(final List<String> photos) {
        Luban.with(this)
                .load(photos)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        Loge("compressWithLs ->onSuccess");
                        showResult(photos, file);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }

    List<PictureBean> pictureBeanList = new ArrayList<>();
    private void showResult(List<String> photos, File file) {
//        int[] originSize = computeSize(photos.get(mAdapter.getItemCount()));
        int[] thumbSize = computeSize(file.getAbsolutePath());
//        String originArg = String.format(Locale.CHINA, "原图参数：%d*%d, %dk", originSize[0], originSize[1], new File(photos.get(mAdapter.getItemCount())).length() >> 10);
        String thumbArg = String.format(Locale.CHINA, "压缩后参数：%d*%d, %dk", thumbSize[0], thumbSize[1], file.length() >> 10);
        String filePath = file.getAbsolutePath();
        Loge("压缩后:"+filePath);
        Loge(thumbArg);

        String name = getFileName(filePath);
        PictureBean pictureBean = new PictureBean(name,filePath, ImageUtil.imageToBase64(filePath),"描述");
        pictureBean.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Loge("上传成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Loge("上传onFailure");
            }
        });
        pictureBeanList.add(pictureBean);

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
