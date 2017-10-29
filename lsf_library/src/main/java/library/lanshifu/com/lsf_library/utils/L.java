package library.lanshifu.com.lsf_library.utils;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Log统一管理类
 * 
 * @author way
 * 
 */
public class L
{

	private static boolean sDebug = true;
	private static String sTag = "lanshifu";
	private static final int JSON_INDENT = 2;


	public static void init(boolean debug, String tag) {
		L.sDebug = debug;
		L.sTag = tag;
	}


	public static void d(String msg, Object... params) {
		Logger.d(msg);
//		d(null, msg, params);
	}

	public static void d(String tag, String msg, Object[] params) {
		if (!sDebug) return;
		LogText.d(getFinalTag(tag), String.format(msg, params));
	}


	public static void e(String msg, Object... params) {
		Logger.e(msg);
//		e(null, msg, params);
	}

	public static void e(String tag, String msg, Object[] params) {
		if (!sDebug) return;
		LogText.e(getFinalTag(tag), String.format(msg, params));
	}

	public static void json(String json) {
		json(null, json);
	}

	public static void json(String tag, String json) {
		if (!sDebug) return;
		LogText.e(getFinalTag(tag), getPrettyJson(json));
	}

	private static String getPrettyJson(String jsonStr) {
		try {
			jsonStr = jsonStr.trim();
			if (jsonStr.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(jsonStr);
				return jsonObject.toString(JSON_INDENT);
			}
			if (jsonStr.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(jsonStr);
				return jsonArray.toString(JSON_INDENT);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "Invalid Json, Please Check: " + jsonStr;
	}


	private static String getFinalTag(String tag) {
		if (!TextUtils.isEmpty(tag)) {
			return tag;
		}
		return sTag;
	}

	private static class LogText {
		private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════\n";
		private static final String SINGLE_DIVIDER = "────────────────────────────────────────────\n";

		private String mTag;

		public LogText(String tag) {
			mTag = tag;
		}


		public static void d(String tag, String content) {
			LogText logText = new LogText(tag);
			logText.setup(content,tag);
		}

		public static void e(String tag, String content) {
			LogText logText = new LogText(tag);
			logText.setup(content,tag);
		}

		public void setup(String content ,String label) {
			setUpHeader(label);
			setUpContent(content,label);
			setUpFooter(label);

		}

		private void setUpHeader(String label) {
			if(label.equals("d")){
				Log.d(mTag, SINGLE_DIVIDER);
			}else if(label.equals("e")){
				Log.e(mTag, SINGLE_DIVIDER);
			}else{
				Log.d(mTag, SINGLE_DIVIDER);
			}
		}

		private void setUpFooter(String label) {

			if(label.equals("d")){
				Log.d(mTag, DOUBLE_DIVIDER);
			}else if(label.equals("e")){
				Log.e(mTag, DOUBLE_DIVIDER);
			}else{
				Log.e(mTag, DOUBLE_DIVIDER);
			}

		}

		public void setUpContent(String content, String label) {
			StackTraceElement targetStackTraceElement = getTargetStackTraceElement();


			if(label.equals("d")){
				Log.d(mTag, "(" + targetStackTraceElement.getFileName() + ":"
						+ targetStackTraceElement.getLineNumber() + ")");
				Log.d(mTag, content);
			}else if(label.equals("e")){
				Log.e(mTag, "(" + targetStackTraceElement.getFileName() + ":"
						+ targetStackTraceElement.getLineNumber() + ")");
				Log.e(mTag, content);
			}else{
				Log.e(mTag, "(" + targetStackTraceElement.getFileName() + ":"
						+ targetStackTraceElement.getLineNumber() + ")");
				Log.e(mTag, content);
			}

		}

		private StackTraceElement getTargetStackTraceElement() {
			// find the target invoked method
			StackTraceElement targetStackTrace = null;
			boolean shouldTrace = false;
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			for (StackTraceElement stackTraceElement : stackTrace) {
				boolean isLogMethod = stackTraceElement.getClassName().equals(L.class.getName());
				if (shouldTrace && !isLogMethod) {
					targetStackTrace = stackTraceElement;
					break;
				}
				shouldTrace = isLogMethod;
			}
			return targetStackTrace;
		}
	}
}