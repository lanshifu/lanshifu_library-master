package library.lanshifu.com.myapplication.network_into.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import library.lanshifu.com.lsf_library.utils.NetworkUtils;
import library.lanshifu.com.myapplication.network_into.NetworkHelper;

public class NetworkReceiver extends BroadcastReceiver {

	private static final String TAG = "NetworkReceiver";
	private boolean disconnect = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.e(TAG, "网络状态改变>>");
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
				&& NetworkUtils.isWifiConnected() && disconnect) {
			disconnect = false;
			NetworkHelper.initWifiInfo();
			Log.e(TAG, "连接WiFi");
		} else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
				&& !NetworkUtils.isWifiConnected()) {
			disconnect = true;
			Log.e(TAG, "断开WiFi");
		}
	}
}
