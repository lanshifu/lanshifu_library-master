
package library.lanshifu.com.lsf_library.encrypt;

import android.util.Base64;

public final class Crypto {
    static {
        System.loadLibrary("g3crypto");
    }

    public native static byte[] encrypt(byte[] src);

    public native static byte[] decrypt(byte[] src);

    public static String encrypt(String src) {
        if (null == src || "".equals(src)) {
            return "";
        }
        try {
            byte[] output = encrypt(src.getBytes());
            return Base64.encodeToString(output, Base64.URL_SAFE | Base64.NO_WRAP);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String src) {
        if (null == src || "".equals(src)) {
            return "";
        }
        try {
            byte[] output = decrypt(Base64.decode(src, Base64.URL_SAFE | Base64.NO_WRAP));
            return new String(output);
        } catch (Exception e) {
            return null;
        }
    }
}
