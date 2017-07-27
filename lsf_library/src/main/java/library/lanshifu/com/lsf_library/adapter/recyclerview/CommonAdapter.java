package library.lanshifu.com.lsf_library.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ItemViewDelegate;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    public void add(T t){
        mDatas.add(t);
        notifyDataSetChanged();
    }

    public void addData(int position,T t) {
        mDatas.add(position, t);
        notifyItemInserted(position);
        /**在移动动画之后，重新刷新onBindViewHolder，使用notifyItemRangeChanged方法*/
        //可以刷新从positionStart开始itemCount数量的item了（这里的刷新指回调onBindViewHolder()方法）。
        notifyItemRangeChanged(position,mDatas.size()-position);

    }

    public void removeData(int position) {

        if(position<getItemCount()){
            mDatas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,mDatas.size()-position);
        }
    }


    public void removeAllData() {
        if(mDatas.size() >0){
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
       return mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addAll(List<T> datas){
        this.mDatas.addAll(datas);
        notifyDataSetChanged();

    }

    public void refresh(List<T> datas){
        removeAllData();
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void loadmore(List<T> list) {
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }
}
