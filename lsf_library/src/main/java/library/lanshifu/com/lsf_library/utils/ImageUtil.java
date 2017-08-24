
package library.lanshifu.com.lsf_library.utils;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Image通用操作封装工具类
 * 
 * @author wujian
 * @version [ME WOYOUClient_Handset V100R001C04SPC002, 2011-10-17]
 */
public final class ImageUtil {

    private ImageUtil() {
    }

    /**
     * 将图片转化给byte[]操作
     * 
     * @param bm 图片对象
     * @return 图片byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 将byte[]转化成图片
     * 
     * @param b 图片的byte[]
     * @return 图片对象
     */
    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 将Drawable转换为Bitmap
     * 
     * @param drawable Drawable对象
     * @return bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                .getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 对图片进行缩放
     * 
     * @param context 上下文
     * @param bitmap 原图
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 缩放后的图
     */
    public static Bitmap zoomImage(Context context, Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null) {
            return null;
        }
        DisplayMetrics displayMetrics = getDisplayMetris(context);
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        if (maxWidth < 1) {
            maxWidth = originWidth;
        } else {
            maxWidth = (int) (maxWidth * displayMetrics.density);
        }
        if (maxHeight < 1) {
            maxHeight = originHeight;
        } else {
            maxHeight = (int) (maxHeight * displayMetrics.density);
        }
        if (originWidth <= maxWidth && originHeight <= maxHeight) {
            return bitmap;
        }
        // 判断哪个超出的多
        float widthScale = maxWidth / (float) originWidth;
        float heightScale = maxHeight / (float) originHeight;
        float newScale = (widthScale - heightScale < 0.) ? widthScale : heightScale;
        Matrix matrix = new Matrix();
        matrix.setScale(newScale, newScale);
        Bitmap zoomedImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return zoomedImage;
    }

    /**
     * 获取不超过limited宽高的新宽高
     * 
     * @param context 上下文
     * @param originWidth 原始宽度
     * @param originHeight 原始高度
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 新宽高(索引0为宽度, 索引1为高度)
     */
    public static final int[] getScaledLimitedSize(Context context, int originWidth,
                                                   int originHeight, int maxWidth, int maxHeight) {
        DisplayMetrics displayMetrics = getDisplayMetris(context);
        float fMaxWidth;
        float fMaxHeight;
        if (maxWidth < 1) {
            fMaxWidth = originWidth;
        } else {
            fMaxWidth = maxWidth * displayMetrics.density;
        }
        if (maxHeight < 1) {
            fMaxHeight = originHeight;
        } else {
            fMaxHeight = maxHeight * displayMetrics.density;
        }
        if (originWidth <= fMaxWidth && originHeight <= fMaxHeight) {
            return new int[] {
                    originWidth, originHeight
            };
        }
        float widthScale = fMaxWidth / originWidth;
        float heightScale = fMaxHeight / originHeight;
        float newScale = (widthScale - heightScale < 0.) ? widthScale : heightScale;
        int newWidth = (int) (originWidth * newScale);
        int newHeight = (int) (originHeight * newScale);
        return new int[] {
                newWidth, newHeight
        };
    }

    /**
     * 根据最小边长进行压缩图片，以便向服务器上传
     * 
     * @param path 图片路径
     * @return 压缩后的位图
     */
    public static Bitmap getFitBitmapAccordingWidth(String path, int IMAGE_WIDTH, int IMAGE_HEIGHT) {
        if (path == null) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);

        int srcWidth = opts.outWidth;
        int srcHeight = opts.outHeight;

        int destWidth = 0;
        int destHeight = 0;
        // 缩放的比例
        double ratio = 0.0;
        if (srcWidth < srcHeight) {
            ratio = (double) srcWidth / IMAGE_WIDTH;
            if (ratio > 1.0) {
                destHeight = (int) (srcHeight / ratio);
                destWidth = IMAGE_WIDTH;
            } else {
                return BitmapFactory.decodeFile(path);
            }

        } else {
            ratio = (double) srcHeight / IMAGE_HEIGHT;
            if (ratio > 1.0) {
                destWidth = (int) (srcWidth / ratio);
                destHeight = IMAGE_HEIGHT;
            } else {
                return BitmapFactory.decodeFile(path);
            }

        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        double x = Math.log(ratio) / Math.log(2);
        int k = (int) Math.ceil(x);
        int j = (int) Math.pow(2, k);
        newOpts.inSampleSize = j;
        newOpts.inJustDecodeBounds = false;
        newOpts.outHeight = destHeight;
        newOpts.outWidth = destWidth;

        // Tell to gc that whether it needs free memory, the Bitmap can
        // be cleared
        newOpts.inPurgeable = true;
        // Which kind of reference will be used to recover the Bitmap
        // data after being clear, when it will be used in the future
        newOpts.inInputShareable = true;
        // Allocate some temporal memory for decoding
        newOpts.inTempStorage = new byte[64 * 1024];

        Bitmap destBm = BitmapFactory.decodeFile(path, newOpts);
        return destBm;
    }



    /**
     * 返回系统的显示信息
     *
     * @param context 应用上下文
     * @return 系统的显示信息
     */
    public static synchronized DisplayMetrics getDisplayMetris(Context context)
    {

        DisplayMetrics sDisplayMetris = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(sDisplayMetris);

        return sDisplayMetris;
    }

    /**
     * 将图片转换成Base64编码的字符串
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     *base64编码字符集转化成图片文件。
     * @param base64Str
     * @param path 文件存储路径
     * @return 是否成功
     */
    public static boolean base64ToFile(String base64Str,String path){
        byte[] data = Base64.decode(base64Str,Base64.DEFAULT);
        for (int i = 0; i < data.length; i++) {
            if(data[i] < 0){
                //调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
            os.flush();
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

    }
}
