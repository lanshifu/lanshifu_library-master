package library.lanshifu.com.myapplication.fragment.round;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.WechatItem;
import library.lanshifu.com.myapplication.ui.NewsDetailActivity;
import library.lanshifu.com.myapplication.utils.PixelUtil;
import library.lanshifu.com.myapplication.widget.transition.EasyTransition;
import library.lanshifu.com.myapplication.widget.transition.EasyTransitionOptions;

/**
 * Created by Administrator on 2017/8/15.
 */

public class NewListAdapter extends BaseMultiItemQuickAdapter<WechatItem.ResultBean.ListBean, BaseViewHolder> {

    private boolean isNotLoad = false;
    protected Context mContext;

    int imgWidth = PixelUtil.getWindowWidth();
    int imgHeight = imgWidth * 3 / 4;

    public NewListAdapter(Context context, List<WechatItem.ResultBean.ListBean> datas) {
        super(datas);
        mContext = context;

        addItemType(0, R.layout.item_wechat_style2);
        addItemType(1, R.layout.item_wechat_style1);
    }


    public void addAll(List<WechatItem.ResultBean.ListBean> datas) {
        addData(datas);

    }

    public void refresh(List<WechatItem.ResultBean.ListBean> datas) {
        getData().clear();
        addData(datas);
        notifyDataSetChanged();

    }


    private void toDetailActivity(WechatItem.ResultBean.ListBean listBean, View view) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra("listBean", listBean);
        // ready for transition options
        EasyTransitionOptions options =
                EasyTransitionOptions.makeTransitionOptions(
                        (Activity) mContext,
                        view);
        // start transition
        EasyTransition.startActivity(intent, options);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final WechatItem.ResultBean.ListBean item) {
        switch (helper.getItemViewType()) {
            case 0:
                helper.setText(R.id.title_wechat_style2, TextUtils.isEmpty(item.getTitle()) ? "微信精选" : item.getTitle());
                if (!isNotLoad) {
                    Glide.with(mContext.getApplicationContext())
                            .load(item.getFirstImg())
                            .placeholder(R.drawable.ic_image_loading)
                            .error(R.mipmap.ic_launcher)
                            .override(imgWidth / 2, imgHeight / 2)
                            .crossFade(1000)
                            .into((ImageView) helper.getView(R.id.ivImage));
                }
                helper.setOnClickListener(R.id.ivImage, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toDetailActivity(item, helper.getView(R.id.ivImage));
                    }
                });
                break;
            case 1:
                helper.setText(R.id.title_wechat_style1, TextUtils.isEmpty(item.getTitle()) ? "微信精选" : item.getTitle());
                if (!isNotLoad) {
                    Glide.with(mContext.getApplicationContext())
                            .load(item.getFirstImg())
                            .override(imgWidth, imgHeight)
                            .placeholder(R.drawable.ic_image_loading)
                            .error(R.mipmap.ic_launcher)
                            .crossFade(1000)
                            .into((ImageView) helper.getView(R.id.ivImage));
                }
                helper.setOnClickListener(R.id.ivImage, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toDetailActivity(item, helper.getView(R.id.ivImage));
                    }
                });


                break;
        }

    }
}
