/*
 * 文 件 名:  SysSharePres.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  mKF67523
 * 修改时间:  2012-12-4
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package library.lanshifu.com.lsf_library.utils;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import library.lanshifu.com.lsf_library.baseapp.AppConfig;
import library.lanshifu.com.lsf_library.baseapp.BaseApplication;
import library.lanshifu.com.lsf_library.encrypt.DESedeEncrypt;

/**
 * 数据保存
 * 
 * @author mKF67523
 * @version [版本号, 2012-12-4]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PrefUtil {


    private static final String PRES_NAME = "system_share";

    
    private SharedPreferences mSharePres;

    private DESedeEncrypt dESedeEncrypt;

    /**
     * <默认构造函数>
     */
    private PrefUtil() {
        mSharePres = BaseApplication.getContext().getSharedPreferences(PRES_NAME,
                Context.MODE_PRIVATE);

        dESedeEncrypt = new DESedeEncrypt();
    }

    private static class SysSharePresHolder {

        static final PrefUtil INSTANCE = new PrefUtil();
    }

    public static PrefUtil getInstance() {
        return SysSharePresHolder.INSTANCE;
    }



    /************** set ***********************************************/
    public void putString(String key, String value) {
        if (AppConfig.isEncryptUserData && StringUtil.isNotEmpty(value)) {
            value = dESedeEncrypt.encrypt(value);
        }
        mSharePres.edit().putString(key, value).commit();
    }

    public void putBoolean(String key, Boolean value) {
        mSharePres.edit().putBoolean(key, value).commit();
    }

    public void putFloat(String key, float value) {
        mSharePres.edit().putFloat(key, value).commit();
    }

    public void putInt(String key, int value) {
        mSharePres.edit().putInt(key, value).commit();
    }

    public void putLong(String key, long value) {
        mSharePres.edit().putLong(key, value).commit();
    }

    /************** get ***********************************************/

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String def) {
        String value = mSharePres.getString(key, def);
        if (StringUtil.isEmpty(value)) {
            return value;
        }

        if (value.equals(def)) {
            return value;
        }

        if (AppConfig.isEncryptUserData) {
            value = dESedeEncrypt.decrypt(value);
        }
        return value;
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public Boolean getBoolean(String key, Boolean def) {
        return mSharePres.getBoolean(key, def);
    }

    public float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public float getFloat(String key, float def) {
        return mSharePres.getFloat(key, def);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int def) {
        return mSharePres.getInt(key, def);
    }

    public long getLong(String key) {
        return getLong(key, 0l);
    }

    public long getLong(String key, long def) {
        return mSharePres.getLong(key, def);
    }

    public void remove(String key) {
        mSharePres.edit().remove(key).commit();
    }

    /**
     * 兼容旧的数据，全部加密
     */
    public void upgradeEncrypt() {
        Map<String, ?> map = mSharePres.getAll();

        if (null==map) {
            // 为空
            return;
        }
        boolean encrypted = PrefUtil.getInstance().getBoolean("encrypted");

        if (encrypted) {
            return;
        }

        Iterator<?> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, ?> next = (Entry<String, ?>) it.next();
            if (next.getValue() instanceof String) {
                putString(next.getKey(), (String) next.getValue());
            }
        }
        PrefUtil.getInstance().putBoolean("encrypted", true);
    }

}
