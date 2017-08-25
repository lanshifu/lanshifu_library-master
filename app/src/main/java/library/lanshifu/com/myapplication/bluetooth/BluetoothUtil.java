package library.lanshifu.com.myapplication.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Arrays;

/**
 * @Description: 蓝牙基础操作工具类
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 16/8/5 20:43.
 */
public class BluetoothUtil {
    private static final String TAG = "BluetoothUtil";

    public static void enableBluetooth(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static boolean isSupportBle(Context context){
        if (context == null || !context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        return manager.getAdapter() != null;
    }

    public static boolean isBleEnable(Context context){
        if(!isSupportBle(context)){
            return false;
        }
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        return manager.getAdapter().isEnabled();
    }

    public static void printServices(BluetoothGatt gatt) {
        if (gatt != null) {
            for (BluetoothGattService service : gatt.getServices()) {
                Log.d(TAG,"service: " + service.getUuid());
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    Log.d(TAG," characteristic: " + characteristic.getUuid() + " value: " + Arrays.toString(characteristic.getValue()));
                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                        Log.d(TAG,"        descriptor: " + descriptor.getUuid() + " value: " + Arrays.toString(descriptor.getValue()));
                    }
                }
            }
        }
    }

}
