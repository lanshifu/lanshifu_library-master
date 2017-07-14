
package library.lanshifu.com.lsf_library.utils;

import android.content.res.Resources;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import library.lanshifu.com.lsf_library.baseapp.BaseApplication;

/**
 * 字符串操作工具类
 */
public final class StringUtil {

    private StringUtil() {
    }

    /**
     * 判断是否中文字符串
     *
     * @param str 待判断的字符串
     * @return 如果包含中文返回true, 否则false
     */
    public static boolean isChinese(CharSequence str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        return str.toString().matches("[\\u4e00-\\u9fa5]+");
    }

    /**
     * 对字符串进行简单处理，空字符串处理成--
     *
     * @param str 待处理的字符串
     * @return 如果非空则原样返回，否则返回--
     */
    public static String nvl(String str) {
        if (StringUtil.isEmpty(str)) {
            return "--";
        }
        return str.trim().replace("\\n\\r", "");
    }

    /**
     * 获取key-value键值对的key
     *
     * @param src   原字符串
     * @param regex 用来分割的正则表达式
     * @return String 键值对的key
     */
    public static String getkey(String src, String regex) {
        return src.split(regex)[0];
    }

    /**
     * 获取key-value键值对的value
     *
     * @param src   原字符串
     * @param regex 用来分割的正则表达式
     * @return String 键值对的value
     */
    public static String getValue(String src, String regex) {
        String[] kv = src.split(regex);
        if (kv.length < 2) {
            return "";
        }
        return kv[1];
    }

    /**
     * 判断是否为中国移动的手机号码
     * "134;135;136;137;138;139;147;150;151;152;157;158;159;182;187;188";
     *
     * @param phoneNumber 手机号码
     * @return boolean 如果是中国移动号码则返回true,否则返回false
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        // 移动号段越来越多 去掉限制
        // final Pattern moblie_pattern =
        // Pattern.compile("(13[4-9]|14[7]|15[0-27-9]|18[23478])\\d{8}");
        // final Pattern moblie_pattern = Pattern.compile("(1[2-9])\\d{9}");
        // return moblie_pattern.matcher(phoneNumber).matches();
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(phoneNumber).matches();
    }

    /**
     * 判断是否是手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        // 移动号段越来越多 去掉限制
        // Pattern p =
        // Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,3-9]))\\d{8}$");
        // Pattern p = Pattern.compile("^(1[2-9])\\d{9}$");
        // Matcher m = p.matcher(mobiles);
        // return m.matches();
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(mobiles).matches();
    }

    /**
     * 前缀补0到指定位数 如果数字的位数不足,则补0到最少位数后返回,否则直接返回
     *
     * @param inValue     需要补0的数字
     * @param inMinDigits 要求的最少位数
     * @return String 补0后的数字字符串
     */
    public static String zeroPad(int inValue, int inMinDigits) {
        return String.format(Locale.getDefault(), "%0" + inMinDigits + "d", inValue);
    }

    /**
     * 复制boolean数组 因为Arrays.copyOf方法在API level 9才添加
     *
     * @param original 待复制的boolean数组
     * @return boolean[] 复制后的boolean数组
     */
    @Deprecated
    public static boolean[] arraycopy(boolean[] original) {
        if (original == null) {
            return null;
        }
        int originalLength = original.length;
        boolean[] result = new boolean[originalLength];
        System.arraycopy(original, 0, result, 0, originalLength);
        return result;
    }

    /**
     * <复制数组> <功能详细描述>
     *
     * @param src
     * @return String[] [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String[] arraycopy(CharSequence[] src) {
        if (src == null) {
            return null;
        }
        String[] dest = new String[src.length];
        System.arraycopy(src, 0, dest, 0, src.length);
        return dest;
    }

    /**
     * 复制String数组 因为Arrays.copyOf方法在API level 9才添加
     *
     * @param original 待复制的String数组
     * @return String[] 复制后的String数组
     */
    @Deprecated
    public static String[] arraycopy(String[] original) {
        if (original == null) {
            return null;
        }
        int originalLength = original.length;
        String[] result = new String[originalLength];
        System.arraycopy(original, 0, result, 0, originalLength);
        return result;
    }

    /**
     * 生成N位随机数
     *
     * @param length 随机数长度
     * @return String 生成的随机数
     */
    public static String getRandomNumbers(int length) {
        if (length <= 0) {
            return "";
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 生成N位随机字符串
     *
     * @param length 随机字符串长度
     * @return String 生成的随机字符串
     */
    public static String getRandomStrings(int length) {
        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] numAndLetters = str.toCharArray();

        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int position = random.nextInt(numAndLetters.length);
            sb.append(numAndLetters[position]);
        }

        return sb.toString();
    }

