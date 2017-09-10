
package library.lanshifu.com.lsf_library.utils;

import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import library.lanshifu.com.lsf_library.baseapp.BaseApplication;

/**
 * 文件操作工具类
 */
public final class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    public static final int IMAGETYPE = 0;

    public static final int CAMERATYPE = 1;

    public static final double BYTES_PER_M = 1024.0 * 1024.0;

    public static final String DATABASE_FOLDER_NAME = Environment.getDataDirectory() + "/data/"
            + BaseApplication.getContext().getPackageName() + "/databases/";

    public static final String SHAREPRE_CACHE_NAME = Environment.getDataDirectory() + "/data/"
            + BaseApplication.getContext().getPackageName() + "/cache/";

    /**
     * sd卡的根目录
     */
    public static final String FILE_ROOT = Environment.getExternalStorageDirectory() + "/aa_lanshifu/"
            + getSimplePackage() + "/";

    /**
     * 这个实现完全固定了文件名,而不是根据当前时间来生成
     */
    @Deprecated
    public static final String FILE_NAME = FILE_ROOT
            + DateUtil.getCurrent(DateUtil.FORMAT_YYYYMMDDHHMMSS) + ".jpg";

    /**
     * 附件下载的目录
     */
    public static final String DOWN_PATH = FILE_ROOT + "attach/";

    /**
     * 新功能图片下载路径
     */
    public static final String NEWFUN_DOWN_PATH = FILE_ROOT + "newfunattach/";

    /**
     * 日志的目录
     */
    public static final String LOG_ROOT = FILE_ROOT + "log";

    /**
     * 文件下载路径
     */
    public static final String CLIENT_ROOT = FILE_ROOT + "client";

    /**
     * 列表的icon缓存路径
     */
    public static final String ICON_CACHE_ROOT = FILE_ROOT + "image";

    private FileUtil() {
    }

    /**
     * 获取包名的最后一个字段
     *
     * @return 包名的最后一个字段
     */
    public static String getSimplePackage() {
        String packageName = BaseApplication.getContext().getPackageName();
        int idx = packageName.lastIndexOf(".");
        return idx == -1 ? packageName : packageName.substring(idx + 1);
    }

    /**
     * 取得文件的大小
     *
     * @param file 本地文件
     * @return 文件的大小, 如果找不到文件或读取失败则返回-1
     */
    public static long getFileSize(File file) {
        return (file.exists() && file.isFile() ? file.length() : -1L);
    }

    /**
     * 根据uri获取文件路径
     *
     * @param uri     使用URI表示的文件路径
     * @param context 上下文
     * @return 实际的文件路径
     */
    public static String getFilePath(Uri uri, Context context) {

        return FileUriUtil.getPath(context, uri);
    }

    public static boolean isDir(String filePath){
        File file = new File(filePath);
        if(file.exists() && file.isDirectory()){
            return true;
        }
        return false;
    }
    /**
     * 拷贝文件到某文件夹
     *
     * @param source      源文件
     * @param destination 目标文件夹
     * @return 拷贝成功则返回true, 否则返回false
     */
    public static boolean copyFile(File source, File destination) {
        if (!source.exists() || !source.isFile())
            return false;
        if (!destination.exists() && !destination.mkdirs())
            return false;
        if (!destination.exists() || !destination.isDirectory())
            return false;

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(new File(destination, source.getName()));
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            closeQuietly(fis);
            closeQuietly(fos);
        }
        return false;
    }

    /**
     * 拷贝Assets下的文件到程序私有空间
     *
     * @param context 上下文
     * @param source  原文件
     * @return 保存到程序私有空间后的文件路径
     * @throws IOException 原文件在assets目录中不存在的时候
     */
    public static String copyAssetFileToInternal(Context context, String source) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getAssets().open(source);
            out = context.openFileOutput(source, Context.MODE_PRIVATE);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            return (context.getFileStreamPath(source)).getAbsolutePath();
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    /**
     * 复制asset里的文件到指定路径
     *
     * @param context  上下文
     * @param fileName 原文件名称
     * @param path     copy到指定的路径[路径包括文件名称]
     * @return 是否copy成功
     */
    public static boolean copyAssetFileToPath(Context context, String fileName, String path) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            File file = new File(path);
            if (!file.createNewFile()) {
                return false;
            }
            in = context.getAssets().open(fileName);
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            return new File(path).exists();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return false;
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    /**
     * 读取属性文件
     *
     * @param inputStream
     * @deprecated 用处不大
     */
    public static Properties getProperties(InputStream inputStream) throws IOException {
        Properties p = new Properties();
        p.load(inputStream);
        return p;
    }

    /**
     * 判断是否是根目录
     *
     * @param path 目录
     * @return boolean 如果是SD卡根目录则返回true,否则返回false
     * @deprecated 考虑移到{@link SystemManage}中去,或者删除
     */
    public static boolean isExternalDataDirectory(String path) {
        return Environment.getExternalStorageDirectory().toString().equals(path);
    }

    /**
     * 检查sdcard是否存在
     *
     * @return 如果sdcard存在返回true, 否则返回false
     * @deprecated 请直接使用{@link SystemManage#externalMemoryAvailable()}
     */
    public static boolean isSDCardExist() {
        return SystemManage.externalMemoryAvailable();
    }

    /**
     * 调用系统的工具，浏览某个文件
     *
     * @param path 文件路径
     */
    public static Intent openFile(String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, getMIMEType(file));
        return intent;
    }

    /**
     * 判断文件MimeType audio:.amr,.m4a,.mp3,.mid,.xmf,.ogg,.wav,.3gpp,.3ga,.wma
     * video:.3gp,.mp4,.m4v image:.jpg,.gif,.png,.jpeg,.bmp word:.doc apk:.apk
     *
     * @param file 文件名
     * @return 文件MimeType
     */
    public static String getMIMEType(File file) {
        String type="*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
        /* 获取文件的后缀名*/
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private static final String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            { ".3gp", "video/3gpp" },
            { ".apk", "application/vnd.android.package-archive" },
            { ".asf", "video/x-ms-asf" },
            { ".avi", "video/x-msvideo" },
            { ".bin", "application/octet-stream" },
            { ".bmp", "image/bmp" },
            { ".c", "text/plain" },
            { ".class", "application/octet-stream" },
            { ".conf", "text/plain" },
            { ".cpp", "text/plain" },
            { ".doc", "application/msword" },
            { ".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
            { ".xls", "application/vnd.ms-excel" },
            { ".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
            { ".exe", "application/octet-stream" },
            { ".gif", "image/gif" },
            { ".gtar", "application/x-gtar" },
            { ".gz", "application/x-gzip" },
            { ".h", "text/plain" },
            { ".htm", "text/html" },
            { ".html", "text/html" },
            { ".jar", "application/java-archive" },
            { ".java", "text/plain" },
            { ".jpeg", "image/jpeg" },
            { ".jpg", "image/jpeg" },
            { ".js", "application/x-javascript" },
            { ".log", "text/plain" },
            { ".m3u", "audio/x-mpegurl" },
            { ".m4a", "audio/mp4a-latm" },
            { ".m4b", "audio/mp4a-latm" },
            { ".m4p", "audio/mp4a-latm" },
            { ".m4u", "video/vnd.mpegurl" },
            { ".m4v", "video/x-m4v" },
            { ".mov", "video/quicktime" },
            { ".mp2", "audio/x-mpeg" },
            { ".mp3", "audio/x-mpeg" },
            { ".mp4", "video/mp4" },
            { ".mpc", "application/vnd.mpohun.certificate" },
            { ".mpe", "video/mpeg" },
            { ".mpeg", "video/mpeg" },
            { ".mpg", "video/mpeg" },
            { ".mpg4", "video/mp4" },
            { ".mpga", "audio/mpeg" },
            { ".msg", "application/vnd.ms-outlook" },
            { ".ogg", "audio/ogg" },
            { ".pdf", "application/pdf" },
            { ".png", "image/png" },
            { ".pps", "application/vnd.ms-powerpoint" },
            { ".ppt", "application/vnd.ms-powerpoint" },
            { ".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
            { ".prop", "text/plain" }, { ".rc", "text/plain" },
            { ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
            { ".sh", "text/plain" }, { ".tar", "application/x-tar" },
            { ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
            { ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
            { ".wmv", "audio/x-ms-wmv" },
            { ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
            { ".z", "application/x-compress" },
            { ".zip", "application/x-zip-compressed" }, { "", "*/*" } };

    /**
     * 判断文件类型 audio:.amr,.m4a,.mp3,.mid,.xmf,.ogg,.wav,.3gpp,.3ga,.wma
     * video:.3gp,.mp4,.m4v image:.jpg,.gif,.png,.jpeg,.bmp
     * word:.xls,doc,docx,xlsx
     *
     * @param filename 文件名
     * @return 文件类型
     */
    public static String getMIMEType(String filename) {

        if (StringUtil.isEmpty(filename)) {
            return "";
        }

        /* 取得扩展名 */
        String[] texts = filename.split("\\.");
        String end = texts[texts.length - 1].toLowerCase(Locale.US);

        /* 依扩展名的类型决定MimeType */
        // 根据需要扩展解析文件类型
        String type = "";
        if (end.equals("amr") || end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")
                || end.equals("3gpp") || end.equals("3ga") || end.equals("wma")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4") || end.equals("m4v")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("xls") || end.equals("doc") || end.equals("docx")
                || end.equals("xlsx")) {
            type = "word";
        } else {
            type = "other";
        }
        return type;
    }

    /***
     * 计算文件夹大小
     *
     * @param mFile 目录或文件
     * @return 文件或目录的大小
     */
    public static long calculateFolderSize(File mFile) {
        // 判断文件是否存在
        if (!mFile.exists()) {
            return 0;
        }

        // 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
        if (mFile.isDirectory()) {
            File[] files = mFile.listFiles();
            long size = 0;
            if (null != files) {
                for (File f : files) {
                    size += calculateFolderSize(f);
                }
            }
            return size;
        } else {
            return mFile.length();
        }

    }

    /**
     * 计算所有指定缓存文件夹的大小
     *
     * @return 缓存大小, 以M为单位, 保留两位小数
     */
    public static String getAllcacheFolderSize() {
        long c1 = calculateFolderSize(new File(SHAREPRE_CACHE_NAME));
        long c2 = calculateFolderSize(new File(FILE_ROOT));
        // 单位是M
        double resoult = (c1 + c2) / BYTES_PER_M;
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(resoult);
    }

    /***
     * 清空指定文件夹/文件
     *
     * @return 清空成功的话返回true, 否则返回false
     */

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] childs = file.listFiles();
            if (childs == null || childs.length <= 0) {
                // 空文件夹删掉
                return file.delete();
            } else {
                // 非空，遍历删除子文件
                for (int i = 0; i < childs.length; i++) {
                    deleteFile(childs[i]);
                }
                return deleteFile(file);
            }
        } else {
            return file.delete();
        }

    }

    /***
     * 清空指定文件夹下所有文件
     *
     * @return 清空成功的话返回true, 否则返回false
     * @see
     * @deprecated
     */
    public static boolean cleanDirectory(File directory) {
        return deleteFile(directory);
    }

    /***
     * 删除指定文件或文件夹
     *
     * @return 删除成功的话返回true, 否则返回false
     * @see
     * @deprecated
     */
    public static boolean forceDelete(File file) {
        return cleanDirectory(file);
    }

    /**
     * 删除所有缓存文件
     *
     * @return 如果缓存清空成功返回true, 否则返回false
     */
    public static boolean deleteAllCacheFiles() {
        return cleanDirectory(new File(SHAREPRE_CACHE_NAME)) && cleanDirectory(new File(FILE_ROOT));
    }

    /**
     * 删除文件
     *
     * @param path
     * @see
     * @deprecated
     */
    public static void delFile(String path) {
        if (StringUtil.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        deleteFile(file);
    }

    /**
     * 获取asset目录中文件的流
     *
     * @param fileName asserts目录中的文件名
     * @return 文件流, 如果读取失败则返回null
     */
    public static InputStream getAssetsInputStream(Context context, String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return is;
    }

    /**
     * 获取asset目录中文件中的字符串
     *
     * @param fileName asserts目录中的文件名
     * @return 文件内容, 如果读取失败则返回空字符串
     */
    public static String getAssets(String fileName) {

        return stream2String(getAssetsInputStream(BaseApplication.getContext(), fileName));

    }

    /**
     * 判断路径(文件或目录)是否存在
     *
     * @param path 文件或目录路径
     * @return 如果存在返回true, 否则返回false
     */
    public static boolean isFileExist(String path) {
        return new File(path).exists();
    }

    /**
     * 关闭输入输出流
     *
     * @param c 输入输出流
     */
    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                Log.i(TAG, e.toString());
            }
        }
    }

    /**
     * 流转为string
     *
     * @param is
     * @return String [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String stream2String(InputStream is) {
        if (is == null) {
            return "";
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer strBuffer = new StringBuffer("");

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            closeQuietly(reader);
        }
        return strBuffer.toString();
    }

    /**
     * 把输入流拷贝到输出流
     *
     * @param input  输入流
     * @param output 输出流
     * @return 拷贝的字节数，如果失败返回-1
     */
    public static int copyStream(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024];
        int count = 0;
        int n = 0;
        try {
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
        } catch (Exception ex) {
            return -1;
        }
        return count;
    }

    /**
     * <查询指定目录下所有指定一种后缀名的文件放到一个文件队列中> <功能详细描述>
     *
     * @param dirName  指定的目录
     * @param endName  指定的后缀名
     * @param fileList [文件List]
     * @return void [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void getFiles(File dirName, String endName, List<File> fileList) {

        File[] files = dirName.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getFiles(f, endName, fileList);
                } else if (f.isFile()) {
                    try {
                        if (f.getName().endsWith(endName.toLowerCase(Locale.US))
                                || f.getName().endsWith(endName.toUpperCase(Locale.US))) {
                            fileList.add(f);
                        }
                    } catch (Exception e) {
                        e.toString();
                    }
                }
            }
        }
    }

    /**
     * <查询指定目录下所有指定多种后缀名的文件放到一个文件队列中> <功能详细描述>
     *
     * @param dirName
     * @param endNames [后缀名数组]
     * @param fileList [参数说明]
     * @return void [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void getFiles(File dirName, String[] endNames, List<File> fileList) {
        for (String endName : endNames) {
            getFiles(dirName, endName, fileList);
        }
    }

    public static String getDirectoryName(String path) {
        if (!StringUtil.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                String parentPath = file.getParent();
                return parentPath.substring(parentPath.lastIndexOf(File.separator) + 1);
            }
        }
        return "";

    }

    /**
     * <判断SD卡上的图片是否存在> <功能详细描述>
     *
     * @param filePath
     * @return boolean [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isImageExist(String filePath) {
        if (!StringUtil.isEmpty(filePath)) {
            File file = new File(filePath);
            return file.exists() && file.length() > 0;
        }
        return false;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:

                    degree = 90;

                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:

                    degree = 180;

                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:

                    degree = 270;

                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());

        }
        return degree;
    }

    /**
     * 字节数组保存到文件
     *
     * @param data
     * @param path [参数说明]
     * @return void [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void saveFile(byte[] data, String path) {
        File file = new File(new File(path).getParent());
        if (!file.exists() && !file.mkdirs()) {
            return;
        }
        BufferedOutputStream stream = null;
        try {
            file = new File(path);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(data);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            closeQuietly(stream);
        }
    }

    /**
     * 获取文件的字节
     *
     * @param file
     * @return [参数说明]
     */
    public static byte[] getFileContent(File file) {
        FileInputStream fis = null;
        ByteArrayOutputStream output = null;
        try {
            fis = new FileInputStream(file);
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = fis.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            FileUtil.closeQuietly(output);
            FileUtil.closeQuietly(fis);
        }
        return null;
    }

}
