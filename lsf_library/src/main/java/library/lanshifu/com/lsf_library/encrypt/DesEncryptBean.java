/*
 * 文 件 名:  EncryptBean.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  d00108397
 * 修改时间:  Apr 28, 2009
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package library.lanshifu.com.lsf_library.encrypt;

import android.content.Context;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author d00108397
 * @version [版本号, Apr 28, 2009]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@SuppressWarnings("unchecked")
public class DesEncryptBean {
    
    private static final String TAG = "DesEncryptBean";

    private static DesEncryptBean desEncryptBean;

    private static String privateKey;

    private static String publicKey;

    public static final int NUMBER_SIX_FOUR = 64;

    public static final int NUMBER_ONE_SIX = 16;

    public static final int NUMBER_ONE_ZERO_TWO_FOUR = 1024;

    public static final int EIGHT_ONE = 0XFF;

    public static final String PRIVATE_KEY_FILENAME = "private.kf.key";

    public static final String PUBLIC_KEY_FILENAME = "public.kf.key";

    public static final String ALGORITHM_DSA = "DSA";

    public static final String ALGORITHM_RSA = "RSA";

    public static final String ALGORITHM_DES = "DES";

    public static final int KEY_SIZE_512 = 512;

    public static final int KEY_SIZE_768 = 768;

    public static final int KEY_SIZE_1024 = 1024;

    private static Class rsaPriKeySpec = null;

    private static Class rsaPubKeySpec = null;

    private static Class dsaPriKeySpec = null;

    private static Class dsaPubKeySpec = null;

    private static KeyFactory dsaKF = null;

    private static KeyFactory rsaKF = null;

    private static final String KEY_TYPE_PUBLIC = "public";

    private static final String KEY_TYPE_PRIVATE = "private";

    private static final String JSON_PROPERTIES_KEYTYPE = "keyType";

    private static final String JSON_PROPERTIES_ALGORITHM = "algorithm";

    private static final String JSON_PROPERTIES_DSA_G = "g";

    private static final String JSON_PROPERTIES_DSA_P = "p";

    private static final String JSON_PROPERTIES_DSA_Q = "q";

    private static final String JSON_PROPERTIES_DSA_X = "x";

    private static final String JSON_PROPERTIES_DSA_Y = "y";

    private static final String JSON_PROPERTIES_RSA_MODULUS = "modulus";

    private static final String JSON_PROPERTIES_RSA_EXPONENT = "exponent";

    private KeyPairGenerator keyGen = null;

    private PublicKey pubKey = null;

    private PrivateKey priKey = null;

    private final String passwordKeyName = "password";

    private String passwordKey = "__G3ESOPSERVICE__";

    private String algorithm = "DES";

    private Context context;

    public DesEncryptBean(Context context) {
        this.context = context;
    }

    static {
        try {
            dsaKF = KeyFactory.getInstance(ALGORITHM_DSA);
        } catch (NoSuchAlgorithmException e) {
            dsaKF = null;
            // LOGGER.error(e.getMessage(), e);
        }
        try {
            rsaKF = KeyFactory.getInstance(ALGORITHM_RSA);
        } catch (NoSuchAlgorithmException e) {
            rsaKF = null;
            // LOGGER.error(e.getMessage(), e);
        }

        try {
            rsaPriKeySpec = Class.forName("java.security.spec.RSAPrivateKeySpec");
        } catch (ClassNotFoundException e) {
            rsaPriKeySpec = null;
            // LOGGER.error(e.getMessage(), e);
        }
        try {
            rsaPubKeySpec = Class.forName("java.security.spec.RSAPublicKeySpec");
        } catch (ClassNotFoundException e) {
            rsaPubKeySpec = null;
            // LOGGER.error(e.getMessage(), e);
        }
        try {
            dsaPriKeySpec = Class.forName("java.security.spec.DSAPrivateKeySpec");
        } catch (ClassNotFoundException e) {
            dsaPriKeySpec = null;
            // LOGGER.error(e.getMessage(), e);
        }
        try {
            dsaPubKeySpec = Class.forName("java.security.spec.DSAPublicKeySpec");
        } catch (ClassNotFoundException e) {
            dsaPubKeySpec = null;
            // LOGGER.error(e.getMessage(), e);
        }

    }

    public static synchronized DesEncryptBean getInstance(Context context)
            throws NoSuchAlgorithmException {
        if (desEncryptBean == null) {
            desEncryptBean = new DesEncryptBean(context);
        }
        return desEncryptBean;
    }

    public static String getPrivateKey() throws Exception {
        if (privateKey == null) {
            privateKey = desEncryptBean.readFile(PRIVATE_KEY_FILENAME);
        }
        return privateKey;
    }

    public static String getPublicKey() throws Exception {
        if (publicKey == null) {
            publicKey = desEncryptBean.readFile(PUBLIC_KEY_FILENAME);
        }
        return publicKey;
    }

    /**
     * token加密
     * 
     * @param context [上下文]
     * @param token [票据]
     * 
     * @return String [加密字符串]
     */
    public static String getEncryptedAndSignedToken(Context context, String token) {
        try {
            getInstance(context);
            desEncryptBean.setPriKeyUseJsonString(getPrivateKey());
        } catch (Exception e) {
            Log.i(TAG, "加密票据出错！");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("token=");
        sb.append(token);
        sb.append("&systime=");
        sb.append(System.currentTimeMillis());
        sb.append("&from=g3esopterminal");
        String dynamicToken = sb.toString();
        byte[] signDataCrypt = desEncryptBean.signUseRSAMD5(dynamicToken.getBytes());
        if(null!=signDataCrypt){
        	String msgCrypt = desEncryptBean.encryptWithDes(dynamicToken);
        	sb.delete(0, sb.length());
        	sb.append(msgCrypt);
        	sb.append(":");
        	sb.append(DesEncryptBean.byte2hex(signDataCrypt));
        }
        return sb.toString();
    }

    /**
     * token解密
     * 
     * @param context [上下文]
     * @param encryptedToken [后台返回的字符串]
     * 
     * @return String [加密字符串]
     */
    public static Map<String, String> getVerifyedAndDecryptedTokenMap(Context context,
                                                                      String encryptedToken) {
        try {
            getInstance(context);
            desEncryptBean.setPubKeyUseJsonString(getPublicKey());
        } catch (Exception e) {
            Log.i(TAG, "解密票据出错!");
        }
        
        String[] datas = encryptedToken.split(":");
        String des = desEncryptBean.decryptWithDes(datas[0]);

        if (desEncryptBean.verifyUseRSAMD5(des.getBytes(), DesEncryptBean.hex2byte(datas[1]
                .getBytes()))) {
            Map<String, String> tokenInfo = new HashMap<String, String>();
            List<String> strs = desEncryptBean.getListBySplitStr(des, "&");
            for (int i = 0; i < strs.size(); i++) {
                List<String> keyValueList = desEncryptBean.getListBySplitStr(strs.get(i), "=");
                tokenInfo.put(keyValueList.get(0), keyValueList.get(1));
            }
            return tokenInfo;
        } else {
            return new HashMap<String, String>();
        }
    }

    private List<String> getListBySplitStr(String srcStr, String sign) {
        String splitStr = srcStr;

        // 定义返回的参数
        List<String> pramaList = new ArrayList<String>();
        String subsprama = "";

        // 循环输入的字符串长度，查看是否还有要截取的字符串
        while (splitStr.length() > 0) {
            // 查询特殊符号的位置
            int signSum = splitStr.indexOf(sign);

            // 判断是否有两个特殊符号连在一起 如 ..##.. 情况
            if (signSum != -1) {
                // 把取到的值放入要返回的数组中
                subsprama = splitStr.substring(0, signSum);
                pramaList.add(subsprama);
            } else {
                // 将截断的末尾字符窜添加
                String str = splitStr.trim();
                if (!"".equals(str))
                    pramaList.add(str);

                break;
            }
            // 更新截取后的字符串
            splitStr = splitStr.substring(signSum + 1);
        }
        return pramaList;
    }

    public DesEncryptBean(String algorithm, int keySize) throws NoSuchAlgorithmException {
        keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(keySize);
    }

    /**
     * 默认使用RSA算法，512密钥长度初始化密钥生成器
     */
    @SuppressWarnings("unused")
    private DesEncryptBean() throws NoSuchAlgorithmException {
        keyGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        keyGen.initialize(KEY_SIZE_512);
    }

    /**
     * 使用MD5withRSA算法对原数据进行签名验证 签名成功返回true 异常或失败均返回false
     * 
     * @param raw 原数据字节组
     * @param signData 签名数据字节组
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final boolean verifyUseRSAMD5(byte[] raw, byte[] signData) {
        Signature signer = null;
        try {
            signer = Signature.getInstance("MD5withRSA");// 创建签名类
            signer.initVerify(pubKey);// 使用密钥对签名类初始化
            signer.update(raw);// 添加原数据
            return signer.verify(signData); // 将原数据与签名数据进行对比
        } catch (NoSuchAlgorithmException noAlgorithm) {
            // LOGGER.error(noAlgorithm.getMessage(), noAlgorithm);
            return false;
        } catch (InvalidKeyException badKey) {
            // LOGGER.error(badKey.getMessage(), badKey);
            return false;
        } catch (SignatureException e) {
            // LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 带有RSA加密签名数据的,使用MD5withRSA算法对原数据进行签名验证 签名成功返回true 异常或失败均返回false
     * 
     * @param raw 原数据字节组
     * @param signData 加密后的签名数据字节组
     * @return
     * @throws Exception [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final boolean verifyUseRSAMD5WithCrypt(byte[] raw, byte[] signData) throws Exception {
        byte[] decryptData = decryptWithRSA(signData);
        return verifyUseRSAMD5(raw, decryptData);
    }

    /**
     * 使用MD5withRSA算法对原数据进行签名 返回签名数据字节组,如果异常则返回null
     * 
     * @param obj 需要签名的数据字节组
     * @return [参数说明]
     * @return byte[] 签名数据字节组
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final byte[] signUseRSAMD5(byte[] obj) {
        Signature signer = null;
        byte[] signData = null;
        try {
            signer = Signature.getInstance("MD5withRSA");
            signer.initSign(priKey);
            signer.update(obj);
            signData = signer.sign();
        } catch (NoSuchAlgorithmException noAlgorithm) {
            // LOGGER.error(noAlgorithm.getMessage(), noAlgorithm);
        } catch (InvalidKeyException badKey) {
            // LOGGER.error(badKey.getMessage(), badKey);
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            // LOGGER.error(e.getMessage(), e);
        }
        return signData;
    }

    /**
     * 带有RSA加密的,使用MD5withRSA算法对原数据进行签名 返回加密后的签名数据字节组,如果异常则返回null
     * 
     * @param obj 需要签名的数据字节组
     * @return [参数说明]
     * @return byte[] 加密后的签名数据字节组
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final byte[] signUseRSAMD5WithCrypt(byte[] obj) throws Exception {
        byte[] signData = signUseRSAMD5(obj);
        return (signData!=null) ? encryptWithRSA(signData) : new byte[]{};
    }

    /**
     * RSA加密 默认使用PrivateKey进行加密
     * 
     * @param data 待加密的明文数据
     * @return
     * @throws Exception [参数说明]
     * @return byte[] [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final byte[] encryptWithRSA(byte[] data) throws Exception {
        return encryptWithRSA(priKey, data);
    }

    /**
     * RSA加密
     * 
     * @param key 加密的密钥
     * @param data 待加密的明文数据
     * @return 加密后的数据
     * @throws Exception
     */
    public final byte[] encryptWithRSA(Key key, byte[] data) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA,
                    "org.bouncycastle.jce.provider.BouncyCastleProvider");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 获得加密块大小，如:加密前数据为128个byte，而key_size=1024 加密块大小为127
            // byte,加密后为128个byte;
            // 因此共有2个加密块，第一个127 byte第二个为1个byte
            int blockSize = cipher.getBlockSize();
            int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length
                    / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i
                            * outputSize);
                }
                // 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中
                // ，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
                i++;
            }
            return raw;
        } catch (Throwable e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * RSA解密 默认使用PublicKey进行解密
     * 
     * @param raw 已经加密的数据
     * @return
     * @throws Exception [参数说明]
     * @return byte[] [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final byte[] decryptWithRSA(byte[] raw) throws Exception {
        return decryptWithRSA(pubKey, raw);
    }

    /**
     * RSA解密
     * 
     * @param key 解密的密钥
     * @param raw 已经加密的数据
     * @return 解密后的明文
     * @throws Exception
     */
    public final byte[] decryptWithRSA(Key key, byte[] raw) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA,
                    "org.bouncycastle.jce.provider.BouncyCastleProvider");
            cipher.init(Cipher.DECRYPT_MODE, key);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream(NUMBER_SIX_FOUR);
            int j = 0;
            while (raw.length - j * blockSize > 0) {
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
                j++;
            }
            return bout.toByteArray();
        } catch (Throwable e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 重新构造密钥生成器,并自动生成一对密钥
     * 
     * @param algorithm
     * @param keySize
     * @throws NoSuchAlgorithmException [当算法不支持的时候抛出此异常]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final void setKeyGen(String algorithm, int keySize) throws NoSuchAlgorithmException {
        keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(keySize);
        generateKeyPair();
    }

    /**
     * 使用设置好的密钥生成器生成一对密钥 生成的密钥保存在bean对象的pubKey和priKey属性中
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final void generateKeyPair() {
        KeyPair keyPair = keyGen.genKeyPair();
        pubKey = keyPair.getPublic();
        priKey = keyPair.getPrivate();
    }

    /**
     * 生成密钥文件
     * 
     * @return void [返回类型说明]
     * @throws Exception
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final void generateKeyPair(String path, String privateFile, String publicFile)
            throws Exception {
        generateKeyPair();
        String priStr = this.getPriKeyASJsonString() ;
        if(priStr!=null){
        	// 写私钥
        	wirteFile(priStr, path, privateFile);
        }
        String pubStr = this.getPubKeyASJsonString();
        if(pubStr!=null){
        	// 写公钥
        	wirteFile(pubStr, path, publicFile);
        }
    }

    /**
     * 讀密钥文件
     *
     * @param file
     * @throws Exception
     */
    public final String readFile(String file) throws Exception {
        Log.i(TAG, "context：" + context);
        BufferedInputStream in = new BufferedInputStream(context.getAssets().open(file));

        try {
            byte[] b = new byte[NUMBER_ONE_ZERO_TWO_FOUR];
            int size = 0;
            StringBuffer result = new StringBuffer();
            while ((size = in.read(b)) > 0) {
                result.append(new String(b, 0, size));
            }
            return result.toString();
        } catch (IOException e) {
            // e.printStackTrace();
            throw new Exception("不可读文件:" + file, e);
        } finally {
            if (null != in) {
                in.close();
            }
        }
    }

    /**
     * 写密钥文件
     * 
     * @param path
     * @param privateFile
     * @throws Exception
     */
    public final String readFile(String path, String privateFile) throws Exception {
        File p = new File(path);
        if (!p.exists()) {
            if (!p.mkdirs()) {
                return "";
                // throw new Exception("Key存放目录不存在且不可新建。");
            }
        }
        File pf = new File(p, privateFile);

        if (!pf.exists()) {
            return "";
            // pf.createNewFile();
        }

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(pf));

        try {
            byte[] b = new byte[NUMBER_ONE_ZERO_TWO_FOUR];
            int size = 0;
            StringBuffer result = new StringBuffer();
            while ((size = in.read(b)) > 0) {
                result.append(new String(b, 0, size));
            }
            return result.toString();
        } catch (IOException e) {
            // e.printStackTrace();
            throw new Exception("不可读文件:" + pf.getAbsolutePath(), e);
        } finally {
            if (null != in) {
                in.close();
            }
        }
    }

    /**
     * 写密钥文件
     * 
     * @param path
     * @param privateFile
     * @throws Exception
     */
    private void wirteFile(String content, String path, String privateFile) throws Exception {
        File p = new File(path);
        if (!p.exists()) {
            if (!p.mkdirs()) {
                return;
                // throw new Exception("Key存放目录不存在且不可新建。");
            }
        }
        File pf = new File(p, privateFile);

        if (!pf.exists()) {
            return;
            // pf.createNewFile();
        }

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(pf));

        try {
            out.write(content.getBytes());
        } catch (IOException e) {
            // e.printStackTrace();
            throw new Exception("不可写:" + pf.getAbsolutePath(), e);
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 设置public key <功能详细描述>
     * 
     * @param pubKey [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final void setPubKey(PublicKey pubKey) {
        this.pubKey = pubKey;
    }

    /**
     * @return 返回 pubKey
     */
    public final PublicKey getPubKey() {
        return pubKey;
    }

    /**
     * 设置private key <功能详细描述>
     * 
     * @param priKey [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final void setPriKey(PrivateKey priKey) {
        this.priKey = priKey;
    }

    /**
     * @return 返回 priKey
     */
    public final PrivateKey getPriKey() {
        return priKey;
    }

    /**
     * 以JSON数据形式返回pulickKey 如果key不存在，转换器无法初始化，或者Key工厂无法创建的情况下返回null
     * 
     * @return [参数说明]
     * @return String [返回类型说明]
     * @throws JSONException
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final String getPubKeyASJsonString() throws JSONException {
        // 不存在则直接返回null
        if (pubKey == null) {
            return null;
        }
        JSONObject json = new JSONObject();
        String algorithmValue = pubKey.getAlgorithm();
        json.put(JSON_PROPERTIES_KEYTYPE, KEY_TYPE_PUBLIC);
        json.put(JSON_PROPERTIES_ALGORITHM, algorithmValue);// 加密算法
        json.put(passwordKeyName, passwordKey);// 加密密码
        if (ALGORITHM_DSA.equals(algorithmValue)) {
            if (dsaKF == null || dsaPubKeySpec == null) {
                return null;
            }
            try {
                DSAPublicKeySpec ks = (DSAPublicKeySpec)dsaKF.getKeySpec(pubKey, dsaPubKeySpec);
                json.put(JSON_PROPERTIES_DSA_G, ks.getG().toString());
                json.put(JSON_PROPERTIES_DSA_P, ks.getP().toString());
                json.put(JSON_PROPERTIES_DSA_Q, ks.getQ().toString());
                json.put(JSON_PROPERTIES_DSA_Y, ks.getY().toString());
            } catch (InvalidKeySpecException e) {
                // LOGGER.error(e.getMessage(), e);
                return null;
            }
        } else if (ALGORITHM_RSA.equals(algorithmValue)) {

            if (rsaKF == null || rsaPubKeySpec == null) {
                return null;
            }
            try {
                RSAPublicKeySpec ks = (RSAPublicKeySpec)rsaKF.getKeySpec(pubKey, rsaPubKeySpec);
                json.put(JSON_PROPERTIES_RSA_MODULUS, ks.getModulus().toString());
                json.put(JSON_PROPERTIES_RSA_EXPONENT, ks.getPublicExponent().toString());
            } catch (InvalidKeySpecException e) {
                // LOGGER.error(e.getMessage(), e);
                return null;
            }
        }
        return json.toString();
    }

    /**
     * 以JSON数据形式返回privateKey 如果key不存在，转换器无法初始化，或者Key工厂无法创建的情况下返回null
     * 
     * @return [参数说明]
     * @return String [返回类型说明]
     * @throws JSONException
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final String getPriKeyASJsonString() throws JSONException {
        // 不存在则直接返回null
        if (priKey == null) {
            return null;
        }
        JSONObject json = new JSONObject();
        String algorithmValue = priKey.getAlgorithm();
        json.put(JSON_PROPERTIES_KEYTYPE, KEY_TYPE_PRIVATE);
        json.put(JSON_PROPERTIES_ALGORITHM, algorithmValue);// 加密算法
        json.put(passwordKeyName, passwordKey);// 加密密码
        if (ALGORITHM_DSA.equals(algorithmValue)) {
            if (dsaKF == null || dsaPriKeySpec == null) {
                return null;
            }
            try {
                DSAPrivateKeySpec ks = (DSAPrivateKeySpec)dsaKF.getKeySpec(priKey, dsaPriKeySpec);
                json.put(JSON_PROPERTIES_DSA_G, ks.getG().toString());
                json.put(JSON_PROPERTIES_DSA_P, ks.getP().toString());
                json.put(JSON_PROPERTIES_DSA_Q, ks.getQ().toString());
                json.put(JSON_PROPERTIES_DSA_X, ks.getX().toString());
            } catch (InvalidKeySpecException e) {
                // LOGGER.error(e.getMessage(), e);
                return null;
            }
        } else if (ALGORITHM_RSA.equals(algorithmValue)) {

            if (rsaKF == null || rsaPriKeySpec == null) {
                return null;
            }
            try {
                RSAPrivateKeySpec ks = (RSAPrivateKeySpec)rsaKF.getKeySpec(priKey, rsaPriKeySpec);
                json.put(JSON_PROPERTIES_RSA_MODULUS, ks.getModulus().toString());
                json.put(JSON_PROPERTIES_RSA_EXPONENT, ks.getPrivateExponent().toString());
            } catch (InvalidKeySpecException e) {
                // LOGGER.error(e.getMessage(), e);
                return null;
            }
        }
        return json.toString();
    }

    /**
     * 将JSON字符串转化为公有密钥 转换成功返回true,转化失败返回false
     * 
     * @param jsonStr
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @throws JSONException
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final boolean setPubKeyUseJsonString(String jsonStr) throws JSONException {
        JSONObject json = new JSONObject(jsonStr);
        String keyType = json.getString(JSON_PROPERTIES_KEYTYPE);
        String algorithmValue = json.getString(JSON_PROPERTIES_ALGORITHM);

        String password = json.getString(passwordKeyName);
        if (keyType == null || algorithmValue == null) {
            return false;// 无法识别json数据是甚么密钥
        }
        if (password != null) {
            passwordKey = password;
        }

        algorithm = algorithmValue;

        if ((ALGORITHM_DES.equals(algorithmValue) || ALGORITHM_RSA.equals(algorithmValue))
                && KEY_TYPE_PUBLIC.equals(keyType))// 如果是RSA算法
        // 并且是公钥的JSON数据
        {
            return setRsaPubKeyUseJsonString(json);
        } else if (ALGORITHM_DSA.equals(algorithmValue) && KEY_TYPE_PUBLIC.equals(keyType))// 如果是DSA算法
        // 并且是公钥的JSON数据
        {
            return setDsaPubKeyUseJsonString(json);
        }
        return false;

    }

    private boolean setDsaPubKeyUseJsonString(JSONObject json) throws JSONException {
        String gStr = json.getString(JSON_PROPERTIES_DSA_G);
        String pStr = json.getString(JSON_PROPERTIES_DSA_P);
        String qStr = json.getString(JSON_PROPERTIES_DSA_Q);
        String yStr = json.getString(JSON_PROPERTIES_DSA_Y);
        if (gStr != null && pStr != null && qStr != null && yStr != null) {
            BigInteger g = new BigInteger(gStr);
            BigInteger p = new BigInteger(pStr);
            BigInteger q = new BigInteger(qStr);
            BigInteger y = new BigInteger(yStr);
            DSAPublicKeySpec ks = new DSAPublicKeySpec(y, p, q, g);
            try {
                pubKey = dsaKF.generatePublic(ks);
            } catch (InvalidKeySpecException e) {
                // LOGGER.error(e.getMessage(), e);
                return false;
            }
        }
        return false;
    }

    private boolean setRsaPubKeyUseJsonString(JSONObject json) throws JSONException {
        String modulusStr = json.getString(JSON_PROPERTIES_RSA_MODULUS);
        String exponentStr = json.getString(JSON_PROPERTIES_RSA_EXPONENT);
        if (modulusStr != null && exponentStr != null) {
            BigInteger modulus = new BigInteger(modulusStr);
            BigInteger exponent = new BigInteger(exponentStr);
            RSAPublicKeySpec ks = new RSAPublicKeySpec(modulus, exponent);
            try {
                pubKey = rsaKF.generatePublic(ks);
            } catch (InvalidKeySpecException e) {
                // LOGGER.error(e.getMessage(), e);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 将JSON字符串转化为私有密钥 转换成功返回true,转化失败返回false
     * 
     * @param jsonStr
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @throws JSONException
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final boolean setPriKeyUseJsonString(String jsonStr) throws JSONException {
        JSONObject json = new JSONObject(jsonStr);
        String keyType = json.getString(JSON_PROPERTIES_KEYTYPE);
        String algorithmValue = json.getString(JSON_PROPERTIES_ALGORITHM);
        if (keyType == null || algorithmValue == null) {
            return false;// 无法识别json数据是甚么密钥
        }
        algorithm = algorithmValue;
        if ((ALGORITHM_DES.equals(algorithmValue) || ALGORITHM_RSA.equals(algorithmValue))
                && KEY_TYPE_PRIVATE.equals(keyType))// 如果是RSA算法
        // 并且是公钥的JSON数据
        {
            return setRsaPriKeyUseJsonString(json);
        } else if (ALGORITHM_DSA.equals(algorithmValue) && KEY_TYPE_PRIVATE.equals(keyType))// 如果是DSA算法
        // 并且是公钥的JSON数据
        {
            return setDsaPriKeyUseJsonString(json);
        }
        return false;
    }

    private boolean setDsaPriKeyUseJsonString(JSONObject json) throws JSONException {
        String gStr = json.getString(JSON_PROPERTIES_DSA_G);
        String pStr = json.getString(JSON_PROPERTIES_DSA_P);
        String qStr = json.getString(JSON_PROPERTIES_DSA_Q);
        String xStr = json.getString(JSON_PROPERTIES_DSA_X);
        if (gStr != null && pStr != null && qStr != null && xStr != null) {
            BigInteger g = new BigInteger(gStr);
            BigInteger p = new BigInteger(pStr);
            BigInteger q = new BigInteger(qStr);
            BigInteger x = new BigInteger(xStr);
            DSAPrivateKeySpec ks = new DSAPrivateKeySpec(x, p, q, g);
            try {
                priKey = dsaKF.generatePrivate(ks);
            } catch (InvalidKeySpecException e) {
                // LOGGER.error(e.getMessage(), e);
                return false;
            }
        }
        return false;
    }

    private boolean setRsaPriKeyUseJsonString(JSONObject json) throws JSONException {
        String modulusStr = json.getString(JSON_PROPERTIES_RSA_MODULUS);
        String exponentStr = json.getString(JSON_PROPERTIES_RSA_EXPONENT);
        if (modulusStr != null && exponentStr != null) {
            BigInteger modulus = new BigInteger(modulusStr);
            BigInteger exponent = new BigInteger(exponentStr);
            RSAPrivateKeySpec ks = new RSAPrivateKeySpec(modulus, exponent);
            try {
                priKey = rsaKF.generatePrivate(ks);
            } catch (InvalidKeySpecException e) {
                // LOGGER.error(e.getMessage(), e);
                return false;
            }
            return true;
        }
        return false;
    }

    public static final String byte2hex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & EIGHT_ONE));
            if (stmp.length() == 1) {
                sb.append("0").append(stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    public static final byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, NUMBER_ONE_SIX);
        }
        return b2;
    }

    public final String decryptWithDes(String data) {
        try {
            return new String(decrypt(hex2byte(data.getBytes()), passwordKey.getBytes()));
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return "";
    }

    public final String encryptWithDes(String data) {
        try {
            return byte2hex(encrypt(data.getBytes(), passwordKey.getBytes()));
        } catch (Exception e) {
            Log.e("111", e.toString());
        }
        return "";
    }

    public final byte[] encrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(src);
    }

    public final byte[] decrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(src);
    }

}
