package library.lanshifu.com.lsf_library.commwidget.flowtaglayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/7/8.
 */

public abstract class FlowTagAdapter<T> extends BaseAdapter {

    private List<T> mList = new ArrayList<>();
    private Context mContext;

    public FlowTagAdapter(Context context,List<T> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        if (converView == null) {
            converView = View.inflate(mContext, getLayoutId(), null);
        }

        bindView(converView,i, mList.get(i));
        return converView;
    }

    public void addAll(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    public abstract int getLayoutId();

    public abstract void bindView(View itemView, int position,T data);
}
