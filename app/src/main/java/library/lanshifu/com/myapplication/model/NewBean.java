package library.lanshifu.com.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.annotation.Encrypt;
import org.litepal.crud.DataSupport;

/**
 * Created by lanxiaobin on 2017/8/15.
 */

public  class NewBean extends DataSupport implements Parcelable {
    public NewBean(String id, String title, String source, String firstImg, String mark, String url, int itemType, int spansize) {
        this.newid = id;
        this.title = title;
        this.source = source;
        this.firstImg = firstImg;
        this.mark = mark;
        this.url = url;
        this.itemType = itemType;
        this.spansize = spansize;
    }

    /**
     * id : wechat_20170222006600
     * title : 不死的基因
     * source : 大科技
     * firstImg : http://zxpic.gtimg.com/infonew/0/wechat_pics_-13427722.jpg/640
     * mark :
     * url : http://v.juhe.cn/weixin/redirect?wid=wechat_20170222006600
     */
    public static final int STYLE_BIG = 1;
    public static final int STYLE_SMALL = 0;

    public static final int STYLE_SMALL_SPAN_SIZE = 1;
    public static final int STYLE_BIG_SPAN_SIZE = 2;

    @Column(unique = true)
    private String newid;
    private String title;
    private String source;
    private String firstImg;
    private String mark;
    @Encrypt(algorithm = AES)
    private String url;

    private int itemType = 0;
    private int spansize = 1;

    public int getSpansize() {
        return spansize;
    }

    public void setSpansize(int spansize) {
        this.spansize = spansize;
    }

    public String getId() {
        return newid;
    }

    public void setId(String id) {
        this.newid = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        if (itemType == 1 || itemType == 0) {
            this.itemType = itemType;
        } else {
            this.itemType = 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.newid);
        dest.writeString(this.title);
        dest.writeString(this.source);
        dest.writeString(this.firstImg);
        dest.writeString(this.mark);
        dest.writeString(this.url);
        dest.writeInt(this.itemType);
        dest.writeInt(this.spansize);
    }

    public NewBean() {
    }

    protected NewBean(Parcel in) {
        this.newid = in.readString();
        this.title = in.readString();
        this.source = in.readString();
        this.firstImg = in.readString();
        this.mark = in.readString();
        this.url = in.readString();
        this.itemType = in.readInt();
        this.spansize = in.readInt();
    }

    public static final Creator<NewBean> CREATOR = new Creator<NewBean>() {
        @Override
        public NewBean createFromParcel(Parcel source) {
            return new NewBean(source);
        }

        @Override
        public NewBean[] newArray(int size) {
            return new NewBean[size];
        }
    };
}
