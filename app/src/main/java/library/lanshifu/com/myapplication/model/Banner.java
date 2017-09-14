package library.lanshifu.com.myapplication.model;

/**
 * Created by lanshifu on 2017/9/14.
 */

public class Banner {
    public String url;
    public String title;
    public String imgUrl;

    @Override
    public String toString() {
        return "Banner{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    public Banner(String url, String title, String imgUrl) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
    }
}
