package library.lanshifu.com.lsf_library.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 * 
 */
public class T {

	private static Toast toast = null;// 提醒框

	private T()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("不能通过new创建");
	}



	private static Context sContext;
	private static Toast sToast;

	public static void init(Context context)
	{
		sContext = context.getApplicationContext();
		sToast = Toast.makeText(sContext, "", Toast.LENGTH_SHORT);
	}


	public static void showShort(String msg)
	{
		sToast.setText(msg);
		sToast.setDuration(Toast.LENGTH_SHORT);
		sToast.show();
	}


	public static void showLong(String msg)
	{
		sToast.setText(msg);
		sToast.setDuration(Toast.LENGTH_LONG);
		sToast.show();
	}

}