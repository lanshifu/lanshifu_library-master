package library.lanshifu.com.myapplication.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private String baseUrl = "http://www.apkbus.com/";
    private List<Banner> list = new ArrayList<>();
    private BaseQuickAdapter<Banner, BaseViewHolder> adapter;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_jsoup;
    }

    @Override
    protected void onViewCreate() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<Banner, BaseViewHolder>(R.layout.list_item, list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, Banner banner) {
                baseViewHolder.setText(R.id.title, banner.title);
                ImageView imageView = baseViewHolder.getView(R.id.iv_icon);
                Glide.with(JsoupActivity.this)
                        .load(banner.imgUrl)
                        .into(imageView);

            }

        };
        recyclerView.setAdapter(adapter);

        /**
         *
         1.使用Document doc = Jsoup.parse(htmlString)方法加载我们需要解析Html。
         2.假设我们需要获取<h2 id="test">123<h2>元素里的123。
         3.调用Element test=doc.getElementById("test");就可以了。
         4.String value=test.text();就获取到了value="123"
         */

        RetrofitHelper.getJsoupApi().getBasePager().map(new Function<ResponseBody, List<Banner>>() {
            @Override
            public List<Banner> apply(@NonNull ResponseBody responseBody) throws Exception {
                List<Banner> bannerList = null;
                try {
                    bannerList = new ArrayList<Banner>();
                    Document document = Jsoup.parse(responseBody.string());
                    Element theTarget = document.getElementById("theTarget");
                    Elements children = theTarget.children();
                    Loge("children.size" + children.size());
                    for (Element child : children) {
                        Elements a = child.getElementsByTag("a");
                        String url = a.attr("href");
                        String imgUrl = a.select("img").attr("src");
                        if (!imgUrl.contains("http")) {
                            imgUrl = baseUrl + imgUrl;
                        }
                        String title = a.select("span").text();
                        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(imgUrl)) {
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

                        adapter.addData(banners);
                        Loge("banners.size" + banners.size());
                        for (Banner banner : banners) {
//                            textview.append(banner.toString() + "\n\n");
                            Loge(banner.toString() + "\n\n");
                        }

                    }

                    @Override
                    public void _onError(String e) {
                        textview.setText(e);
                    }
                });

    }



}
