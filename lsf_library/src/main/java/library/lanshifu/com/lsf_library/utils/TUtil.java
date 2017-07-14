package library.lanshifu.com.lsf_library.utils;

import java.lang.reflect.ParameterizedType;

/**
 * 类转换初始化
 */
public class TUtil {


    /**
     *  getGenericSuperclass()通过反射获取当前类表示的实体（类，接口，基本类型或void）
     * 的直接父类的Type，getActualTypeArguments()返回参数数组。
     * @return
     */

    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
