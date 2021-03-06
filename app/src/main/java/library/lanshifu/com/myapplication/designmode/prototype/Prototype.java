package library.lanshifu.com.myapplication.designmode.prototype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 原型模式
 * 该模式的思想就是将一个对象作为原型，对其进行复制、克隆，产生一个和原对象类似的新对象。
 *
 * 浅复制：将一个对象复制后，基本数据类型的变量都会重新创建，而引用类型，指向的还是原对象所指向的。
   深复制：将一个对象复制后，不论是基本数据类型还有引用类型，都是重新创建的。简单来说，就是深复制进行了完全彻底的复制，而浅复制不彻底。
 *
 * 当创建新的对象比较复杂或者耗时天长，而新建对象又与已有对象相似时。可以使用原型模式，通过复制一个新的对象
 * 提高创建效率。
 *
 * Created by lanshifu on 2017/9/3.
 */

public class Prototype implements Cloneable,Serializable{

    private static final long serialVersionUID = 1L;
    private String string;

    /**
     * 浅复制
     * */
    public Object clone() throws CloneNotSupportedException {

        /**
         * super.clone()调用的是Object的clone()方法，而在Object类中，clone()是native的
         */
        Prototype prototype = (Prototype) super.clone();
        return prototype;
    }

    /**
     * 深复制
     */
    public Object deepClone() throws IOException, ClassNotFoundException {

         /* 写入当前对象的二进制流 */
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

         /* 读出二进制流产生的新对象 */
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

}
