package library.lanshifu.com.myapplication.twolist;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lanxiaobin on 2017/7/17.
 */



public class SortBean implements Serializable {
    private String name;
    private String tag;
    private boolean isTitle;

    private List<DetailBean> detailBeanList;
    public static class DetailBean{
        private String title;
        private String msg;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public SortBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }







}
