
package library.lanshifu.com.lsf_library.utils;

import java.io.Serializable;

public class Dictitem implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String dictid;

    public String dictname;

    public String groupid;

    public String dictTime;

    /**
     * 请使用带参数的构造方法
     *
     * @deprecated
     */
    public Dictitem() {
    }

    public Dictitem(String dictid, String dictname) {
        this.dictid = dictid;
        this.dictname = dictname;
    }

    public Dictitem(String dictid, String dictname, String groupid) {
        this.dictid = dictid;
        this.dictname = dictname;
        this.groupid = groupid;
    }

    public String getDictid() {
        return dictid;
    }

    /**
     * 请使用带参数的构造方法
     *
     * @deprecated
     */
    public void setDictid(String dictid) {
        this.dictid = dictid;
    }

    public String getDictname() {
        return dictname;
    }

    /**
     * 请使用带参数的构造方法
     *
     * @deprecated
     */
    public void setDictname(String dictname) {
        this.dictname = dictname;
    }

    public String getGroupid() {
        return groupid;
    }

    /**
     * 请使用带参数的构造方法
     *
     * @deprecated
     */
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getDictTime() {
        return dictTime;
    }

    public void setDictTime(String dictTime) {
        this.dictTime = dictTime;
    }

    public void clone(Dictitem src) {
        this.dictid = src.dictid;
        this.dictname = src.dictname;
        this.dictTime = src.dictTime;
    }

    @Override
    public String toString() {
        return "Dictitem [dictid=" + dictid + ", dictname=" + dictname + ", groupid=" + groupid
                + ", dictTime=" + dictTime + "]";
    }

}
