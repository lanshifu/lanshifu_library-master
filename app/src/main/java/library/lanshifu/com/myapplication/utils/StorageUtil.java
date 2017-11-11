package library.lanshifu.com.myapplication.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import library.lanshifu.com.myapplication.MyApp;

public class StorageUtil {

	public static String getAppRootDir() {
		String rootPath = "";
		if (checkSDCard()) {
			//外部存储可用
			rootPath = MyApp.getContext().getExternalFilesDir(null).getPath() +"/";
		}else {
			//外部存储不可用
			rootPath = MyApp.getContext().getCacheDir().getPath() +"/";
		}
		return rootPath;
	}

	public static String getWallpaperDir() {
		String path = getAppRootDir() + "wallpaper/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static void deleteWallpaper(String fileName) {
		String path = getWallpaperDir() + fileName;
		File file = new File(path);
		if (file.exists()) {
			file.delete();
			Log.d("mydebug", "delete succees");
		}else{
			Log.d("mydebug", "delete fail");
		}
	}

	public static String getUpdateDir() {
		String path = getAppRootDir() + "update/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	private static String getTakePhotoDir() {
		String path = getAppRootDir() + "camera/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static File getCrashLogDirFile() {
		String path = getAppRootDir() + "crash/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public static String getLogPath(String name) {
		return getLogFolder() + name + ".txt";
	}

	public static String getLogFolder() {
		String path = getAppRootDir() + "dev/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String getHeadPhotoDir() {
		String path = getAppRootDir() + "head_photo/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String getTakePhotoPath() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		String name = dateFormat.format(date) + ".jpg";

		return getTakePhotoDir() + name;
	}

	public static String getTakePhotoNamePath(String number, String time) {
		String dir = Environment.getExternalStorageDirectory()
				+ "/DCIM/Camera/";
		return dir + number + "-" + time + ".jpg";
	}

	public static boolean checkSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					|| !Environment.isExternalStorageRemovable();
	}

	public static String getMessageImageDir() {
		String path = getAppRootDir() + "image/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String getMessageThumImageDir() {
		String path = getAppRootDir() + "image/thum/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String getMessageAudioDir() {
		String path = getAppRootDir() + "audio/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String getMessageVideoDir() {
		String path = getAppRootDir() + "video/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String getMessageFileDir() {
		String path = getAppRootDir() + "file/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String getDir(String folderName) {
		String path = getAppRootDir() + folderName + "/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String getMessageViedoThumbDir() {
		String path = getMessageVideoDir() + "thumb/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}


}
