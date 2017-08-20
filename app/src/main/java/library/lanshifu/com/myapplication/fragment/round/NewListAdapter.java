package library.lanshifu.com.myapplication.fragment.round;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import library.lanshifu.com.lsf_library.adapter.recyclerview.MultiItemTypeAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ItemViewDelegate;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.WechatItem;
import library.lanshifu.com.myapplication.ui.NewsDetailActivity;
import library.lanshifu.com.myapplication.utils.PixelUtil;
import library.lanshifu.com.myapplication.widget.transition.EasyTransition;
import library.lanshifu.com.myapplication.widget.transition.EasyTransitionOptions;

/**
 * Created by Administrator on 2017/8/15.
 */

public class NewListAdapter extends MultiItemTypeAdapter<WechatItem.ResultBean.ListBean> {

    private boolean isNotLoad = false;
    protected Context mContext;
    protected int mLayoutId;
    protected List<WechatItem.ResultBean.ListBean> mDatas;
    protected LayoutInflater mInflater;

    int imgWidth = PixelUtil.getWindowWidth();
    int imgHeight = imgWidth * 3 / 4;

    public NewListAdapter(Context context, List<WechatItem.ResultBean.ListBean> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<WechatItem.ResultBean.ListBean>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return  R.layout.item_wechat_style1;
            }

            @Override
            public boolean isForViewType(WechatItem.ResultBean.ListBean item, int position)
            {
                return item.getItemType() == 1;
            }

            @Override
            public void convert(final ViewHolder holder, final WechatItem.ResultBean.ListBean item, int position)
            {
                holder.setText(R.id.title_wechat_style1, TextUtils.isEmpty(item.getTitle()) ? "微信精选" : item.getTitle());

                if (!isNotLoad) {
                    Glide.with(mContext.getApplicationContext())
                            .load(item.getFirstImg())
                            .override(imgWidth, imgHeight)
                            .placeholder(R.drawable.ic_image_loading)
                            .error(R.mipmap.ic_launcher)
                            .crossFade(1000)
                            .into((ImageView) holder.getView(R.id.ivImage));
                }
                holder.setOnClickListener(R.id.ivImage, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toDetailActivity(item, holder.getView(R.id.ivImage));
                    }
                });

            }
        });


        addItemViewDelegate(new ItemViewDelegate<WechatItem.ResultBean.ListBean>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return  R.layout.item_wechat_style2;
            }

            @Override
            public boolean isForViewType(WechatItem.ResultBean.ListBean item, int position)
            {
                return item.getItemType() != 1;
            }

            @Override
            public void convert(final ViewHolder helper, final WechatItem.ResultBean.ListBean item, int position)
            {
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
                        toDetailActivity(item,helper.getView(R.id.ivImage));
                    }
                });
            }
        });

    }


    public void addAll(List<WechatItem.ResultBean.ListBean> datas){
        this.mDatas.addAll(datas);
        notifyDataSetChanged();

    }
     public void refresh(List<WechatItem.ResultBean.ListBean> datas){
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        notifyDataSetChanged();

    }


    private void toDetailActivity(WechatItem.ResultBean.ListBean listBean, View view){
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra("listBean",listBean);
        // ready for transition options
        EasyTransitionOptions options =
                EasyTransitionOptions.makeTransitionOptions(
                        (Activity) mContext,
                        view);
        // start transition
        EasyTransition.startActivity(intent, options);
    }


}
