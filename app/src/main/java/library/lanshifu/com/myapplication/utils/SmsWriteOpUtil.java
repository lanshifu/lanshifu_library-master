package library.lanshifu.com.myapplication.utils;

import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import library.lanshifu.com.lsf_library.utils.L;

/**
 * Created by lanshifu on 2017/9/16.
 */

public class SmsWriteOpUtil {

    private static final int OP_WRITE_SMS = 15;

    public static boolean isWriteEnabled(Context context) {
        int uid = getUid(context);
        L.e("uid:"+uid);
        Object opRes = checkOp(context, OP_WRITE_SMS, uid);
        if (opRes instanceof Integer) {
            return (Integer) opRes == AppOpsManager.MODE_ALLOWED;
        }
        return false;
    }

    public static boolean setWriteEnabled(Context context, boolean enabled) {
        int uid = getUid(context);
        L.e("uid:"+uid);
        int mode = enabled ? AppOpsManager.MODE_ALLOWED
                : AppOpsManager.MODE_IGNORED;
        L.e("mode:"+mode);

        return setMode(context, OP_WRITE_SMS, uid, mode);
    }

    private static Object checkOp(Context context, int code, int uid) {
        AppOpsManager appOpsManager = (AppOpsManager) context
                .getSystemService(Context.APP_OPS_SERVICE);
        Class appOpsManagerClass = appOpsManager.getClass();

        try {
            Class[] types = new Class[3];
            types[0] = Integer.TYPE;
            types[1] = Integer.TYPE;
            types[2] = String.class;
            Method checkOpMethod = appOpsManagerClass.getMethod("checkOp",
                    types);

            Object[] args = new Object[3];
            args[0] = Integer.valueOf(code);
            args[1] = Integer.valueOf(uid);
            args[2] = context.getPackageName();
            Object result = checkOpMethod.invoke(appOpsManager, args);

            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射，调用AppOpsManager 的setMode
     * @param context
     * @param code
     * @param uid
     * @param mode
     * @return
     */
    private static boolean setMode(Context context, int code, int uid, int mode) {
        AppOpsManager appOpsManager = (AppOpsManager) context
                .getSystemService(Context.APP_OPS_SERVICE);
        Class appOpsManagerClass = appOpsManager.getClass();

        try {
            Class[] types = new Class[4];
            types[0] = Integer.TYPE;
            types[1] = Integer.TYPE;
            types[2] = String.class;
            types[3] = Integer.TYPE;
            Method setModeMethod = appOpsManagerClass.getMethod("setMode",
                    types);

            Object[] args = new Object[4];
            args[0] = Integer.valueOf(code);
            args[1] = Integer.valueOf(uid);
            args[2] = context.getPackageName();
            args[3] = Integer.valueOf(mode);
            setModeMethod.invoke(appOpsManager, args);

            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static int getUid(Context context) {
        try {
            int uid = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_SERVICES).uid;

            return uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }



    public void deleteSMS(Context context,String number) {
        try {
            ContentResolver CR = context.getContentResolver();
            // Query SMS
            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor c = CR.query(uriSms, new String[] { "_id", "thread_id" },
                    null, null, null);
            if (null != c && c.moveToFirst()) {
                do {
                    // Delete SMS
                    long threadId = c.getLong(1);
                    int result = CR.delete(Uri
                                    .parse("content://sms/conversations/" + threadId),
                            null, null);
                    Log.d("deleteSMS", "threadId:: " + threadId + "  result::"
                            + result);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.d("deleteSMS", "Exception:: " + e);
        }
    }
}
