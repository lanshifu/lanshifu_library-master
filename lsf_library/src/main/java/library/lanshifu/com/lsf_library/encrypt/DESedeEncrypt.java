
package library.lanshifu.com.lsf_library.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * DESede加密
 *
 * @author g00140516
 * @version 1.0, 2012/03/21
 * @see
 */
public class DESedeEncrypt {
    /**
     * 编码格式
     */
    private static final String CHARSET = "GBK";

    /**
     * 加密算法
     */
    private static final String KEY_ALGORITHM = "DESede";

    private static final int HEX_16 = 16;

    private Cipher encryptCipher = null;

    private Cipher decryptCipher = null;

    private byte[] buffer = new byte[]{
            0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31,
            0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31
    };

    // private static int iS=0;

    /**
     * 构造方法
     *
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public DESedeEncrypt() {
        // 固定密钥
        // byte[] buffer = new byte[]{0x6B, 0x46, 0x0E, 0x64, 0x0D, 0x70, 0x5B,
        // 0x7A, (byte) 0xD9, 0x37, 0x68,
        // 0x67, (byte) 0xE0, 0x0B, (byte) 0xE9, 0x2C, 0x3D, 0x52, (byte) 0xF1,
        // 0x4F, (byte) 0xF1, (byte) 0x9E, (byte) 0xFE, (byte) 0x94};

        SecretKeySpec key = new SecretKeySpec(buffer, "DESede");

        try {
            encryptCipher = Cipher.getInstance(KEY_ALGORITHM);
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance("DESede/ECB/NoPadding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("找不到加解密算法," + e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("加解密算法的Padding规则不存在," + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("加解密算法的秘钥无效," + e.getMessage());
        }
    }

    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     */
    public byte[] encrypt(byte[] arrB) {
        try {
            return encryptCipher.doFinal(arrB);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("加密失败," + e.getMessage());
        } catch (BadPaddingException e) {
            throw new RuntimeException("加密失败," + e.getMessage());
        }
    }

    /**
     * 加密字符串
     *
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     */
    public String encrypt(String strIn) {
        if (null == strIn || "".equals(strIn)) {
            return "";
        }

        int i = 0;
        i = strIn.length() % 8;

        StringBuffer sb = new StringBuffer();
        sb.append(strIn);
        if (0 == i) {
            for (i = 0; i < 8; i++) {
                sb.append("\0");
            }
        } else {
            while (i > 0) {
                sb.append("\0");
                i--;
            }
        }
        strIn = sb.toString();
        try {
            return byteArrToHexStr(encrypt(strIn.getBytes(CHARSET))).trim();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("加密失败," + e.getMessage());
        }
    }

    /**
     * 将byte数组转换为表示16进制值的字符串
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     */
    public String byteArrToHexStr(byte[] arrB) {
        int iLen = arrB.length;

        // 每个byte用十六进制两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);

        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];

            // 把负数转换为正数
            if (intTmp < 0) {
                intTmp = intTmp + 256;
            }

            // 小于0F的数需要在前面补0

            if (intTmp < HEX_16) {
                sb.append("0");
                // System.out.println(iS++);
            }
            sb.append(Integer.toString(intTmp, HEX_16));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     */
    public byte[] decrypt(byte[] arrB) {
        try {
            return decryptCipher.doFinal(arrB);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("解密失败," + e.getMessage());
        } catch (BadPaddingException e) {
            throw new RuntimeException("解密失败," + e.getMessage());
        }
    }

    /**
     * 解密字符串
     *
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     */
    public String decrypt(String strIn) {
        try {
            return new String(decrypt(hexStrToByteArr(strIn)), CHARSET).trim();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("解密失败," + e.getMessage());
        }
    }

    /**
     * 将表示16进制值的字符串转换为byte数组
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public static byte[] hexStrToByteArr(String strIn) throws UnsupportedEncodingException {
        byte[] arrB = strIn.getBytes(CHARSET);
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];

        String strTmp = "";
        for (int i = 0; i < iLen; i = i + 2) {
            strTmp = new String(arrB, i, 2, CHARSET);

            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    // public static void main(String[] args)
    // {
    // try
    // {
    // byte[] buffer = new byte[]{0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31,
    // 0x31, 0x31,
    // 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31,
    // 0x31, 0x31, 0x31};
    //
    // System.out.println("密钥（16进制）" + byteArrToHexStr(buffer));
    // String password = "<v:Envelope xmlns:i=\"http:";
    // System.out.println("加密前的字符：" + password);
    // DESedeEncrypt des = new DESedeEncrypt();
    // String stren = DESedeEncrypt.encrypt(password);
    // System.out.println("加密后的字符：" + stren);
    // String strde = DESedeEncrypt.decrypt(stren);
    // System.out.println("解密后的字符：" + strde.trim());
    // }
    // catch (Exception e)
    // {
    // e.printStackTrace();
    // }
    // }

}
