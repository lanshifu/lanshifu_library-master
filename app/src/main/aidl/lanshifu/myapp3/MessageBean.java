package lanshifu.myapp3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lanshifu on 2017/10/29.
 */

public class MessageBean implements Parcelable {
    private String content;//需求内容
    private int level;//重要等级

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.level);
        dest.writeString(this.content);
    }

    public MessageBean() {
    }

    protected MessageBean(Parcel in) {
        this.level = in.readInt();
        this.content = in.readString();
    }

    public static final Creator<MessageBean> CREATOR = new Creator<MessageBean>() {
        @Override
        public MessageBean createFromParcel(Parcel source) {
            return new MessageBean(source);
        }

        @Override
        public MessageBean[] newArray(int size) {
            return new MessageBean[size];
        }
    };

    /**
     * 参数是一个Parcel,用它来存储与传输数据
     * @param dest
     */
    public void readFromParcel(Parcel dest) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        content = dest.readString();
        level = dest.readInt();
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "content='" + content + '\'' +
                ", level=" + level +
                '}';
    }
}