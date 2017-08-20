package library.lanshifu.com.myapplication.imagepicker;

import cn.bmob.v3.BmobObject;

/**
 * Created by lanxiaobin on 2017/8/16.
 */

public class PictureBean extends BmobObject{

    public String name;       //图片的名字
    public String path;
    public String base64;
    public String desc;

    public PictureBean(String name, String path, String base64, String desc) {
        this.name = name;
        this.path = path;
        this.base64 = base64;
        this.desc = desc;
    }
}
