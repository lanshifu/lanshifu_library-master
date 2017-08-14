package library.lanshifu.com.myapplication.fragment.round;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.MultiItemTypeAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ItemViewDelegate;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.WechatItem;
import library.lanshifu.com.myapplication.utils.PixelUtil;

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
            public boolean isForViewType( WechatItem.ResultBean.ListBean item, int position)
            {
                return item.getItemType() == 1;
            }

            @Override
            public void convert(ViewHolder holder, WechatItem.ResultBean.ListBean item, int position)
            {
                holder.setText(R.id.title_wechat_style1, TextUtils.isEmpty(item.getTitle()) ? "微信精选" : item.getTitle());

                if (!isNotLoad) {
                    Glide.with(mContext.getApplicationContext())
                            .load(item.getFirstImg())
                            .override(imgWidth, imgHeight)
                            .placeholder(R.drawable.ic_image_loading)
                            .error(R.mipmap.ic_launcher)
                            .crossFade(1000)
                            .into((ImageView) holder.getView(R.id.img_wechat_style));
                }

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
            public boolean isForViewType( WechatItem.ResultBean.ListBean item, int position)
            {
                return item.getItemType() != 1;
            }

            @Override
            public void convert(ViewHolder helper, WechatItem.ResultBean.ListBean item, int position)
            {
                helper.setText(R.id.title_wechat_style2, TextUtils.isEmpty(item.getTitle()) ? "微信精选" : item.getTitle());
                if (!isNotLoad) {
                    Glide.with(mContext.getApplicationContext())
                            .load(item.getFirstImg())
                            .placeholder(R.drawable.ic_image_loading)
                            .error(R.mipmap.ic_launcher)
                            .override(imgWidth / 2, imgHeight / 2)
                            .crossFade(1000)
                            .into((ImageView) helper.getView(R.id.img_wechat_style));
                }
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



}
