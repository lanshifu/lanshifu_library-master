
package library.lanshifu.com.lsf_library.utils;

import java.io.File;
import java.math.BigDecimal;


public class CacheUtil {

    public static String getCacheSize() {
        File file = new File(FileUtil.FILE_ROOT);
        long fileSize = 0L;
        if (!file.exists()) {
            return "0KB";
        }
        fileSize = getDirSize(file);
        if (fileSize < 512 * 1024) {
            return fileSize / 1024 + "KB";
        } else {
            float totleSize = fileSize / 1024f / 1024f;
            BigDecimal bd = new BigDecimal((double) totleSize);
            bd = bd.setScale(2, 4); // 取两位小数，并四舍五入
            return bd + "M";
        }

    }

    public static boolean hasCache() {
        File file = new File(FileUtil.FILE_ROOT);
        long fileSize = 0L;
        if (!file.exists()) {
            return false;
        }
        fileSize = getDirSize(file);
        if (fileSize == 0) {
            return false;
        }
        return true;
    }

    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (dir.isFile()) {
            return dir.length();
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        if (files == null) {
            return 0;
        }
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile() && file.delete()) {
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

}