    /**
     * 是否为null或空字符串
     *
     * @param str 字符串
     * @return boolean 如果是null或长度为0,则返回true,否则返回false
     */
    public static boolean isEmpty(CharSequence str) {
        return null == str || str.length() == 0;
    }

    /**
     * 是否非null或非空字符串
     *
     * @param str 字符串
     * @return boolean 如果是null或长度为0,则返回false,否则返回true
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 对字符串数据按一定的分隔符进行拼接
     *
     * @param split 分隔符
     * @param strs  字符串数组
     * @return 拼接后的字符串
     */
    public static String join(String split, String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(strs[0]);
        for (int i = 1; i < strs.length; i++) {
            sb.append(split).append(strs[i]);
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否是整数
     *
     * @param value 待判断的字符串
     * @return boolean 如果是整数格式返回true,否则返回false
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是整数
     *
     * @param value 待判断的字符串
     * @return boolean 如果是整数格式返回true,否则返回false
     */
    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     *
     * @param value 待判断的字符串
     * @return boolean 如果是浮点数格式返回true,否则返回false
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return value.contains(".");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     *
     * @param value 待判断的字符串
     * @return boolean 如果是数字格式返回true,否则返回false
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 获取请求流水号 格式要求(20位):REGION(3)+请求年月日时分秒+5位流水号,如75011101412392100001
     *
     * @return String 流水号
     */
    public static String getSerialNumber() {
        return "JSCRM" + DateUtil.getCurrent(DateUtil.FORMAT_YYYYMMDDHHMMSS) + getRandomNumbers(5);
    }

    /**
     * 获取字典id
     *
     * @param groupid  字典组id
     * @param dictName 字典id
     * @return String 如果找到相应的字典组和字典,返回相应的字典名称,否则返回空字符串
     */
    public static String getDictId(String groupid, String dictName) {
        List<Dictitem> dictitems = StringUtil.getDictitems(groupid);
        for (Dictitem dictitem : dictitems) {
            if (dictitem.getDictname().equals(dictName)) {
                return dictitem.getDictid();
            }
        }
        return "";
    }

    /**
     * 获取字典名称
     *
     * @param groupid 字典组id
     * @param dictid  字典id
     * @return String 如果找到相应的字典组和字典,返回相应的字典名称,否则返回空字符串
     */
    public static String getDictName(String groupid, String dictid) {
        List<Dictitem> dictitems = StringUtil.getDictitems(groupid);
        for (Dictitem dictitem : dictitems) {
            if (dictitem.getDictid().equals(dictid)) {
                return dictitem.getDictname();
            }
        }
        return "";
    }

    /**
     * 获取字典组
     *
     * @param groupid 字典组id
     * @return List<Dictitem> 如果找到对应的字典组则返回,否则返回空列表
     */
    public static List<Dictitem> getDictitems(String groupid) {
        List<Dictitem> dictitems = new ArrayList<Dictitem>();
        Resources res = BaseApplication.getContext().getResources();
        int id = res.getIdentifier("dict_" + groupid, "array", BaseApplication.getContext()
                .getPackageName());
        if (id <= 0) {
            return dictitems;
        }
        String[] dicts = res.getStringArray(id);
        for (String s : dicts) {
            String[] ss = s.split("#");
            Dictitem dictitem = new Dictitem(ss[0], ss[1], groupid);
            dictitems.add(dictitem);
        }

        return dictitems;
    }

    /**
     * 对字符串进行特定长度的省略化
     *
     * @param str    需要截取的字符串
     * @param len    需要显示的长度(注意：长度是以byte为单位的，一个汉字是2个byte)
     * @param symbol 用于表示省略的信息的字符，如“...”,“>>>”等。
     * @return 返回处理后的字符串
     * @throws UnsupportedEncodingException 如果不支持GBK编码格式的话
     */
    public static String getLimitLengthString(String str, int len, String symbol)
            throws UnsupportedEncodingException {
        byte b[] = str.getBytes("GBK");
        if (b.length <= len)
            return str;
        int counterOfDoubleByte = 0;
        for (int i = 0; i < len; i++) {
            if (b[i] < 0)
                counterOfDoubleByte++;
        }

        if (counterOfDoubleByte % 2 != 0)
            len--;
        return new String(b, 0, len, "GBK") + symbol;
    }

    /**
     * 手机号码校验
     *
     * @param telNum 电话号码
     * @return
     */
    public static boolean regexTelephone(String telNum) {

        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(telNum);

        return m.find();// boolean
    }

    /**
     * 电话号码分割
     *
     * @param telNum
     * @return
     */
    public static String splitTelNum(String telNum) {
        if (telNum.length() != 11) {
            return telNum;
        }
        return telNum.substring(0, 3) + "-" + telNum.substring(3, 7) + "-"
                + telNum.substring(7, 11);
    }

    /**
     * 字符串转float
     */
    public static float getFloat(String str) {

        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Float.parseFloat(str.trim());
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    /**
     * 字符串转整数
     */
    public static int getInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 字符串转长整数
     */
    public static double getDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    /**
     * 保留两位小数
     *
     * @param str
     * @return
     */
    public static String formatDoubleDot(String str) {
        DecimalFormat df = new DecimalFormat("0.00");

        return df.format((StringUtil.getDouble(str)));
    }

    /**
     * 字符串转长整数
     */
    public static long getLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * 当空时，默认返回 “--”
     *
     * @param text
     */
    public static String getDisplayText(String text) {
        if (StringUtil.isEmpty(text)) {
            return "--";
        } else {
            return text;
        }
    }

    /**
     * 模糊化，我也不知道模糊化规则，且用着
     *
     * @param name
     * @return [参数说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Deprecated
    public static StringBuilder replace(String name) {

        StringBuilder sb = new StringBuilder();
        if (null == name || "".equals(name)) {
            return sb;
        }
        String regEx = "[`~!@#$%^&*×()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcherStart = pattern.matcher(name.substring(0, 1));
        Matcher matcherEnd = pattern.matcher(name.substring(name.length() - 1, name.length()));

        if (name.length() >= 3) {
            if (matcherStart.matches() && matcherEnd.matches()) {
                sb.append(name.substring(0, 2));
                for (int i = 2; i < name.length() - 2; i++) {
                    sb.append("*");
                }
                sb.append(name.substring(name.length() - 2, name.length()));
            } else if (!matcherStart.matches() && matcherEnd.matches()) {
                sb.append(name.substring(0, 1));
                for (int i = 1; i < name.length() - 2; i++) {
                    sb.append("*");
                }
                sb.append(name.substring(name.length() - 2, name.length()));
            } else if (matcherStart.matches() && !matcherEnd.matches()) {
                sb.append(name.substring(0, 2));
                for (int i = 2; i < name.length() - 1; i++) {
                    sb.append("*");
                }
                sb.append(name.substring(name.length() - 1, name.length()));
            } else {
                sb.append(name.substring(0, 1));
                for (int i = 1; i <= name.length() - 2; i++) {
                    sb.append("*");
                }
                sb.append(name.substring(name.length() - 1, name.length()));
            }

        } else if (name.length() == 2) {
            sb.append(name.substring(0, 1));
            sb.append("*");
        } else {
            sb.append(name);
        }

        return sb;
    }

    /**
     * 模糊化名称，替换名称中的名为*
     *
     * @param replaceName
     * @return
     */
    public static String replaceName(String replaceName) {
        StringBuilder name = new StringBuilder(replaceName);
        // 名称除第一个字符，其他用*代替
        for (int i = 1; i < name.length(); i++) {
            name.setCharAt(i, '*');
        }
        return name.toString();
    }

    /**
     * 模糊化证件号码 如果是身份证，出生年月日用*代替，最后一位用*代替，其他类型末4位用*代替,如果不满足4位目前不做处理
     *
     * @param replaceCertType 证件类型
     * @param replaceCert     证件号码
     * @return
     */
    public static String replaceCert(String replaceCertType, String replaceCert) {
        StringBuilder certId = new StringBuilder(replaceCert);
        int certLength = replaceCert.length();
        if (isNotEmpty(replaceCertType) && replaceCertType.contains("身份证")) {

            for (int i = 6; i < certLength - 4; i++) {
                certId.setCharAt(i, '*');
            }
            certId.setCharAt(certLength - 1, '*');

        } else if (certLength > 4) {
            for (int i = certLength - 4; i < certLength; i++) {
                certId.setCharAt(i, '*');
            }
        }
        return certId.toString();
    }

    /**
     * 换算 分 转 元
     *
     * @param price
     * @return
     */
    public static String fenToYuan(String price) {
        double temp = 0f;
        try {
            temp = Double.parseDouble(price);
            temp = temp / 100;
        } catch (NumberFormatException e) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(temp);

    }

    /**
     * 获取字符串的字符数组, 用于规避findbugs问题
     *
     * @param str 原字符串
     * @return 字节数组
     */
    public static byte[] toBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }
    }
}
