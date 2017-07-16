package library.lanshifu.com.myapplication.widget.popu;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import java.util.ArrayList;
import java.util.List;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.utils.Dictitem;
import library.lanshifu.com.myapplication.R;

/**
 * Created by lWX385269 lanshifu on 2017/6/1.
 */

public class ListPopWindow {

    public ListPopWindow(Context mContext) {
        this.mContext = mContext;
    }

    private PopupWindow mPopupWindow;
    private List<Dictitem> dictitems;
    private Context mContext;
    private int maxTextPositon = 0;
    private RecyclerView mRecyclerView;
    private int mResLayoutId = -1;
    private View mContentView;
    private int mWidth;
    private int mHeight;
    private OnPopupItemSelectListener onPopuSelectListener;
    private OnPopupDismissListener onPopupDismissListener;
    private int layoutId = -1;


    private PopupWindow build() {

        mContentView = LayoutInflater.from(mContext).inflate(R.layout.pop_list, null);
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        mRecyclerView.setAdapter(new CommonAdapter<Dictitem>(mContext, R.layout.item_textview, dictitems) {
            @Override
            protected void convert(ViewHolder holder, final Dictitem dictitem, final int position) {
                holder.setText(R.id.textview,dictitem.getDictname());
                holder.setOnClickListener(R.id.textview, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onPopuSelectListener != null) {
                            onPopuSelectListener.select(position, dictitem);
                        }
                        dismiss();
                    }
                });
            }


        });


        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onPopupDismissListener != null) {
                    onPopupDismissListener.onDismiss();
                }
            }
        });

        reMeasure();
        return mPopupWindow;

    }


    private void reMeasure() {
//        View item = mRecyclerView.getChildAt(maxTextPositon);
//        item.measure(0, 0);
//
//        int count = mRecyclerView.getAdapter().getItemCount();
//        int height = item.getMeasuredHeight() * count +
//                +mRecyclerView.getPaddingTop() + mRecyclerView.getPaddingBottom();
//        int width = item.getMeasuredWidth() + mRecyclerView.getPaddingLeft()
//                + mRecyclerView.getPaddingRight();
//
//        int maxHeight = mContext.getResources().getDisplayMetrics().heightPixels / 3;
//
//        mPopupWindow.setWidth(Math.max(width, mContentView.getMeasuredWidth()));
//        mPopupWindow.setHeight(Math.min(height, maxHeight));
    }


    /**
     *
     * @param anchor
     * @param xOff
     * @param yOff
     * @return
     */
    public ListPopWindow showAsDropDown(View anchor, int xOff, int yOff){
        if(mPopupWindow!=null){
            mPopupWindow.showAsDropDown(anchor,xOff,yOff);
        }
        return this;
    }

    public ListPopWindow showAsDropDown(View anchor){
        if(mPopupWindow!=null){
            mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ListPopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity){
        if(mPopupWindow !=null){
            mPopupWindow.showAsDropDown(anchor,xOff,yOff,gravity);
        }
        return this;
    }


    /**
     * 相对于父控件的位置（通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
     * @param parent 父控件
     * @param gravity
     * @param x the popup's x location offset
     * @param y the popup's y location offset
     * @return
     */
    public ListPopWindow showAtLocation(View parent, int gravity, int x, int y){
        if(mPopupWindow !=null){
            mPopupWindow.showAtLocation(parent,gravity,x,y);
        }
        return this;
    }


    public void dismiss() {
        if(mPopupWindow !=null){
            mPopupWindow.dismiss();
        }
    }





    public static class Builder {

        private ListPopWindow listPopWindow;

        public Builder(Context context) {
            listPopWindow = new ListPopWindow(context);
        }

        public Builder setData(List dictitems) {

            List<Dictitem> dictitemList = new ArrayList<>();

            if(dictitems.get(0) instanceof String){
                List<String> strings = dictitems;
                for (String s : strings) {
                    dictitemList.add(new Dictitem(s, s));
                }
            }else if(dictitems.get(0) instanceof Dictitem){
                dictitemList = dictitems;
            }

            listPopWindow.dictitems = dictitemList;
            return this;
        }


        public Builder SetOnPopupItemSelectListener(OnPopupItemSelectListener listener) {
            listPopWindow.onPopuSelectListener = listener;
            return this;
        }

        public Builder SetOnPopupDismissListener(OnPopupDismissListener listener) {
            listPopWindow.onPopupDismissListener = listener;
            return this;
        }

        public ListPopWindow build() {
            listPopWindow.build();
            return listPopWindow;
        }
    }


    public interface OnPopupItemSelectListener {
        void select(int position, Dictitem dictitem);
    }

    public interface OnPopupDismissListener {
        void onDismiss();
    }

}
