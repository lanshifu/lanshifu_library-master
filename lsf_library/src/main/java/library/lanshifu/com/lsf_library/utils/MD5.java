package library.lanshifu.com.lsf_library.utils;

import java.security.MessageDigest;

/**
 * 对字符串进行签名的操作
 */
public final class MD5 {
    private static final char[] hexDigits = "0123456789ABCDEF".toCharArray();

    private MD5() {
    }

    /**
     * 对字符串进行MD5签名
     *
     * @param src 待处理的字符串
     * @return String 如果存在MD5算法则返回MD5签名,否则返回原字符串
     */
    public static String getMD5(String src) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(src.getBytes());

            byte[] md = messagedigest.digest();
            int len = md.length;

            char str[] = new char[len * 2];
            int k = 0;
            for (int i = 0; i < len; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >>> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return src;
        }
    }
}
