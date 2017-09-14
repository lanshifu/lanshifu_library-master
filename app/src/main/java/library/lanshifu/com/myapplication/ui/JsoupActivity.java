package library.lanshifu.com.myapplication.ui;

import android.text.TextUtils;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.Banner;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RetrofitHelper;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;
import okhttp3.ResponseBody;

/**
 * Created by lanshifu on 2017/9/14.
 */

public class JsoupActivity extends BaseToolBarActivity {

    @Bind(R.id.textview)
    TextView textview;
    private String baseUrl = "http://www.apkbus.com/forum.php/";

    @Override
    protected int getLayoutid() {
        return R.layout.activity_jsoup;
    }

    @Override
    protected void onViewCreate() {

        RetrofitHelper.getJsoupApi().getBasePager().map(new Function<ResponseBody, List<Banner>>() {
            @Override
            public List<Banner> apply(@NonNull ResponseBody responseBody) throws Exception {
                List<Banner> bannerList = null;
                try {
                    bannerList = new ArrayList<Banner>();
                    Document document = Jsoup.parse(responseBody.string());
                    Element theTarget = document.getElementById("theTarget");
                    Elements children = theTarget.children();
                    for (Element child : children) {
                        Element a = child.getElementById("a");

                        String url = a.attr("href");
                        String imgUrl = a.select("img").attr("src");
                        if (!imgUrl.contains("http")) {
                            imgUrl = baseUrl + imgUrl;
                        }
                        String title = a.select("spam").text();
                        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(title)) {
                            bannerList.add(new Banner(url, title, imgUrl));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return bannerList;
            }
        }).compose(RxSchedulerHelper.<List<Banner>>io_main())
                .subscribe(new MyObserver<List<Banner>>() {
                    @Override
                    public void _onNext(List<Banner> banners) {
                        for (Banner banner : banners) {
                            textview.append(banner.toString() + "\r");
                        }

                    }

                    @Override
                    public void _onError(String e) {
                        textview.setText(e);
                    }
                });

    }


}
